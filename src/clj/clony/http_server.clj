(ns clony.http-server
  (:use compojure.core)
  (:require
   [compojure.handler :as chandler]
   [compojure.route :as croute]
   [org.httpkit.server :as httpkit]
   [ring.util.response :refer [file-response]]
   [cheshire.core :as json]
   [taoensso.timbre :as timbre
    :refer (trace debug info warn error fatal spy with-log-level)]
   [clony.lifecycle :as lifecycle]))

(def quotes
  ["Hey. I could clear this sky in ten seconds flat"
   "Nopony knows! You know why? Because everypony who's ever come in...has never...come...OUT!"
   "See? I'd never leave my friends hanging!"
   "Time to take out the adorable trash"
   "It needs to be about 20% cooler"
   "I'm... hanging... with the... Wonderbolts!"
   "Danger's my middle name. Rainbow 'Danger' Dash."])

(defn ws-handler [req]
  (httpkit/with-channel req chan
    (httpkit/on-close chan (fn [status] (debug "channel closed" status)))
    (httpkit/on-receive chan
      (fn [raw-msg]
        (let [data (json/decode raw-msg)]
          (debug "incoming WS data" data))
        (let [reply (json/encode {:msg (rand-nth quotes)})]
          (httpkit/send! chan reply))))))

(defroutes app-routes
  (GET "/" [] (file-response "resources/landing.html"))
  (GET "/ws" [] ws-handler)
  (croute/resources "/static")
  (croute/not-found "Nothing to see here, move along"))

(defn wrap-logging [handler]
  (fn [req]
    (let [resp (handler req)]
      (debug (str (if (:websocket? req) "WS req: " "req: ")
                  (:uri req) " " (:status resp)))
      resp)))

(def app
  (-> (chandler/site #'app-routes)
      (wrap-logging)))

(defrecord HttpNode [config handler stopper]
  lifecycle/Node
  (start [s]
    (let [stopper (httpkit/run-server app config)]
      (assoc s :stopper stopper :handler app)))
  (stop [s]
    ((:stopper s))
    (assoc s :stopper nil)))

(defn prepare [config]
  {:node (->HttpNode config nil nil)
   :children []})

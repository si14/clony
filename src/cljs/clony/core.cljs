(ns clony.core
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy])
  (:use-macros
   [dommy.macros :only [node sel sel1]]))

(def ws-url "ws://localhost:8081/ws")
(def ws (new js/WebSocket ws-url))

(defn click-handler [evt]
  (->> {:msg "gimme a quote!"}
       clj->js
       (.stringify js/JSON)
       (.send ws)))

(defn msg-handler [raw-msg]
  (let [msg (.parse js/JSON raw-msg)
        msg (js->clj msg :keywordize-keys true)]
    (dommy/prepend! (sel1 :#somediv) [:div (:msg msg)])))

(defn init []
  (dommy/listen! (sel1 :#rainbowdash) :click click-handler)
  (set! (.-onmessage ws) (fn [msg] (msg-handler (.-data msg)))))

(def on-load
  (set! (.-onload js/window) init))

(ns user
  (:require
   [clojure.pprint :refer [pprint]]
   [cemerick.pomegranate :as pomegranate]
   [clojure.tools.namespace.repl :as ctnrepl]
   [criterium.core :as cr]
   [clony.core :as core]
   [clony.lifecycle :as lifecycle]))

(def dev-config
  {:http {:port 8081}})

(defonce sys nil)

(defn create []
  (alter-var-root #'sys (fn [_] (core/prepare-system dev-config))))

(defn start []
  (lifecycle/start-node! sys))

(defn create-and-start []
  (create)
  (start))

(defn stop []
  (lifecycle/stop-node! sys))

(defn restart []
  (lifecycle/restart-node! sys))

(defn refresh []
  (stop)
  (ctnrepl/refresh :after 'user/create-and-start)
  :ok)

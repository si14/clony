(ns clony.core
  (:require [clony.lifecycle :as lifecycle])
  (:require [clony.http-server :as http-server]))

(def default-config
  {:http {:port 8081}})

(defrecord RootNode [config]
  lifecycle/Node
  (start [s]
    s)
  (stop [s]
    s))

(defn prepare-root [config]
  {:node (->RootNode config)
   :children [#(http-server/prepare (:http config))]})

(defn prepare-system [config]
  (let [root-node (prepare-root config)]
    (lifecycle/prepare-node root-node)))

(defn run-system []
  (let [sys (prepare-system default-config)]
    (lifecycle/start-node! sys)))

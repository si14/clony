(ns clony.lifecycle
  (:require [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]))

(defprotocol Node
  (start [_])
  (stop [_]))

(defn prepare-node [{:keys [node children]}]
  {:node (atom node)
   :children (vec (for [c children]
                    (prepare-node (c))))
   :state (atom :prepared)})

(defn start-node! [{:keys [node state children] :as nodemap}]
  (info (str "starting node of " (type @node)))
  (swap! node start)
  (->> children (map start-node!) dorun)
  (reset! state :started)
  (info (str "started node of " (type @node)))
  nodemap)

(defn stop-node! [{:keys [node state children] :as nodemap}]
  (info (str "stopping node of " (type @node)))
  (swap! node stop)
  (->> children (map stop-node!) dorun)
  (reset! state :stopped)
  (info (str "stopped node of " (type @node)))
  nodemap)

(defn restart-node! [node]
  (stop-node! node)
  (start-node! node))

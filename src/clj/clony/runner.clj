(ns clony.runner
  (:require [clony.core :as core])
  (:gen-class))

(defn -main
  [& args]
  (core/run-system))

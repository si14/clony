(defproject clony "0.1.0-SNAPSHOT"
  :description "modern Clojure webapp example"
  :source-paths ["src/clj"]
  :plugins [[lein-cljsbuild "0.3.3"]
            [lein-midje "3.0.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1877"]

                 [midje "1.6-beta1"]
                 [com.taoensso/timbre "2.6.1"]
                 [cheshire "5.2.0"]
                 [http-kit "2.1.10"]
                 [compojure "1.1.5"]

                 [prismatic/dommy "0.1.1"]]
  :main clony.runner
  :aot [clony.runner]
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"],
                :compiler {:output-dir "resources/public/js/",
                           :output-to "resources/public/js/main.js",
                           :pretty-print true,
                           :optimizations :whitespace}}]}
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]
                                  [com.cemerick/pomegranate "0.2.0"]
                                  [criterium "0.4.2"]]
                   :source-paths ["dev"]
                   :main user}})

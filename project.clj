(defproject org.clojars.mike706574/demo "0.0.1-SNAPSHOT"
  :description "TODO"
  :url "https://github.com/mike706574/demo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.3.442"]
                 [com.stuartsierra/component "0.3.2"]

                 ;; Utility
                 [com.cognitect/transit-clj "0.8.300"]
                 [manifold "0.1.6"]
                 [byte-streams "0.2.2"]
                 [environ "1.1.0"]
                 [com.rabbitmq/amqp-client "4.1.0"]

                 ;; Logging
                 [com.taoensso/timbre "4.10.0"]

                 ;; Web
                 [aleph "0.4.3"]
                 [ring/ring-anti-forgery "1.0.1"]
                 [ring/ring-defaults "0.2.3"]
                 [ring-middleware-format "0.7.2"]
                 [compojure "1.5.2"]
                 [com.cemerick/friend "0.3.0-SNAPSHOT"]
                 [selmer "1.10.7"]

                 ;; ClojureScript
                 [org.clojure/clojurescript "1.9.521"]
                 [com.cognitect/transit-cljs "0.8.239"]

                 [cljsjs/react-with-addons "15.4.2-2"]
                 [com.yetanalytics/re-mdl "0.1.5"]
                 [reagent "0.6.1" :exclusions [cljsjs/react]]
                 [re-frame "0.9.2" :exclusions [cljsjs/react]]

                 [day8.re-frame/http-fx "0.1.3"]
                 [cljs-ajax "0.5.9"]]
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :plugins [[lein-cljsbuild "1.1.5"]
            [cider/cider-nrepl "0.14.0"]
            [org.clojure/tools.nrepl "0.2.12"]
            [lein-figwheel "0.5.10"]]
  :profiles {:dev {:source-paths ["dev"]
                   :target-path "target/dev"
                   :dependencies [[org.clojure/test.check "0.9.0"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.10"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
             :production {:aot :all
                          :main demo.server.main
                          :uberjar-name "demo.jar"}}
  :cljsbuild {:builds {:dev {:source-paths ["src/cljs"]
                             :figwheel {:on-jsload "demo.client.core/refresh"
                                        :websocket-host "192.168.1.141"}
                             :compiler {:main "demo.client.core"
                                        :asset-path "js"
                                        :source-map true
                                        :source-map-timestamp true
                                        :optimizations :none
                                        :output-dir "resources/public/js"
                                        :output-to "resources/public/client.js"}}
                       :production {:source-paths ["src/cljs"]
                                    :compiler {:output-dir "target/js"
                                               :optimizations :advanced
                                               :elide-asserts true
                                               :pretty-print false
                                               :output-to "resources/public/client.js"}}}}
  :figwheel {:repl false
             :http-server-root "public"}
  :clean-targets ^{:protect false} ["resources/public/js"]
  :aliases {"production-build" ["with-profile" "production" "do" "clean" ["cljsbuild" "once" "production"] ["uberjar"]]})

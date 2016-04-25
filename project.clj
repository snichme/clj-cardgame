(defproject cardgame "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.5.0"]
                 [duct "0.5.10"]
                 [environ "1.0.2"]
                 [meta-merge "0.1.1"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring-jetty-component "0.3.1"]
                 [ring/ring-json "0.4.0"]
                 [duct/hikaricp-component "0.1.0"]
                 [org.postgresql/postgresql "9.4.1207"]
                 [honeysql "0.6.3"]
                 [duct/ragtime-component "0.1.3"]]
  :plugins [[lein-environ "1.0.2"]
            [lein-gen "0.2.2"]
            [lein-cljsbuild "1.1.2"]]

  :jvm-opts ["-Dclojure.server.repl={:port 5555 :accept clojure.core.server/repl}"]

  :source-paths ["src/clj" "src/cljs" "dev"]
  :test-paths ["test/clj"]
  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :generators [[duct/generators "0.5.10"]]
  :duct {:ns-prefix cardgame}
  :main ^:skip-aot cardgame.main
  :target-path "target/%s/"
  :resource-paths ["resources"]
  :prep-tasks [["javac"] ["cljsbuild" "once"] ["compile"]]
  :cljsbuild {:builds {:main {:jar          true
                              :source-paths ["src/cljs"]
                              :compiler     {:main          cardgame.core
                                             :output-to     "resources/public/js/main.js"
                                             :optimizations :advanced}}}}
  :aliases {"gen"   ["generate"]
            "setup" ["do" ["generate" "locals"]]}
  :profiles {
             :dev           [:project/dev :profiles/dev]
             :test          [:project/test :profiles/test]
             :repl          {:prep-tasks ^:replace [["javac"] ["compile"]]}
             :uberjar       {:aot :all}
             :profiles/dev  {}
             :profiles/test {}
             :project/dev   {:dependencies [[reloaded.repl "0.2.1"]
                                            [org.clojure/tools.namespace "0.2.11"]
                                            [org.clojure/tools.nrepl "0.2.12"]
                                            [eftest "0.1.1"]
                                            [kerodon "0.7.0"]
                                            [com.cemerick/piggieback "0.2.1"]
                                            [duct/figwheel-component "0.3.2"]
                                            [figwheel "0.5.0-6"]]
                             :source-paths ["dev"]
                             :repl-options {:init-ns          user
                                            :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                             :env          {:port "3000"}}
             :project/test  {}})

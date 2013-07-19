(defproject hackedio "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [overtone "0.8.1" :exclusions [org.clojure/clojure]]
                 [org.thnetos/cd-client "0.3.6" :exclusions [[org.clojure/clojure]
                                                             cheshire]]
                 [clj-http "0.7.5"]
                 [com.novemberain/monger "1.6.0"]]

  :plugins [[lein-cljsbuild "0.2.9"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:crossovers []
              :crossover-jar true
              :builds [{:source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/main.js"
                                   :warnings true
                                   :optimizations :whitespace
                                   :print-input-delimiter false
                                   :pretty-print true}}]})

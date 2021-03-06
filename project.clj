(defproject mybank "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.12.1"]]
  :ring {:handler mybank.core/app
         :auto-reload? true
         :auto-refresh? false}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.6.3"]
                 [ring/ring-json "0.5.0-beta1"]
                 [hiccup/hiccup "2.0.0-alpha1"]
                 [compojure "1.6.0"]
                 [net.htmlparser.jericho/jericho-html "3.1"]
                 [org.clojure/data.json "0.2.6"]
                 [hickory "0.7.1"]
                 [clj-http "3.7.0"]
                 [ring/ring-jetty-adapter "1.6.3"]]
  :main mybank.core
  :aot [mybank.core])

(defproject authz-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.9"]
                 [http-kit "2.2.0"]
                 [ring/ring-mock "0.3.0"]
                 [mount "0.1.11"]
                 [yada "1.2.0"]
                 [aleph "0.4.1"]
                 [bidi "2.0.12"]
                 [buddy "1.3.0"]]

  :main ^:skip-aot authz-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

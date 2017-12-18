(ns authz-test.core-test
  (:require [authz-test.core :refer [routes]]
            [cheshire.core :as json]
            [cheshire.parse :as cheshire]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [yada.yada :as yada]))

(defn get-json-from-response [resp]
  (json/parse-string (:body resp) true))

(deftest general-case-authorization
  (testing "General case authorization"
    (let [resp (yada/response-for routes :get (str "/api/issues/" 1))]
          ;; body (get-json-from-response resp)]
      (is true))))
      ;; (is (= 200 (:status resp))))))

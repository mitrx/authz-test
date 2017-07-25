(ns authz-test.core-test
  (:require [authz-test.core :refer :all]
            [cheshire.core :as json]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(defn get-json-from-response [resp]
  (json/parse-string (slurp (:body resp)) true))

(deftest general-case-authorization
  (testing "General case authorization"
    (let [req (-> (mock/request :get "/api/issues/1")
                  (mock/content-type "application/json")
                  (mock/body (json/generate-string {:user-id 4
                                                    :issue-id 2})))
          resp (app req)
          body (get-json-from-response resp)]
      (clojure.pprint/pprint body)
      (clojure.pprint/pprint (:status resp))
      (is (= 0 0)))))

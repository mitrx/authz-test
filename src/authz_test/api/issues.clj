(ns authz-test.api.issues
  (:require [authz-test.db.issues :as issues :refer [issues]]
            [yada.yada :as yada]))

(defn get-issues []
  issues)

(defn get-issue-by-id [ctx]
  (let [issue-id (-> ctx :parameters :path :issue-id)]
    (issues/get-issue issue-id)))

(def issues-resource
  (yada/resource {:produces {:media-type "application/json"}
                  :methods {:get {:response get-issues}}}))

(def issue-resource
  (yada/resource {:produces #{"application/json"}
                  :parameters {:path {:issue-id Long}}
                  :methods {:get {:response get-issue-by-id}}
                  :responses {404 {:produces #{"application/json"}
                                   :response {:description "no-such-issue"}}}}))

(ns authz-test.api.issues
  (:require [authz-test.db.issues :as issues :refer [issues]]
            [yada.yada :as yada]
            [authz-test.db.organizations :as organizations]))

(defn get-issues []
  issues)

(defn get-issue-by-id [ctx]
  (let [issue-id (-> ctx :parameters :path :issue-id)]
    (issues/get-issue issue-id)))

(def issues-resource
  (yada/resource {:produces {:media-type "application/json"}
                  :methods {:get {:response get-issues}}}))

(defn get-issue-properties [ctx]
  (let [params (-> ctx :parameters)
        issue-id (-> params :path :issue-id)
        user-id (-> params :query :user-id)
        issue (issues/get-issue issue-id)
        user-orgs (organizations/get-user-organizations user-id)
        issue-org-id (:organization-id issue)
        issue-org (organizations/get-organization issue-org-id)]
    {:exists? (not (nil? issue))
     :issues/issue issue
     :users/organization-member? (some #(= (:organization-id %) issue-org-id) user-orgs)
     :issues/issue-author? (= user-id (:author-id issue))
     :issues/issue-responsible? (= user-id (:responsible-id issue))}))

(def issue-resource
  (yada/resource {:produces #{"application/json"}
                  :parameters {:path {:issue-id Long}
                               :query {:user-id Long}}
                  :properties get-issue-properties
                  :access-control {:authorization
                                   {:scheme :authz-test/abac
                                    :predicate [:or :users/organization-member?
                                                :issues/issue-author?
                                                :issues/issue-responsible?]}}
                  :methods {:get {:response (fn [ctx] (-> ctx :properties :issues/issue))}}
                  :responses {404 {:produces #{"application/json"}
                                   :response {:description "no-such-issue"}}}}))

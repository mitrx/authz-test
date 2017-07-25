(ns authz-test.api.authorization
  (:require [authz-test.db.issues :as issues]
            [authz-test.db.organizations :as organizations]
            [cheshire.core :as json]
            [ring.util.http-response :as resp]))

(defn get-json-from-response [resp]
  (json/parse-string (slurp (:body resp)) true))

(defn get-attrs [req]
  (print "hello"))

(defn check-against-rules [attrs]
  true)

(defn authorized? [req]
  (let [body (get-json-from-response req)
        user-id (:user-id body)
        user-organizations (organizations/get-user-organizations user-id)
        issue-id (:issue-id body)
        issue (issues/get-issue issue-id)
        a (print issue)
        issue-organization-id (:organization-id issue)
        issue-organization (organizations/get-organization issue-organization-id)
        author-id (:author-id issue)
        responsible-id (:responsible-id issue)
        organization-id (:organization-id issue)]

    (clojure.pprint/pprint (str "OUTPUT: " user-id " " issue-id " " author-id " " responsible-id))
    (or (= user-id author-id)
        (= user-id responsible-id)
        (some #(= (:organization-id %) issue-organization-id) user-organizations))))

(defn wrap-abac-authorization [handler]
  (fn [req]
    (if (authorized? req)
      (handler req)
      (resp/unauthorized {:error {:message "not-authorized"}}))))

;; (defmulti get-attrs (-> :entity))

;; (defmethod get-attrs :users [params]
;;   (str "hello"))

;; (defmethod get-attrs :default [params]
;;   (str "fock"))

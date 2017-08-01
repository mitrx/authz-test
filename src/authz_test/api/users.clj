(ns authz-test.api.users
  (:require [authz-test.db.users :refer [users]]
            [ring.util.http-response :as resp]
            [schema.core :as sc]
            [yada.yada :as yada]))

(defn get-user-by-email [email]
  (filter #(= email (:email %)) users))

(defn get-users [ctx]
  (let [users (if (:email ctx)
                (get-user-by-email (:email ctx))
                users)]
    users))

(defn get-user-by-id [ctx]
  (let [msg (clojure.pprint/pprint ctx)
        id (-> ctx :parameters :path :user-id)
        users (filter #(= id (:user-id %)) users)]
    (when-not (empty? users)
      (first users))))

(def user-resource
  (yada/resource {:produces #{"application/json" "text/plain;q=0.9"}
                  :parameters {:path {:user-id Long}}
                  :methods {:get {:response get-user-by-id}}
                  :responses {404 {:produces #{"application/json"}
                                   :response {:description "no-such-user"}}}}))

(def users-resource
  (yada/resource {:produces {:media-type "application/json"}
                  :methods {:get {:response get-users}}}))

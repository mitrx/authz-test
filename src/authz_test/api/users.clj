(ns authz-test.api.users
  (:require [compojure.api.sweet :refer [context defroutes GET]]
            [schema.core :as sc]
            [authz-test.db.users :refer [users]]
            [ring.util.http-response :as resp]))

(defn resp-ok-users [users]
  (resp/ok {:users users}))

(defn get-user-by-email [email]
  (filter #(= email (:email %)) users))

(defn get-users [filters]
  (let [users (if (:email filters)
                (get-user-by-email (:email filters))
                users)]
    (resp-ok-users users)))

(defn get-user-by-id [id]
  (let [users (first (filter #(= id (:user-id %)) users))]
    (if-not (empty? users)
      (resp-ok-users users)
      (resp/not-found {:error {:message "no-such-user"}}))))

(defroutes users-routes
  (context "/users" []
           (GET "/" []
                :query-params [{email :- sc/Str nil}]
                (get-users {:email email}))
           (GET "/:id" [id]
                :path-params [id :- Long]
                (get-user-by-id id))))

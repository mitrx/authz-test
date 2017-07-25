(ns authz-test.db.organizations
  (:require [authz-test.db.roles-organizations :as roles-organizations]))

(def organizations [{:organization-id 1
                     :name "Default organization"
                     :type "cities"}
                    {:organization-id 2
                     :name "Some other organization"
                     :type "cities"}])

(defn get-organization [organization-id]
  (first (filter #(= (:organization-id %) organization-id) organizations)))

(defn get-matching-element [x match-col k]
  (first (filter #(= (k x) (k %)) match-col)))

(get-matching-element {:organization-id 2} organizations :organization-id)

(defn join-maps [m merge-m k merge-k]
  (let [merge-col (map
                   (fn [x] (first (filter #(= (k x) (merge-k %)) merge-m)))
                   m)
        result-col (mapv merge m merge-col)]
    result-col))

(defn get-user-organizations [user-id]
  (let [user-organizations-roles (roles-organizations/get-users-organization-roles user-id)
        user-organizations (join-maps user-organizations-roles organizations
                                      :organization-id :organization-id)]
    user-organizations))

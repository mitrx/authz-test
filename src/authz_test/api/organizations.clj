(ns authz-test.api.organizations
  (:require [yada.yada :as yada]
            [authz-test.db.organizations :as organizations]))

(defn get-organizations [ctx]
  organizations/organizations)

(defn get-organizations-by-id [ctx]
  (let [organization-id (-> ctx :parameters :path :organization-id)]
    (organizations/get-organization organization-id)))

(def organizations-resource
  (yada/resource {:produces {:media-type "application/json"}
                  :methods {:get {:response get-organizations}}}))

(def organization-resource
  (yada/resource {:produces {:media-type "application/json"}
                  :parameters {:path {:organization-id Long}}
                  :methods {:get {:response get-organizations-by-id}}
                  :responses {404 {:produces "application/json"
                                   :response {:description "no-such-organization"}}}}))

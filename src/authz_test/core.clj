(ns authz-test.core
  (:gen-class)
  (:require [compojure.api.sweet :refer [defapi context GET]]
            [ring.util.http-response :as resp]
            [org.httpkit.server :as httpkit]
            [schema.core :as sc]
            [mount.core :refer [defstate]]
            [authz-test.api.users :as users]
            [authz-test.api.issues :as issues]
            [authz-test.api.organizations :as organizations]))

(defn wrap-abac-authorization [handler]
  (fn [req]
    (let [print-req (clojure.pprint/pprint req)
          uri (:uri req)
          method (:request-method req)]
       (handler req))))

(defapi app
  (context "/api" []
           :middleware [wrap-abac-authorization]
           users/users-routes
           (GET "/issues" []
                (resp/ok {:issues (issues/get-issues)}))
           (GET "/organizations" []
                (resp/ok {:organizations (organizations/get-organizations)}))))

(defstate server
  :start (httpkit/run-server #'app {:port 5000})
  :stop (server))

(defn -main [& args]
  (mount.core/start))

(ns authz-test.core
  (:gen-class)
  (:require [authz-test.api.authorization :as authorization]
            [authz-test.api.issues :as issues]
            [authz-test.api.organizations :as organizations]
            [authz-test.api.users :as users]
            [compojure.api.sweet :as sweet :refer [context defapi GET]]
            [mount.core :refer [defstate]]
            [org.httpkit.server :as httpkit]
            [ring.util.http-response :as resp]
            [schema.core :as sc]))

(defapi app
  (context "/api" []
           :middleware [authorization/wrap-abac-authorization]
           users/users-routes
           (context "/issues" []
                    (GET "/" []
                         (resp/ok {:issues (issues/get-issues)}))
                    (GET "/:issue-id" [issue-id]
                         :path-params [issue-id :- Long]
                         :body-params [user-id :- Long
                                       issue-id :- Long]
                         (resp/ok {:issue (issues/get-issue issue-id)})))
           (GET "/organizations" []
                (resp/ok {:organizations (organizations/get-organizations)}))))

(defstate server
  :start (httpkit/run-server #'app {:port 5000})
  :stop (server))

(defn -main [& args]
  (mount.core/start))

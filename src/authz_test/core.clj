(ns authz-test.core
  (:gen-class)
  (:require [authz-test.api.authentication :as authentication]
            [authz-test.api.authorization :as authorization]
            [authz-test.api.issues :as issues]
            [authz-test.api.organizations :as organizations]
            [authz-test.api.users :as users]
            [compojure.api.sweet :as sweet :refer [context defapi GET]]
            [mount.core :refer [defstate]]
            [org.httpkit.server :as httpkit]
            [ring.util.http-response :as resp]
            [schema.core :as sc]
            [yada.swagger :as swagger :refer [swaggered]]
            [yada.yada :as yada]))

(def routes ["/api"
             (swaggered
              [""
               [["/login" authentication/login-resource]
                ["/logout" authentication/logout-resource]
                ["/users" [["" users/users-resource]
                           [["/" :user-id] users/user-resource]]]
                ["/issues" [["" issues/issues-resource]
                            [["/" :issue-id] issues/issue-resource]]]
                ["/organizations" [["" organizations/organizations-resource]
                                   [["/" :organization-id]
                                    organizations/organization-resource]]]]]
              {:info {:title "Authorization test"
                      :version "0.1"
                      :description "Prototyping ABAC authorization with yada"}
               :basePath "/api"})])

(defn start []
  (yada/listener routes {:port 5000}))

(defstate server
  :start (start)
  :stop ((:close server)))

(defn -main [& args]
  (mount.core/start))

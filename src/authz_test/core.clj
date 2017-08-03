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
            [schema.core :as sc]
            [yada.yada :as yada]))

(def routes ["/"
             [
              ["users" [
                        ["" users/users-resource]
                        [["/" :user-id] users/user-resource]]]
              ["issues" [
                         ["" issues/issues-resource]
                         [["/" :issue-id] issues/issue-resource]]]
              ["organizations" [
                                ["" organizations/organizations-resource]
                                [["/" :organization-id]
                                 organizations/organization-resource]]]
              ["hello" (yada/as-resource "Hello, World!")]
              [true (yada/as-resource nil)]]])

(defn start []
  (yada/listener routes {:port 5000}))

(defstate server
  :start (start)
  :stop ((:close server)))

(defn -main [& args]
  (mount.core/start))

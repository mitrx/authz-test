(ns authz-test.api.organizations
  (:require [authz-test.db.organizations :refer [organizations]]))

(defn get-organizations []
  organizations)

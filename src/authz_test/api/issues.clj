(ns authz-test.api.issues
  (:require [authz-test.db.issues :refer [issues]]))

(defn get-issues []
  issues)

(ns authz-test.api.issues
  (:require [authz-test.db.issues :as issues :refer [issues]]))

(defn get-issues []
  issues)

(defn get-issue [issue-id]
  (issues/get-issue issue-id))

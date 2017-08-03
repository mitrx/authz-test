(ns authz-test.api.authorization
  (:require [authz-test.db.issues :as issues]
            [authz-test.db.organizations :as organizations]
            [cheshire.core :as json]
            [ring.util.http-response :as resp]))

(defn eval-predicate [predicate props]
  (cond
    (vector? predicate)
    (case (first predicate)
      :and (every? #(eval-predicate % props) (rest predicate))
      :or (some #(eval-predicate % props) (rest predicate))
      :not (not (eval-predicate (second predicate) props)))
    (keyword? predicate)
    (predicate props)))

(defmethod yada.authorization/validate
  :authz-test/abac
  [ctx credentials authorization]
  (let [predicate (-> authorization :predicate)
        props (:properties ctx)]
    (when (eval-predicate predicate props)
      ctx)))

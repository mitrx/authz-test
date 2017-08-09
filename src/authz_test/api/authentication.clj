(ns authz-test.api.authentication
  (:require [authz-test.api.users :as users]
            [authz-test.db.sessions :as sessions]
            [buddy.core.codecs :as codecs]
            [buddy.core.hash :as hashes]
            [buddy.core.nonce :as nonce]
            [buddy.sign.jwt :as jwt]
            [clj-time.coerce :as c]
            [clj-time.local :as lt]
            [hiccup.page :refer [html5]]
            [yada.yada :as yada]))

(defmethod yada.security/verify
  :authz-test/cookie-auth
  [{:keys [cookies]} _]
  (when cookies
    (sessions/get-session-content (get cookies "session"))))

(defn generate-session-key [user]
  (let [email (-> user :email)
        timestamp (-> (lt/local-now) c/to-long str)
        nonce (nonce/random-nonce 16)]
    (-> (str email timestamp nonce)
        hashes/sha256
        codecs/bytes->hex)))

(defn terminate-session [session-key]
  (sessions/remove-session session-key))

(defn start-session [user]
  (sessions/remove-user-sessions (:user-id user))
  (let [session-key (generate-session-key user)]
    (sessions/add-session session-key user)
    session-key))

;; (def secret "ieXai7aiWafeSh6oowow")

(def login-resource
  (yada/resource
   {:id :authz-test.resources/login
    :methods
    {:get
     {:produces "text/html"
      :response
      (fn [ctx]
        (html5
         [:form {:method :post}
          [:p
           [:label {:for "email"} "Email "]
           [:input {:type :text :id "email" :name "email"}]]
          [:p
           [:label {:for "password"} "Password "]
           [:input {:type :password :id "password" :name "password"}]]
          [:p
           [:input {:type :submit}]]]))}
     :post
     {:consumes "application/x-www-form-urlencoded"
      :produces "application/json"
      :parameters
      {:form {:email String
              :password String}}
      :response
      (fn [ctx]
        (let [{:keys [email password]} (-> ctx :parameters :form)
              user (users/get-user-by-email email)]
          (merge
           (:response ctx)
           (if-not (and user (= password (:password user)))
             {:description "login-failed"
              :status 401}
             (let [session-key (start-session user)]
               {:status 200
                :cookies
                {"session"
                 {:value session-key
                  ;; TODO: set :secure to "true" in production
                  ;; environment
                  :secure false}}})))))}}}))

(def logout-resource
  (yada/resource
   {:id :authz-test.resources/logout
    :parameters {:cookie {:session String}}
    :methods
    {:delete
     {:consumes "application/json"
      :produces "application/json"
      :response
      (fn [ctx]
        (terminate-session (-> ctx :authentication (get "default"))))}}
    :access-control {:scheme :authz-test/cookie-auth}}))

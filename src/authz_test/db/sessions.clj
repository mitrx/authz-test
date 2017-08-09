(ns authz-test.db.sessions)

(def sessions (atom {}))

(defn get-session-content [session-key]
  (get @sessions session-key))

(defn add-session [session-key content]
  (swap! sessions #(assoc % session-key content)))

(defn remove-session [session-key]
  (swap! sessions #(dissoc % session-key)))

(defn remove-user-sessions [user-id]
  (swap!
   sessions
   (fn [sessions]
     (into {} (filter
               #(not= (:user-id (second %)) user-id))
               sessions))))

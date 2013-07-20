(ns hackedio-overtone.hue
  (:require [clj-http.client :as http]
            [cheshire.core :as ch]))

(def endpoint "http://192.168.1.18")
(def username "nicobalestra")
(def url (str endpoint "/api/" username))

(def red 0)
(def green 25500)
(def blue 46920)

(defn login []
  (let [login  (:body (http/post (str endpoint "/api") { :body (ch/generate-string {:devicetype "test user" :username username})}))]
    (println login)
    (println "Now press the button on the bulb and run me again"))
  )

(str url "/lights")

(defn hue-get-lights []
  (ch/parse-string (:body  (http/get (str url "/lights") ))))

(defn hue-get-light [n]
  (let [lights (hue-get-lights)
        k (keys lights)]
    (get lights (str  n))
    )
  )

(defn hue-get-state [n]
  (println (str url "/lights/" n "/state"))
  (ch/parse-string
   (:body (http/get
            (str url "/lights/" n "/state"))))
  )

(defn hue-set-state
  "Set the status of a bulb or, if no bulb number is specified, set the state of all bulbs"
  ([{:keys [on sat bri hue alert] :or {on true sat 255 bri 255 hue 65535} :as settings}]
     (let [lights (hue-get-lights)
           k (keys lights)]
       (doseq [light-n k]
         (hue-set-state light-n settings)))
     )
  ([n {:keys [on sat bri hue] :or {on true sat 255 bri 255 hue 65535} :as settings}]
     (println "Setting state of bulb " n " via url " (str url "/lights/" n "/state"))
     (println (http/put (str url "/lights/" n "/state") {:body (ch/generate-string settings)} ))))

(defn hue-start-loop []
  (hue-set-state {:on true :effect "colorloop" :transitiontime 1}))

(defn hue-stop-loop []
  (hue-set-state {:effect "none"}))

(defn hue-turn-them-off [& n]
  (let [bulb (first n)]
    (println "Setting of bulb " bulb)
    (if n
      (hue-set-state bulb {:on false})
      (hue-set-state {:on false})))
  )

(defn hue-turn-them-on [& n]
  (let [bulb (first n)]
    (println "Setting on bulb " bulb)
    (if n
      (hue-set-state bulb {:on true })
      (hue-set-state {:on true})))
  )

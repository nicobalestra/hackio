(ns hackedio.nk
  (:use [overtone.live]
        [hackedio.hue])
  )

(def nk (midi-in "nanoKONTROL2"))

(defn- is-button-on [event]
  (> (:velocity event) 0))

(defn- is-button-off [event]
  (= (:velocity event) 0))


(defn right-time [t1 t2]
  (> (- t2 t1) 400))

(defmulti handle-nk :note)

(defmethod handle-nk 0 [event]
  (println "Handling note 0"))

(defmethod handle-nk 46 [event]
  "Handle the CYCLE button"
  (let [status (:velocity event)]
    (if (= status 0)
      (hue-stop-loop)
      (hue-start-loop))))

(defmethod handle-nk 48 [event]
  "Handle first MUTE button"
  (if (is-button-on event)
    (hue-turn-them-on 1)
    (hue-turn-them-off 1)
    )
  )

(defmethod handle-nk 49 [event]
  "Handle second MUTE button"
  (if (is-button-on event)
    (hue-turn-them-on 2)
    (hue-turn-them-off 2)
    )
  )

(defmethod handle-nk 50 [event]
  "Handle third MUTE button"
  (if (is-button-on event)
    (hue-turn-them-on 3)
    (hue-turn-them-off 3)
    ))


(def last-slider1-ts (atom nil))

(defmethod handle-nk 0 [event]
  "Handle first SLIDER"
  (let [ts (:timestamp event)
        vel (:velocity event)]
    (when (or (not last-slider1-ts) (right-time ts last-slider1-ts))

      ))
  )

(defmethod handle-nk :default [event]
  (println "No need to handle note " (:note event)))

(defn handle-nk-events [event]
  (let [n (:note event)])
  (handle-nk event)
  (println "Do nothing for now: " event))

(midi-handle-events nk #'handle-nk-events)

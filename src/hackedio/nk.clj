(ns hackedio.nk
  (:use [overtone.live]
        [hackedio.hue])
  )
(connected-midi-devices)

(def nk (midi-in "nanoKONTROL2"))

(defn- is-button-on [event]
  (> (:velocity event) 0))

(defn- is-button-off [event]
  (= (:velocity event) 0))


(defn can-switch [t1 t2]
  (> (- t1 t2) 400))

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
    (hue-turn-them-off 3)))


(def sliders-status (atom {1 {:ts nil :on (get (hue-get-state 1) "on")} 2 {:ts nil} 3 {:ts nil}}))

(defn change-hue [n event]
  (let [ts (:timestamp event)
        col (:velocity event)
        hue (int (* col (/ 65535 127)))
        slider (get @sliders-status n)
        is-on (:on slider)]
    (when (or (nil? (:ts slider)) (can-switch ts (:ts slider)))
      (if (= 0 hue)
        (do
          (hue-turn-them-off n)
          (swap! sliders-status update-in [n] merge {:on false}))
        (do
          (when (not is-on)
            (swap! sliders-status update-in [n] merge {:on true}))
          (hue-set-state n {:on true :hue hue}))
        )
      (swap! sliders-status update-in [n] merge {:ts ts}))))


(defn change-brightness [n event]
  (let [ts (:timestamp event)
        col (:velocity event)
        bri (int (* col 2))
        slider (get @sliders-status n)
        is-on (:on slider)]
    (when (or (nil? (:ts slider)) (can-switch ts (:ts slider)))
      (if (= 0 bri) ;turn off the light
        (do
          (hue-turn-them-off n)
          (swap! sliders-status update-in [n] merge {:on false}))
        (do
          (when (not is-on)
            (swap! sliders-status update-in [n] merge {:on true}))
          (hue-set-state n {:on true :bri bri})))
      (swap! sliders-status update-in [n] merge {:ts ts}))))


(defmethod handle-nk 0 [event]
  "Handle first SLIDER"
  (change-hue 1 event))


(defmethod handle-nk 1 [event]
  "Handle second SLIDER"
  (change-hue 2 event)
  )

(defmethod handle-nk 2 [event]
  "Handle third SLIDER"
  (change-hue 3 event)
  )


(defmethod handle-nk 16 [event]
  "Handle first KNOB"
  (change-brightness 1 event)
  )


(defmethod handle-nk 17 [event]
  "Handle second KNOB"
  (change-brightness 2 event)
  )

(defmethod handle-nk 18 [event]
  "Handle third KNOB"
  (change-brightness 3 event)
  )


(defn set-solo [n event]
  (let [all-bulbs #{1 2 3}
        bulbs (disj all-bulbs n)
        val (:velocity event)]
    (if (> val 0)
      (doseq [bulb bulbs]
        (hue-turn-them-off bulb))
      (hue-turn-them-on))
    )
  )

(defmethod handle-nk 32 [event]
  "Handle first SOLO button"
  (set-solo 1 event)
  )

(defmethod handle-nk 33 [event]
  "Handle second SOLO button"
  (set-solo 2 event)
  )

(defmethod handle-nk 34 [event]
  "Handle third SOLO button"
  (set-solo 3 event)
  )


(defmethod handle-nk :default [event]
  (println "No need to handle note " (:note event)))



(defn handle-nk-events [event]
  (let [n (:note event)])
  (handle-nk event)
  (println event))

(midi-handle-events nk #'handle-nk-events)

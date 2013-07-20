(ns hackedio.sowhat
  (:use overtone.live
        overtone.inst.synth))

(definst evans [note 70
                gate 1
                vel 60
                decay 0.2
                release 0.8
                hard 0.8
                velhard 0.8
                muffle 0.8
                velmuff 0.8
                velcurve 0.8
                stereo 0.9
                tune 0.5
                random 0.1
                stretch 0.1
                sustain 0.1]
  (let [snd (mda-piano {:freq (midicps note)
                        :gate gate
                        :vel vel
                        :decay decay
                        :release release
                        :hard hard
                        :velhard velhard
                        :muffle muffle
                        :velmuff velmuff
                        :velcurve velcurve
                        :stereo stereo
                        :tune tune
                        :random random
                        :stretch stretch
                        :sustain sustain})]
    (detect-silence snd 0.005 :action FREE)
    (* 1 snd)))

(definst miles
  [note  {:default 60  :min 10   :max 120  :step 1}
   amp   {:default 0.8 :min 0.01 :max 0.99 :step 0.01}
   dur   {:default 2   :min 0.1  :max 4    :step 0.1}
   decay {:default 30  :min 1    :max 50   :step 1}
   coef  {:default 0.3 :min 0.01 :max 2    :step 0.01}]
  (let [freq (midicps note)
        noize (* 0.8 (white-noise))
        dly (/ 1.0 freq)
        plk   (pluck noize 1 (/ 1.0 freq) dly
                     decay
                     coef)
        dist (distort plk)
        filt (rlpf dist (* 12 freq) 0.6)
        clp (clip2 filt 0.8)
        reverb (free-verb clp 0.4 0.8 0.2)]
    (* amp (env-gen (perc 0.0001 dur) :action FREE) reverb)))
(def section-a-phrase-1 {:man #'evans :phrase [[0 1 2/3 :d3]
                                               [0 2 0/3 :a3]
                                               [0 2 2/3 :b3]
                                               [0 3 0/3 :c4]
                                               [0 3 2/3 :d4]
                                               [0 4 0/3 :e4]
                                               [0 4 2/3 :c4]
                                               [1 1 0/3 :d4]
                                               [1 3 0/3 :e3 :a4 :d4 :g4 :b4]
                                               [1 4 2/3 :d3 :g4 :c4 :f4 :a4]]})

(def section-a-phrase-2 {:man #'evans :phrase [[0 1 2/3 :d3]
                                               [0 2 0/3 :a3]
                                               [0 2 2/3 :b3]
                                               [0 3 0/3 :c4]
                                               [0 3 2/3 :d4]
                                               [0 4 0/3 :e4]
                                               [0 4 2/3 :c4]
                                               [1 1 0/3 :d4]
                                               [1 1 2/3 :a3]
                                               [1 3 0/3 :e3 :a4 :d4 :g4 :b4]
                                               [1 4 2/3 :d3 :g4 :c4 :f4 :a4]]})

(def section-a-phrase-3 {:man #'evans :phrase [[0 1 2/3 :e4]
                                               [0 3 0/3 :e4]
                                               [0 4 0/3 :e4]
                                               [1 1 0/3 :d4]
                                               [1 1 2/3 :a3]
                                               [1 3 0/3 :e3 :a4 :d4 :g4 :b4]
                                               [1 4 2/3 :d3 :g4 :c4 :f4 :a4]
                                               ]})

(def section-b-phrase-1 {:man #'evans :phrase [[0 1 2/3 :d#3]
                                               [0 2 0/3 :a#3]
                                               [0 2 2/3 :b#4]
                                               [0 3 0/3 :c#4]
                                               [0 3 2/3 :d#4]
                                               [0 4 0/3 :e#4]
                                               [0 4 2/3 :c#4]
                                               [1 1 0/3 :d#4]
                                               [1 3 0/3 :e#3 :a#4 :d#4 :g#4 :b#5]
                                               [1 4 2/3 :d#3 :g#4 :c#4 :f#4 :a#4]]})

(def section-b-phrase-2 {:man #'evans :phrase [[0 1 2/3 :d#3]
                                               [0 2 0/3 :a#3]
                                               [0 2 2/3 :b#4]
                                               [0 3 0/3 :c#4]
                                               [0 3 2/3 :d#4]
                                               [0 4 0/3 :e#4]
                                               [0 4 2/3 :c#4]
                                               [1 1 0/3 :d#4]
                                               [1 1 2/3 :a#3]
                                               [1 3 0/3 :e#3 :a#4 :d#4 :g#4 :b#5]
                                               [1 4 2/3 :d#3 :g#4 :c#4 :f#4 :a#4]]})

(def section-b-phrase-3 {:man #'evans
                         :phrase [[0 1 2/3 :f4]
                                  [0 3 0/3 :f4]
                                  [0 4 0/3 :f4]
                                  [1 1 0/3 :d#4]
                                  [1 1 2/3 :a#3]
                                  [1 3 0/3 :f3 :a#4 :d#4 :g#4 :c4]
                                  [1 4 2/3 :d#3 :g#4 :c#4 :f#4 :a#4]
                                  ]})

(def soloist {:man miles :phrase [[0 1 0 :d4]
                                  [0 2 0 :d4]
                                  [0 3 0 :d4]
                                  [0 4 0 :d4]
                                  [1 1 0 :d4]
                                  [1 2 0 :d4]
                                  [1 3 0 :d4]
                                  [1 4 0 :d4]]}  )

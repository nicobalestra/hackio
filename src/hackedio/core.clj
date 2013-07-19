(ns hackedio.core
  (:use overtone.live
        overtone.inst.piano
        overtone.inst.sampled-piano))

(demo (piano))

(demo 5 (lpf (mix (sin-osc [52 (line 900 1600 5) 101 100.5]))
             (lin-lin (lf-tri (line 2 20 5)) -1 1 400 4000)))

(overtone.inst.sampled-piano/sampled-piano (nth (scale :b4 :aeolian)
                                                0))

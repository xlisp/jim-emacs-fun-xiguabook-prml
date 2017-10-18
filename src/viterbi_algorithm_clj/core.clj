(ns viterbi-algorithm-clj.core
  (:use [clojure.pprint]))

(defstruct hmm :n :m :init-probs :emission-probs :state-transitions)

(defn make-hmm [{:keys [states, obs, init-probs, emission-probs, state-transitions]}]
  (struct-map hmm
              :n (count states)
              :m (count obs)
              :states states
              :obs obs
              :init-probs init-probs ;; n dim
              :emission-probs emission-probs ;;m x n
              :state-transitions state-transitions))

(defn indexed [s]
  (map vector (iterate inc 0) s))

(defn argmax [coll]
  (loop [s (indexed coll)
         max (first s)]
    (if (empty? s)
      max
      (let [[idx elt] (first s)
            [max-indx max-elt] max]
        (if (> elt max-elt)
          (recur (rest s) (first s))
	  (recur (rest s) max))))))

(defn pprint-hmm [hmm]
  (println "number of states: " (:n hmm) " number of observations:  " (:m hmm))
  (print "init probabilities: ") (pprint (:init-probs hmm))
  (print "emission probs: " ) (pprint (:emission-probs hmm))
  (print "state-transitions: " ) (pprint (:state-transitions hmm)))

(defn init-alphas [hmm obs]
  (map (fn [x]
         (* (aget (:init-probs hmm) x) (aget (:emission-probs hmm) x obs)))
       (range (:n hmm))))

(defn forward [hmm alphas obs]
  (map (fn [j]
         (* (reduce (fn [sum i]
                      (+ sum (* (nth alphas i) (aget (:state-transitions hmm) i j))))
                    0
                    (range (:n hmm)))
            (aget (:emission-probs hmm) j obs))) (range (:n hmm))))

(defn delta-max [hmm deltas obs]
  (map (fn [j]
         (* (apply max (map (fn [i]
                              (* (nth deltas i)
                                 (aget (:state-transitions hmm) i j)))
                            (range (:n hmm))))
            (aget (:emission-probs hmm) j obs)))
       (range (:n hmm))))

(defn backtrack [paths deltas]
  (loop [path (reverse paths)
         term (first (argmax deltas))
         backtrack []]
    (if (empty? path)
      (reverse (conj backtrack term))
      (recur (rest path) (nth (first path) term) (conj backtrack term)))))

(defn update-paths [hmm deltas]
  (map (fn [j]
         (first (argmax (map (fn [i]
                               (* (nth deltas i)
                                  (aget (:state-transitions hmm) i j)))
                             (range (:n hmm))))))
       (range (:n hmm))))

(defn viterbi [hmm observs]
  (loop [obs (rest observs)
         alphas (init-alphas hmm (first observs))
         deltas alphas
         paths []]
    (if (empty? obs)
      [(backtrack paths deltas) (float (reduce + alphas))]
      (recur (rest obs)
             (forward hmm alphas (first obs))
             (delta-max hmm deltas (first obs))
             (conj paths (update-paths hmm deltas))))))

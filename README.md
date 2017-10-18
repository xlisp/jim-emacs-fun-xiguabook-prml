# viterbi-algorithm-clj

西瓜书和PRML学习记录--HMM实现算法,以及数学公式理论到算法的过程记录

## Usage

```clojure

viterbi-algorithm-clj.core=> (Viterbi/main (into-array (list "1" "3")))

States: #, NN, VB,

Observations: I, write, a letter,

Start probability: #: 0.3, NN: 0.4, VB: 0.3,

Transition probability:
 #: {  #: 0.2,   NN: 0.2,   VB: 0.6, }
 NN: {  #: 0.4,   NN: 0.1,   VB: 0.5, }
 VB: {  #: 0.1,   NN: 0.8,   VB: 0.1, }


Emission probability:
 #: {  I: 0.01,   write: 0.02,   a letter: 0.02, }
 NN: {  I: 0.8,   write: 0.01,   a letter: 0.5, }
 VB: {  I: 0.19,   write: 0.97,   a letter: 0.48, }
Viterbi path: [NN, VB, NN, ].
 Probability of the whole system: 0.06208000000000002
nil
viterbi-algorithm-clj.core=>

```

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

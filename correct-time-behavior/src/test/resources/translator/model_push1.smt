; definition std
(declare-fun max_val () Int)
(declare-fun over_max_val () Int)
(declare-fun min_val () Int)
(declare-fun time () Int)
(declare-fun now () Int)
(declare-fun deadline () Int)
(declare-fun timeout () Int)
(declare-fun remaining () Int)

(assert (= max_val 9223372036854775807))
(assert (= over_max_val 9223372036854775808))
(assert (= min_val (- 9223372036854775808)))
(assert (and (>= time 0) (<= time max_val)))

(assert (<= min_val now))
(assert (<= now over_max_val))
(assert (= now time))

(assert (<= min_val deadline))
(assert (<= deadline over_max_val))
(assert (<= min_val timeout))
(assert (<= timeout over_max_val))

(assert (= deadline (+ now timeout)))

(assert (<= now deadline))

(assert (<= min_val remaining))
(assert (<= remaining over_max_val))

(assert (= remaining (ite (<= 0 (- deadline now)) (- deadline now) 0)))
(assert (= now time))
(set-option :opt.timeout 9000)
(check-sat)


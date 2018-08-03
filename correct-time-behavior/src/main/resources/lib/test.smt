(declare-fun max_val () Int)
(declare-fun over_max_val () Int)
(declare-fun min_val () Int)
(declare-fun time () Int)
(declare-fun rateFactor () Int)
(declare-fun rateUnit () Int)
(declare-fun |"events/"| () Int)
(declare-fun calculateRateUnit () Int)
(declare-fun durationFactor () Int)
(declare-fun durationUnit () Int)
(declare-fun toLowerCase () Int)
(assert (= max_val 9223372036854775807))
(assert (= over_max_val 9223372036854775808))
(assert (= min_val (- 100)))
(assert (and (>= time 0) (<= time max_val)))
(assert (<= min_val rateFactor))
(assert (<= rateFactor over_max_val))
(assert (= rateFactor time))
(assert (<= min_val rateUnit))
(assert (<= rateUnit over_max_val))
(assert (<= min_val |"events/"|))
(assert (<= |"events/"| over_max_val))
(assert (= rateUnit (+ |"events/"| calculateRateUnit)))
(assert (<= min_val durationFactor))
(assert (<= durationFactor over_max_val))
(assert (= durationFactor (div (to_int 1.0) time)))
(assert (<= min_val durationUnit))
(assert (<= durationUnit over_max_val))
(assert (= durationUnit toLowerCase))
(minimize durationFactor)
(set-option :opt.timeout 9000)
(check-sat)
(get-model)
package android.example.tiptime

abstract class Service(cost: Float) {
    private val cost = cost.toDouble()
    abstract val zeroStar: Double
    abstract val oneStar: Double
    abstract val twoStar: Double
    abstract val threeStar: Double
    abstract val fourStar: Double
    abstract val fiveStar: Double

    open fun calculateTip(rating: Double): Double {
        return when(rating) {
            0.0 -> cost * zeroStar
            1.0 -> cost * oneStar
            2.0 -> cost * twoStar
            3.0 -> cost * threeStar
            4.0 -> cost * fourStar
            5.0 -> cost * fiveStar
            else -> cost
        }
    }

    fun displayPercent(rating: Double): Double {
        return when(rating) {
            0.0 -> zeroStar
            1.0 -> oneStar
            2.0 -> twoStar
            3.0 -> threeStar
            4.0 -> fourStar
            5.0 -> fiveStar
            else -> 0.0
        }
    }
}

class Restaurant(cost: Float): Service(cost) {
    override val zeroStar = 0.11
    override val oneStar = 0.13
    override val twoStar = 0.15
    override val threeStar = 0.18
    override val fourStar = 0.20
    override val fiveStar = 0.22

}
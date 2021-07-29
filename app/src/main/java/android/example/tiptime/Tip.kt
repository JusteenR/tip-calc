package android.example.tiptime

class Tip(amount : Double = 0.0, percent: Double = 0.0, total: Double = 0.0, people: Int = 1, roundedTip: Double = 0.0) {
    var amount = amount
    var percent = percent
    var total = total
    var roundedTip = roundedTip
    var people = people

    fun amountChange(amount: Double){
        this.amount = amount
        this.percent = this.amount/this.total
    }

    fun percentChange(percent: Double){
        this.percent = percent
        this.amount = this.total*this.percent
    }

    fun finalCost(): Double {
        return (this.total + this.amount)/this.people
    }

    fun finalCostRounded(): Double {
        this.roundedTip = kotlin.math.ceil(this.amount)
        return (this.total + this.roundedTip)/this.people
    }
}
package android.example.tiptime

import android.content.Context
import android.example.tiptime.databinding.ActivityMainBinding
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var tipObj = Tip()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set button listener
        //binding.calculateButton.setOnClickListener { calculateTip() }
        binding.ratingBar.onRatingBarChangeListener = listener
        binding.costOfServiceEditText.addTextChangedListener(textListener)
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }

        // Set button listener for changing tip amount
        binding.tipUpArrow.setOnClickListener { view ->
            customTipAmount(view)
        }
        binding.tipDownArrow.setOnClickListener { view ->
            customTipAmount(view)
        }

        binding.percentUpArrow.setOnClickListener { view ->
            customTipPercent(view)
        }
        binding.percentDownArrow.setOnClickListener { view ->
            customTipPercent(view)
        }

        // Listeners for amount of people
        binding.peopleNumUp.setOnClickListener { view ->
            splitCost(view)
        }
        binding.peopleNumDown.setOnClickListener { view ->
            splitCost(view)
        }

        binding.roundUpSwitch.setOnClickListener { roundTip() }

    }

    private val textListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val rating = binding.ratingBar.rating
            val ratingDouble = rating.toDouble()
            calculateTip(ratingDouble)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val listener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
        val ratingDouble = rating.toDouble()
        calculateTip(ratingDouble)
    }

    private fun splitCost(view: View){
        if (!checkCost()){
            return
        }
        if (view.id == binding.peopleNumUp.id){
            tipObj.people += 1
        } else if (view.id == binding.peopleNumDown.id){
            if (tipObj.people > 1){
                tipObj.people -= 1
            }
        }

        roundTip()
    }

    private fun customTipAmount(view: View){
        if (!checkCost()){
            return
        }
        var customTipValue = tipObj.amount
        if (view.id == binding.tipUpArrow.id){
            customTipValue += 0.10
        } else if (view.id == binding.tipDownArrow.id){
            customTipValue -= 0.10
        }
        tipObj.amountChange(customTipValue)

        roundTip()
    }

    private fun customTipPercent(view: View){
        if (!checkCost()){
            return
        }
        var customTipValue = tipObj.percent
        if (view.id == binding.percentUpArrow.id){
            customTipValue += 0.01
        } else if (view.id == binding.percentDownArrow.id){
            customTipValue -= 0.01
        }
        tipObj.percentChange(customTipValue)

        roundTip()
    }


    private fun calculateTip(rating: Double){
        if (!checkCost()){
            return
        }
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDouble()

        val industry = Restaurant(cost.toFloat())
        val tip = industry.calculateTip(rating)

        tipObj = Tip(tip, industry.displayPercent(rating), cost)

        roundTip()
    }


    private fun displayTip(tip : Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.customTip.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun displayPeople() {
        val people = tipObj.people
        binding.peopleNum.text = "People: $people"
    }

    private fun displayPercentage(percent : Double) {
        val formattedPercent = NumberFormat.getPercentInstance().format(percent)
        binding.percentValue.text = "Percent: $formattedPercent"
    }

    private fun displayTotal() {
        val formattedTotal = NumberFormat.getCurrencyInstance().format(tipObj.finalCost())
        binding.perPerson.text = "Total/person: $formattedTotal"
    }

    private fun displayRoundedTotal() {
        val formattedTotal = NumberFormat.getCurrencyInstance().format(tipObj.finalCostRounded())
        binding.perPerson.text = "Total/person: $formattedTotal"
    }

    private fun displayLumpTotal() {
        val formattedLumpTotal = NumberFormat.getCurrencyInstance().format(tipObj.finalCost()*tipObj.people)
        binding.lumpTotal.text = "Total: $formattedLumpTotal"
    }

    private fun displayLumpTotalRounded() {
        val formattedLumpTotal = NumberFormat.getCurrencyInstance().format(tipObj.finalCostRounded()*tipObj.people)
        binding.lumpTotal.text = "Total: $formattedLumpTotal"
    }


    private fun roundTip(){
        if (!checkCost()){
            return
        }
        var tip = tipObj.amount

        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tipObj.amount)
        }

        displayTip(tip)
        displayPercentage(tipObj.percent)
        if (binding.roundUpSwitch.isChecked) {
            displayRoundedTotal()
            displayLumpTotalRounded()
        } else {
            displayTotal()
            displayLumpTotal()
        }
        displayPeople()
    }

    private fun checkCost(): Boolean{
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null || cost == 0.0){
            displayTip(0.0)
            displayPercentage(0.0)
            return false
        }
        return true
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
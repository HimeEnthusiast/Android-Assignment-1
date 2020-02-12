package ca.sheridancollege.piantoni_roberts_a1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun clearFields(amountField: EditText, tipField: EditText){
        amountField.setText("")
        tipField.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//    Labels
        val tipIs = findViewById<TextView>(R.id.tipIs)
        val totalIs = findViewById<TextView>(R.id.totalIs)
        val perPerson = findViewById<TextView>(R.id.totalPerPerson)
        val perPersonTitle = findViewById<TextView>(R.id.perPersonTitle)

//    Buttons
        val calculate = findViewById<Button>(R.id.calculateButton)
        val clear = findViewById<Button>(R.id.clearButton)

//    Spinners
        val chooseTip = findViewById<Spinner>(R.id.chooseTipPercent)
        val peopleNum = findViewById<Spinner>(R.id.setPeople)

//    Text Edits
        val setAmount = findViewById<EditText>(R.id.setAmount)
        val setTip = findViewById<EditText>(R.id.setTipPercent)

//    Arrays
        val people = resources.getStringArray(R.array.people_amount)
        val tip = resources.getStringArray(R.array.tip_amount)

        setSupportActionBar(findViewById(R.id.toolbar))

//        Fill Spinners
        if (peopleNum != null) {
            val peopleAdapter = ArrayAdapter( this, android.R.layout.simple_spinner_item, people)
            peopleNum.adapter = peopleAdapter
        }

        if (chooseTip != null) {
            val tipAdapter = ArrayAdapter( this, android.R.layout.simple_spinner_item, tip)
            chooseTip.adapter = tipAdapter
        }

        // Event Handlers

//      Disabling TextEdits
        chooseTip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setTip.isEnabled = chooseTip.selectedItem.toString().equals("Other")
            }
        }

        peopleNum.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                perPerson.isVisible = !setPeople.selectedItem.toString().equals("1")
                perPersonTitle.isVisible = !setPeople.selectedItem.toString().equals("1")
            }
        }

        clear.setOnClickListener{
            clearFields(setAmount, setTip)
        }

        fun calculateResult(
            setAmount: EditText, chooseTip : Spinner, setTip: EditText, peopleNum: Spinner
        ){
            if (chooseTip.selectedItem == "Other") {
                var tip = setTip.text.toString().toDouble()
                var tipPercent : Double = tip / 100
                var amount = setAmount.text.toString().toFloat()
                var tipAmount = amount * tipPercent
                var people = peopleNum.selectedItem.toString().toDouble()

                var calculate = (amount + tipAmount) / people
                totalIs.text = String.format("%.2f", amount + tipAmount)
                tipIs.text = String.format("%.2f", tipAmount)
                perPerson.text = String.format("%.2f", calculate)
            }else{
                var amount = setAmount.text.toString().toFloat()
                var people = peopleNum.selectedItem.toString().toDouble()

                val strPercent : String = chooseTip.selectedItem.toString()
                val strPercentParts = strPercent.split("%")
                var tipPercent = (strPercentParts[0].toFloat() / 100).toDouble()
                var tipAmount = amount * tipPercent
                var calculate = (amount + tipAmount) / people

                totalIs.text = String.format("%.2f", amount + tipAmount)
                tipIs.text = String.format("%.2f", tipAmount)
                perPerson.text = String.format("%.2f", calculate)
            }
        }

        calculate.setOnClickListener {
            var toastMsg = ""
            if(setAmount.text.toString() == ""){
                toastMsg += "Require bill amount"
                val errorToast = Toast.makeText(applicationContext, toastMsg, Toast.LENGTH_SHORT)
                errorToast.show()
            }else{
                if(chooseTip.selectedItem.toString() == "Other" &&
                    setTip.text.toString() == ""){
                    toastMsg += "Require a tip amount"
                    val errorToast = Toast.makeText(applicationContext, toastMsg, Toast.LENGTH_SHORT)
                    errorToast.show()
                }else{
                    calculateResult(setAmount, chooseTip, setTip, peopleNum)
                }
            }
        }

    }

//    Validating

//    This adds the about button to the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

//    Creating a close button for the alert dialog
    val closeButton = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "Close", Toast.LENGTH_SHORT).show()
    }

//    Actions for the menu
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about_button -> {
            val aboutDialog = AlertDialog.Builder(this)
                aboutDialog.setTitle("About")
                aboutDialog.setMessage("This is our tip calculator! <3")
                aboutDialog.setNegativeButton("Close", closeButton)
                aboutDialog.show()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}

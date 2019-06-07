package com.harsh.textwatchersample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    var beforeText : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<AppCompatEditText>(R.id.editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
                beforeText = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

                if (p0 == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = getNumberOfDigits(beforeText.substring(initialCursorPosition,
                        beforeText.length))
                val newAmount = formatAmount(p0.toString())
                editText.removeTextChangedListener(this)
                editText.setText(newAmount)
                //set new cursor position
                editText.setSelection(getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                editText.addTextChangedListener(this)



            }

        })

    }

    fun formatAmount(@NonNull amount: String): String {

        val result = removeNonNumeric(amount)
        val amt = if (!TextUtils.isEmpty(result) && TextUtils.isDigitsOnly(result)) result.toLong() else 0
        val formatter = NumberFormat.getNumberInstance()
        return getString(R.string.rupees).plus(formatter.format(amt))
    }

    private fun removeNonNumeric(@NonNull numberString: String) : String {
        var numbers = ""
        for (i in numberString){
            if (i.isDigit())
                numbers += i
        }
        return numbers
    }

    private fun getNewCursorPosition(digitCountToRightOfCursor : Int, numberString : String) : Int{
        var position = 0
        var c = digitCountToRightOfCursor
        for (i in numberString.reversed()) {
            if (c == 0)
                break

            if (i.isDigit())
                c --
            position ++


        }
        return numberString.length - position
    }

    private fun getNumberOfDigits(@NonNull text : String) : Int{
        var count = 0
        for (i in text)
            if (i.isDigit())
                count++
        return count
    }

}

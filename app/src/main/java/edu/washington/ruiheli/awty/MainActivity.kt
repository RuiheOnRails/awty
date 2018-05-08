package edu.washington.ruiheli.awty

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBtn = findViewById<Button>(R.id.btnAction)
        val phoneNumEditText = findViewById<EditText>(R.id.phoneNumEditText)
        val minEditText = findViewById<EditText>(R.id.intervalEditText)
        val msgEditText = findViewById<EditText>(R.id.messageEditText)

        actionBtn.setOnClickListener {

            var toastMsg = ""
            var hasError = false

            val phoneNum: String = phoneNumEditText.text.toString()
            var minInterval: Int = 0
            val msg: String = msgEditText.text.toString()



            if(phoneNum.isNullOrEmpty()){
                toastMsg += "Phone Number can not be empty\n"
                hasError = true
            }

            try{
                minInterval = minEditText.text.toString().toInt()
                if(minInterval < 1) {
                    toastMsg += "Interval can not be less than 1\n"
                    hasError = true
                }
            }catch (e: NumberFormatException){
                hasError = true
                toastMsg += "Interval can not be empty\n"
            }


            if(msg.isNullOrEmpty()){
                toastMsg += "Message can not be empty"
                hasError = true
            }

            if (hasError){
                val toast = Toast.makeText(this, toastMsg, Toast.LENGTH_LONG)
                toast.show()
            }else {
                val btnText = actionBtn.text.toString().toUpperCase()
                val task = object: CountDownTimer(60000 * minInterval.toLong(), 60000){
                    override fun onFinish() {
                        val toast = Toast.makeText(this@MainActivity, "Nagging stopped", Toast.LENGTH_SHORT)
                        toast.show()
                        actionBtn.text = "START"
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        val toast = Toast.makeText(this@MainActivity, "Texting ${phoneNum}: ${msg}", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                if(btnText == "START"){
                    actionBtn.text = "STOP"
                    task.cancel()
                    task.start()
                }else {
                    task.cancel()
                    actionBtn.text = "START"
                }
            }
        }
    }
}



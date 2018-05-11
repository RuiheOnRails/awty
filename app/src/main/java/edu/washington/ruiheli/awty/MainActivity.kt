package edu.washington.ruiheli.awty

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.telephony.SmsManager
import android.util.Log
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
        var taskRef: CountDownTimer? = null
        actionBtn.setOnClickListener {

            var toastMsg = ""
            var hasError = false

            val phoneNum: String = phoneNumEditText.text.toString()
            var minInterval = 0
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
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 0)
                val permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                val btnText = actionBtn.text.toString().toUpperCase()
                val task = object: CountDownTimer(60000 * minInterval.toLong(), 60000){
                    override fun onFinish() {
                        val toast = Toast.makeText(this@MainActivity, "Texting ${phoneNum}: ${msg}", Toast.LENGTH_SHORT)
                        toast.show()
                        SmsManager.getDefault().sendTextMessage(phoneNum, null, msg, null, null)
                        this.start()
                    }

                    override fun onTick(millisUntilFinished: Long) {

                    }
                }
                taskRef = task

                if(btnText == "START"){
                    if (permission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 0)
                    }else{
                        actionBtn.text = "STOP"
                        taskRef?.start()
                    }
                }else {
                    Log.i("INSTOP", "in stop")
                    taskRef?.cancel()
                    actionBtn.text = "START"
                    val toast = Toast.makeText(this@MainActivity, "Nagging stopped", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }
}



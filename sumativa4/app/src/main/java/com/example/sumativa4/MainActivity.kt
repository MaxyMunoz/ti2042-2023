package com.example.sumativa4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var messageView: TextView
    private lateinit var CodeView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var offButton: RadioButton
    private lateinit var HumidifierButton: RadioButton
    private lateinit var DehumidifierButton: RadioButton


    private lateinit var mqttClient: MQTTClientHelper
    private val sensor = MQTTClientHelper.SENSOR_TOPIC
    private val Humidifier = MQTTClientHelper.humidifier

    private var humidityValue = 50
    private var deviceStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageView = findViewById(R.id.messageView)
        CodeView = findViewById(R.id.CodeText)
        radioGroup = findViewById(R.id.RadioGroup)
        offButton = findViewById(R.id.offButton)
        HumidifierButton = findViewById(R.id.HumidiferButton)
        DehumidifierButton = findViewById(R.id.DehumidifierButton)
    

        mqttClient = MQTTClientHelper()
        messageView.append("Welcome\n")

        mqttClient.subscribeToTopic(sensor,messageView,CodeView)

       radioGroup.setOnCheckedChangeListener{ _, checkedId ->
           when(checkedId){
               R.id.offButton ->{
                   mqttClient.publishMessage(Humidifier,"OFF")
                   deviceStatus = 0
               }
               R.id.HumidiferButton -> {
                   mqttClient.publishMessage(Humidifier,"HUMIDIFIER")
                   deviceStatus = -1
               }
               R.id.DehumidifierButton ->{
                   mqttClient.publishMessage(Humidifier,"DEHUMIDIFIER")
                   deviceStatus= 1
               }
           }
       }


        GlobalScope.launch(context = Dispatchers.Main) {
            deviceOperation(1000)
        }
    }

    private suspend fun deviceOperation(sleepTime: Long) {
        while (true) {

            humidityValue = humidityValue + deviceStatus * 5

            if (humidityValue > 100) humidityValue = 100

            else if (humidityValue < 0) humidityValue = 0

            val humidityStatus: String
            if (humidityValue < 15) humidityStatus = "RED-"
            else if (humidityValue < 30) humidityStatus = "YELLOW-"
            else if (humidityValue < 60) humidityStatus = "GREEN"
            else if (humidityValue < 75) humidityStatus = "YELLOW+"
            else humidityStatus = "RED+"


            mqttClient.publishMessage(sensor, humidityStatus)
            delay(sleepTime)
        }
    }

}
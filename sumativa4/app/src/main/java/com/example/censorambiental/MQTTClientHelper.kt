package com.example.censorambiental

import android.graphics.Color
import android.widget.TextView
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTTClientHelper {

    private val SERVER_URI = "tcp://mqtt.eclipseprojects.io:1883"
    private val CLIENT_ID = "MaxyMunoz_mqtt"

    companion object{
        const val SENSOR_TOPIC = "sensorTopic"
        const val humidifier = "humidifier"
    }

    private  lateinit var  mqttClient: MqttClient
    init { connectToMqttBroker()}

    private fun connectToMqttBroker(){
        try{
            val persistence = MemoryPersistence()
            mqttClient= MqttClient(SERVER_URI,CLIENT_ID,persistence)
            val options = MqttConnectOptions()
            options.isCleanSession = true
            mqttClient.connect(options)
        } catch (e: MqttException){
            e.printStackTrace()
        }
    }
    fun subscribeToTopic(topic: String, messageView: TextView, CodeView: TextView) {
        try {
            mqttClient.subscribe(topic) { _, message ->
                val payload = String(message.payload)
                CodeView.text=payload
                if (payload == "RED+") {
                    messageView.text = "Alerta: Encienda el Humidificador"
                    messageView.setTextColor(Color.parseColor("#FF0000"))
                    CodeView.setTextColor(Color.parseColor("#FF0000"))
                }
                else if (payload == "YELLOW+"){
                    messageView.text = "Sugiero enceder el Humidificador"
                    messageView.setTextColor(Color.parseColor("#FFFF00"))
                    CodeView.setTextColor(Color.parseColor("#FFFF00"))
                }
                else if (payload == "GREEN"){
                    messageView.text = "Estado normal de humedad"
                    messageView.setTextColor(Color.parseColor("#00FF00"))
                    CodeView.setTextColor(Color.parseColor("#00FF00"))
                }
                else if (payload == "YELLOW-"){
                    messageView.text = "Sugiero enceder el Deshumidificador"
                    messageView.setTextColor(Color.parseColor("#FFFF00"))
                    CodeView.setTextColor(Color.parseColor("#FFFF00"))
                }
                else if (payload == "RED-") {
                    messageView.text = "Alerta: Encienda el Deshumificador"
                    messageView.setTextColor(Color.parseColor("#FF0000"))
                    CodeView.setTextColor(Color.parseColor("#FF0000"))
                }


                //messageView.append("[$topic] $payload\n")
                //println("[$topic] Mensaje recibido: $payload")
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient.publish(topic, mqttMessage)
        }
        catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(){
        mqttClient.disconnect()
    }
}
package com.example.myfirebaseexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myfirebaseexample.api.FirebaseApiAdapter
import com.example.myfirebaseexample.api.response.AnimeResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    // Referenciar campos de las interfaz
    private lateinit var idSpinner: Spinner
    private lateinit var nameField: EditText
    private lateinit var capField: EditText
    private lateinit var linkField: EditText
    private lateinit var imgField: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button

    // Referenciar la API
    private var firebaseApi = FirebaseApiAdapter()

    // Mantener los nombres e IDs de las armas
    private var animeList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idSpinner = findViewById(R.id.idSpinner)
        nameField = findViewById(R.id.nameField)
        capField = findViewById(R.id.capField)
        linkField = findViewById(R.id.linkField)
        imgField = findViewById(R.id.imgField)

        buttonLoad = findViewById(R.id.buttonLoad)
        buttonLoad.setOnClickListener {
            Toast.makeText(this, "Cargando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                getAnimeFromApi()
            }
        }

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            Toast.makeText(this, "Guardando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                sendAnimeToApi()
            }
        }

        runBlocking {
            populateIdSpinner()
        }
    }

    private suspend fun populateIdSpinner() {
        val response = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getAnimes()
        }
        val animes = response.await()
        animes?.forEach { key,value ->
            animeList.add("${key}: ${value.name}")
        }
        val animeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, animeList)
        with(idSpinner) {
            adapter = animeAdapter
            setSelection(0, false)
            gravity = Gravity.CENTER
        }
    }

    private suspend fun getAnimeFromApi() {
        val selectedItem = idSpinner.selectedItem.toString()
        val animeId = selectedItem.subSequence(0, selectedItem.indexOf(":")).toString()
        println("Loading ${animeId}... ")
        val animeResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getAnime(animeId)
        }

        val anime = animeResponse.await()
        nameField.setText(anime?.name)
        capField.setText("${anime?.cap}")
        linkField.setText(anime?.link)
        imgField.setText(anime?.img)
    }

    private suspend fun sendAnimeToApi() {
        val animeName = nameField.text.toString()
        val cap = capField.text.toString().toInt()
        val link = linkField.text.toString()
        val img = linkField.text.toString()
        val anime = AnimeResponse(animeName,cap,link,img)
        val animeResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.setAnime(anime)
        }
        val response = animeResponse.await()
        nameField.setText(anime?.name)
        capField.setText("${anime?.cap}")
        linkField.setText(anime?.link)
        imgField.setText(anime?.img)
    }
}

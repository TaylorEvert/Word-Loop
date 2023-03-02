package com.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.firebaseauthdemo.randomword.RandomWordData
import com.firebaseauthdemo.randomword.RandomWordInterface
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// API URL
const val BASE_URL = "https://api.api-ninjas.com/v1/"
// TAG
const val TAG = "guessAiWordActivity"
class guessAiWordActivity : AppCompatActivity() {

    // Declare activity view variables
    lateinit var view_word: Button
    lateinit var view_strike: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_ai_word)

        getRandomWord()

    }

    // Uses retrofit to create api instance, grabs a random word
    private fun getRandomWord() {

        lifecycleScope.launch {
            // Create retrofit simpleton
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val wordApi = retrofit.create(RandomWordInterface::class.java)
            val response = wordApi.getWord()

            // Grab response from API - return to main activity if an error occurs
            if (response.isSuccessful && response.body() != null) {
                view_word = findViewById(R.id.btn_random_word)
                view_word.text = response.body()!!.word
            } else {
                Toast.makeText(
                    this@guessAiWordActivity,
                    "An error has occurred",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@guessAiWordActivity, MainActivity::class.java))
                finish()
            }

        }
    }
}
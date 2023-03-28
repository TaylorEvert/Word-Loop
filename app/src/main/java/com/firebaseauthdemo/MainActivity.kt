package com.firebaseauthdemo

/**
 * Main Menu - Provides visuals and routes to different functions
 *  - Displays game logo, game title, user information, and user game statistics
 *  - Provides play button - route to start the game at round one
 *  - Provides logout button - logout user and return to login screen
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.firebaseauthdemo.randomword.RandomWordInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // Declare MainActivity Views
    lateinit var email_id: TextView

    // Declare buttons
    lateinit var logout: Button
    lateinit var play: Button
    lateinit var tutorial: Button

    // Declare Database Reference variable
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Enable Play button -
         * Send user to guessAiWord Activity with the random word from API
         */

        // Assign play button id
        play = findViewById(R.id.btn_play)

        // Set listener - send user to guessAiWord Activity - start of game
        play.setOnClickListener {

            // This function calls the RandomWord API - send user to first section of game with the random word
            getRandomWord()
        }

        /**
         * Get user login information -
         * Update screen with user information
         */

        // Grab user email and id from registerActivity
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        // Assign MainActivity View variables
        email_id = findViewById(R.id.text_user_email)
        // Update screen with user information
        email_id.text = "$emailId"

        /**
         *  Setup logout button -
         *  Use onClickListener to get firebase instance and logout user -
         *  Sends user back to Login Activity screen
         */

        // Enable logout button - sends to login screen
        // Assign id to logout variable
        logout = findViewById(R.id.btn_logout)
        // OnClickListener for logout
        logout.setOnClickListener {

            // Use firebase class for logout
            FirebaseAuth.getInstance().signOut()

            // Go to login activity, end main activity
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()

        }


        /**
         *  Setup tutorial button -
         *  Sends user to tutorial page
         *  Allows user to return back to home page
         */

        // Enable tutorial button - send to tutorial screen
        // Assign id ot tutorial button
        tutorial = findViewById(R.id.btn_tutorial)
        // OnClickListener for tutorial
        tutorial.setOnClickListener {
            startActivity(Intent(this@MainActivity, tutorialActivity::class.java))
            finish()
        }

    }

    // This function contacts the random word api - after grabbing a random word, it sends the user to the next activity with the word
    private fun getRandomWord(){
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
                var word = response.body()!!.word
                Toast.makeText(
                    this@MainActivity,
                    word,
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@MainActivity, guessAiWordActivity::class.java)
                // Clear background activities
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("word", word)
                startActivity(intent)
                finish()

            } else {
                Log.d("MainActivity", "getRandomWord: $response")
                Toast.makeText(
                    this@MainActivity,
                    "An error has occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
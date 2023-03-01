package com.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    // Declare MainActivity Views
    lateinit var user_id: TextView
    lateinit var email_id: TextView

    // Declare buttons
    lateinit var logout: Button
    lateinit var play: Button

    // Declare Database Reference variable
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
         * Enable Play button -
         * Send user to guessAiWord Activity
         */

        // Assign play button id
        play = findViewById(R.id.btn_play)

        // Set listener - send user to guessAiWord Activity - start of game
        play.setOnClickListener {

            startActivity(Intent(this@MainActivity, guessAiWordActivity::class.java))
            finish()

        }

        /**
         * Get user login information -
         * Update screen with user information
         */

        // Grab user email and id from registerActivity
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        // Assign MainActivity View variables
        user_id = findViewById(R.id.text_user_id)
        email_id = findViewById(R.id.text_user_email)
        // Update screen with user information
        user_id.text = "ID: $userId"
        email_id.text = "Email: $emailId"

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


    }
}
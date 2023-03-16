package com.firebaseauthdemo

/**
 * This activity is responsible for the final round of the 3 section game loop - AI guesses the user created word
 *  - Displays strikes, incorrect guesses, the user sent word, and the correctly guessed letters
 *  - Correct guesses redirects the user to round 1 for the user to guess the AI word again
 *  - Incorrect guesses redirects the user to the Main Menu and the user wins
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class guessUserWordActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_user_word)

        // User word sent from previous activity - to be guessed by AI
        val sendWord = intent.getStringExtra("word")
        val word = sendWord.toString()
    }
}
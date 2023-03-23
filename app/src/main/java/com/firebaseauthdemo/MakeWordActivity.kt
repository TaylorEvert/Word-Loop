package com.firebaseauthdemo

/**
 * Allows user to create a word, given ten random letters
 *  - This section of the game loop will only be seen upon winning the first round (guessing the word)
 *  - Created words are validated with a dictionary API
 *  - Reset button allows user to redo any letters in play
 *  - Correctly submitting a word allows access to the next round in the game loop (Ai guesses the word)
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.firebaseauthdemo.validword.ValidWordInterface
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MakeWordActivity : AppCompatActivity() {

    // Variables for all tiles to be filled with ten random letters
    lateinit var tile1: Button
    lateinit var tile2: Button
    lateinit var tile3: Button
    lateinit var tile4: Button
    lateinit var tile5: Button
    lateinit var tile6: Button
    lateinit var tile7: Button
    lateinit var tile8: Button
    lateinit var tile9: Button
    lateinit var tile10: Button
    // Variables for button word view and title view
    lateinit var show_word: Button
    lateinit var title: TextView
    // Submit and reset variables
    lateinit var submit: Button
    lateinit var reset: Button
    // Refill tiles button
    lateinit var redo: Button


    // All available letters for user to play - 10 random letters each instance
    var currentHand = mutableListOf<String>()
    // Letters currently in play
    var currentLetters = mutableListOf<String>()
    // Set value decides if word is valid or not - 0 is a placeholder - 1 is a valid word - 2 is an invalid word
    var validWord = 0
    // List of invalid words already made by the user
    var invalidWords = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_word)

        // Populate random 10 letters for user
        fillTiles()

        // Allow user to refill tiles once
        // 0 - refill has been used, 1 - refill is allowed
        var refill = 1
        if (refill == 1) {
            redo = findViewById(R.id.btn_redo_tiles)
            redo.setOnClickListener {
                currentLetters.clear()
                updateButtonView()
                enableButtons()
                fillTiles()
                redo.visibility = View.INVISIBLE
                refill = 0
            }
        }

        // Tile listeners - all tiles will add their respective letter into play and update the change on-screen
        // Upon first press of a tile, said tile will be disabled from play unless user selects the reset button
        tile1 = findViewById(R.id.btn_tile_1)
        tile1.setOnClickListener {
            currentLetters.add(tile1.text.toString())
            tile1.isEnabled = false
            tile1.isClickable = false
            updateButtonView()
        }

        tile2 = findViewById(R.id.btn_tile_2)
        tile2.setOnClickListener {
            currentLetters.add(tile2.text.toString())
            tile2.isEnabled = false
            tile2.isClickable = false
            updateButtonView()
        }

        tile3 = findViewById(R.id.btn_tile_3)
        tile3.setOnClickListener {
            currentLetters.add(tile3.text.toString())
            tile3.isEnabled = false
            tile3.isClickable = false
            updateButtonView()
        }

        tile4 = findViewById(R.id.btn_tile_4)
        tile4.setOnClickListener {
            currentLetters.add(tile4.text.toString())
            tile4.isEnabled = false
            tile4.isClickable = false
            updateButtonView()
        }

        tile5 = findViewById(R.id.btn_tile_5)
        tile5.setOnClickListener {
            currentLetters.add(tile5.text.toString())
            tile5.isEnabled = false
            tile5.isClickable = false
            updateButtonView()
        }

        tile6 = findViewById(R.id.btn_tile_6)
        tile6.setOnClickListener {
            currentLetters.add(tile6.text.toString())
            tile6.isEnabled = false
            tile6.isClickable = false
            updateButtonView()
        }

        tile7 = findViewById(R.id.btn_tile_7)
        tile7.setOnClickListener {
            currentLetters.add(tile7.text.toString())
            tile7.isEnabled = false
            tile7.isClickable = false
            updateButtonView()
        }

        tile8 = findViewById(R.id.btn_tile_8)
        tile8.setOnClickListener {
            currentLetters.add(tile8.text.toString())
            tile8.isEnabled = false
            tile8.isClickable = false
            updateButtonView()
        }

        tile9 = findViewById(R.id.btn_tile_9)
        tile9.setOnClickListener {
            currentLetters.add(tile9.text.toString())
            tile9.isEnabled = false
            tile9.isClickable = false
            updateButtonView()
        }

        tile10 = findViewById(R.id.btn_tile_10)
        tile10.setOnClickListener {
            currentLetters.add(tile10.text.toString())
            tile10.isEnabled = false
            tile10.isClickable = false
            updateButtonView()
        }

        // Reset button functionality - clears all letters in play, re-enables all tiles
        reset = findViewById(R.id.btn_reset_word)
        reset.setOnClickListener {

            currentLetters.clear()
            updateButtonView()

            enableButtons()
        }

        // Submit button functionality - validates user created word
        submit = findViewById(R.id.btn_submit_word)
        submit.setOnClickListener {
            // Cast currentLetters into a string - to be passed to API for validation
            var word: String = ""
            for (x in currentLetters) {
                word += x
            }

            // Check if user made word was already checked
            if (invalidWords.contains(word)) {

                Toast.makeText(
                    this@MakeWordActivity,
                    "This word is invalid!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                // Send word to API
                validWord(word)
                // Determine word validity - update accordingly
                // Word is valid
                if (validWord == 1) {
                    title = findViewById(R.id.view_make_word)
                    title.text = "Valid Word"
                    Toast.makeText(
                        this@MakeWordActivity,
                        "Valid Word!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Send word to next activity - for AI to guess
                    val intent = Intent(this@MakeWordActivity, guessUserWordActivity::class.java)
                    // Clear background activities
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("word", word)
                    startActivity(intent)
                    finish()

                }
                // Word is invalid
                if (validWord == 2) {
                    title = findViewById(R.id.view_make_word)
                    title.text = "Invalid Word"
                    Toast.makeText(
                        this@MakeWordActivity,
                        "Invalid Word!",
                        Toast.LENGTH_SHORT
                    ).show()
                    invalidWords.add(word)
                }
            }
        }

    }

    private fun enableButtons() {
        tile1 = findViewById(R.id.btn_tile_1)
        tile1.isEnabled = true
        tile1.isClickable = true

        tile2 = findViewById(R.id.btn_tile_2)
        tile2.isEnabled = true
        tile2.isClickable = true

        tile3 = findViewById(R.id.btn_tile_3)
        tile3.isEnabled = true
        tile3.isClickable = true

        tile3 = findViewById(R.id.btn_tile_3)
        tile3.isEnabled = true
        tile3.isClickable = true

        tile4 = findViewById(R.id.btn_tile_4)
        tile4.isEnabled = true
        tile4.isClickable = true

        tile5 = findViewById(R.id.btn_tile_5)
        tile5.isEnabled = true
        tile5.isClickable = true

        tile6 = findViewById(R.id.btn_tile_6)
        tile6.isEnabled = true
        tile6.isClickable = true

        tile7 = findViewById(R.id.btn_tile_7)
        tile7.isEnabled = true
        tile7.isClickable = true

        tile8 = findViewById(R.id.btn_tile_8)
        tile8.isEnabled = true
        tile8.isClickable = true

        tile9 = findViewById(R.id.btn_tile_9)
        tile9.isEnabled = true
        tile9.isClickable = true

        tile10 = findViewById(R.id.btn_tile_10)
        tile10.isEnabled = true
        tile10.isClickable = true
    }


    // Updates the users word visually each time a letter is put into play
    private fun updateButtonView() {
        var word: String = ""
        for (x in currentLetters) {
            word += x
        }
        show_word = findViewById(R.id.btn_show_word)
        show_word.text = word
    }

    // Randomly assigns 10 random letters to each tile in player hand - letters to be used to create a random word
    private fun fillTiles() {

        // Variable is filled with random letters, set as players hand
        var playerHand = mutableListOf<String>()
        // List of letters and vowels to fill playerHand
        val letters = listOf('b','c','d','d','f','g','h','h','j','k','l','l','m','n','n','p','q','r','r',
            's','s','t','t','v','w','x','z')
        val vowels = listOf('a','e','e','i','o','u','y')

        // Determines how many vowels are in play - allows every hand to have 3 vowels
        var containVowel = 0
        // Continues whole loop (randomly assigning letters) until user has 10 letters available
        while (playerHand.size < 10) {
            // Grabs random letter from letters list
            var currentRandomLetter = letters.random().toString()
            // Prioritizes adding a random vowel over a random non-vowel letter - making sure at least 3 vowels are in play
            if (containVowel == 2) {
                // Reset vowel counter so other letters can be added as well
                containVowel = 0
                // Grab a random vowel from the vowels list
                var randomVowel = vowels.random().toString()
                // Ensure no duplicates of vowels are in hand
                while (playerHand.contains(randomVowel)) {
                    randomVowel = vowels.random().toString()
                }
                // Add the random vowel to the players hand
                playerHand.add(randomVowel)
            } else {
                // Add to vowel counter - when hitting 2, adds another vowel instead of a random letter
                containVowel++
                // Add the random letter to the players hand
                playerHand.add(currentRandomLetter)
            }
        }

        currentHand = playerHand

        // Update on-screen tiles with the 10 letters
        tile1 = findViewById(R.id.btn_tile_1)
        tile1.text = playerHand[0]
        tile2 = findViewById(R.id.btn_tile_2)
        tile2.text = playerHand[1]
        tile3 = findViewById(R.id.btn_tile_3)
        tile3.text = playerHand[2]
        tile4 = findViewById(R.id.btn_tile_4)
        tile4.text = playerHand[3]
        tile5 = findViewById(R.id.btn_tile_5)
        tile5.text = playerHand[4]
        tile6 = findViewById(R.id.btn_tile_6)
        tile6.text = playerHand[5]
        tile7 = findViewById(R.id.btn_tile_7)
        tile7.text = playerHand[6]
        tile8 = findViewById(R.id.btn_tile_8)
        tile8.text = playerHand[7]
        tile9 = findViewById(R.id.btn_tile_9)
        tile9.text = playerHand[8]
        tile10 = findViewById(R.id.btn_tile_10)
        tile10.text = playerHand[9]

    }

    // Contacts a dictionary API to validate if the users word is a word or not
    private fun validWord(word: String) {
        lifecycleScope.launch {
            // Create retrofit simpleton
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val validApi = retrofit.create(ValidWordInterface::class.java)
            val response = validApi.validWord(word)

            // Grab response from API - return to main activity if an error occurs
            // Update validWord value upon successful response
            if (response.isSuccessful) {
                // If the user created word is valid - update validWord variable with 1
                if (response.body()?.valid == true) {
                    validWord = 1
                }
                // If the user created word is invalid - update validWord variable with 2
                else if (response.body()?.valid == false) {
                    validWord = 2
                }
                // If neither responses are true - show error message and return to main menu
                else {
                    Toast.makeText(
                        this@MakeWordActivity,
                        "An error has occurred while validating",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MakeWordActivity, MainActivity::class.java))
                    finish()
                }

            } else {
                // If API response is unsuccessful - show error message and return to main menu
                Toast.makeText(
                    this@MakeWordActivity,
                    "An error has occurred while contacting API",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@MakeWordActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
package com.firebaseauthdemo

/**
 * Game starts with round 1 - Guess a word provided by "Ai" (Random Word Generator API)
 *  - User continually guesses random letters in an attempt to guess the random word correctly
 *  - User is allowed 5 strikes when guessing, passing 5 ends the game with a loss and provides an option to return to the main screen
 *  - Correctly guessing the word provides the user with the option to continue to the next round
 *  - Upon win/loss, all buttons are disabled to prevent game continuation
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

// API URL
const val BASE_URL = "https://api.api-ninjas.com/v1/"
// TAG
const val TAG = "guessAiWordActivity"
class guessAiWordActivity : AppCompatActivity() {

    // Declare activity view variables
    lateinit var view_word: Button
    lateinit var view_strike: Button
    lateinit var view_incorrect: Button
    lateinit var view_title: TextView
    // Declare keyboard buttons
    lateinit var btn_next: Button
    lateinit var btn_a: Button
    lateinit var btn_b: Button
    lateinit var btn_c: Button
    lateinit var btn_d: Button
    lateinit var btn_e: Button
    lateinit var btn_f: Button
    lateinit var btn_g: Button
    lateinit var btn_h: Button
    lateinit var btn_i: Button
    lateinit var btn_j: Button
    lateinit var btn_k: Button
    lateinit var btn_l: Button
    lateinit var btn_m: Button
    lateinit var btn_n: Button
    lateinit var btn_o: Button
    lateinit var btn_p: Button
    lateinit var btn_q: Button
    lateinit var btn_r: Button
    lateinit var btn_s: Button
    lateinit var btn_t: Button
    lateinit var btn_u: Button
    lateinit var btn_v: Button
    lateinit var btn_w: Button
    lateinit var btn_x: Button
    lateinit var btn_y: Button
    lateinit var btn_z: Button

    // Win/Loss Value - 0 = placeholder - 1 = Win - 2 = Loss
    var winLoss = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_ai_word)

        // Word sent from previous activity - Given from API
        val sendWord = intent.getStringExtra("word")
        val word = sendWord.toString()

        // List of the word to be guessed - add each letter of word to list
        var wordList = mutableListOf<String>()
        for (x in word) {
            wordList.add(x.toString())
        }

        // Underscores to be updated on screen
        var underscores = ""
        // List of underscores - add underscores to both variables based on length of wordList
        var underscoreList = mutableListOf<String>()
        for (x in wordList) {
            underscoreList.add("_")
            underscores+= "_"
        }

        // Incorrectly guessed letters - to be shown on screen and updated
        var incorrect = ""
        // Keep track of user strikes available - 0 = game over
        var strikes = 5

        // Set view on screen for strikes, incorrect guesses, and underscores - to be continually updated
        view_strike = findViewById(R.id.btn_strikes_remaining)
        view_incorrect = findViewById(R.id.btn_guesses)
        view_word = findViewById(R.id.btn_random_word)

        view_strike.text = "STRIKES - $strikes"
        view_incorrect.text = "GUESSES - $incorrect"
        view_word.text = underscores


        // Activate buttons
        // Start with next button disabled
        btn_next = findViewById(R.id.btn_next)
        btn_next.visibility = View.INVISIBLE

        // Start round timer, user has to try and guess the word before the timer ends otherwise the game ends
        val countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                // If a win/loss has already been met, dont update title
                if (winLoss == 1) {

                    view_title = findViewById(R.id.view_guess_title)
                    view_title.text = "CORRECT!"

                } else if (winLoss == 2) {

                    view_title = findViewById(R.id.view_guess_title)
                    view_title.text = "YOU LOST!"

                } else {

                    val remainingTime = millisUntilFinished / 1000
                    view_title = findViewById(R.id.view_guess_title)
                    view_title.text = "Time remaining: $remainingTime"

                }
            }
            // On timer finish, call endTime function to end the game if word hasnt been guessed
            override fun onFinish() {
                endTime(underscoreList, wordList)
            }
        }

        countDownTimer.start()

        // Set listeners for letters - every letter has same functionality regarding its value
        /**
         * On each letter button press, the selected letter will
         *  - check its own validity
         *  - call the updateUi function to change visuals accordingly
         *  - remove its own visibility
         *  - check if win or loss state has been reached
         */

        // Set view and listener for button
        // Comments on how buttons work is for btn_a only - same functionality with different values for every button
        // BUTTON A
        btn_a = findViewById(R.id.btn_press_a)
        btn_a.setOnClickListener {
            // Get button value as a string
            var letter = btn_a.text.toString()
            // If the given random word contains the button value - loop through the word
            if (wordList.contains(letter)) {
                // For each letter in the word, if it matches the buttons value - replace the underscore at the index with the letter
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
                // If given random word does not contain the buttons letter value, take a strike and add the letter to the incorrect guess variable
            } else {
                strikes--
                incorrect+= letter
            }
            // Call updateUi function to show what changes have been made upon button press
            updateUi(underscoreList, strikes, incorrect)
            // Call endGame function to determine win/loss state and continue accordingly
            endGame(underscoreList, wordList, strikes)
            // Remove button visibility so it cannot be guessed again
            btn_a.visibility =View.INVISIBLE
        }

        // BUTTON B
        btn_b = findViewById(R.id.btn_press_b)
        btn_b.setOnClickListener {
            var letter = btn_b.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_b.visibility =View.INVISIBLE
        }

        // BUTTON C
        btn_c = findViewById(R.id.btn_press_c)
        btn_c.setOnClickListener {
            var letter = btn_c.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_c.visibility =View.INVISIBLE
        }

        // BUTTON D
        btn_d = findViewById(R.id.btn_press_d)
        btn_d.setOnClickListener {
            var letter = btn_d.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_d.visibility =View.INVISIBLE
        }

        // BUTTON E
        btn_e = findViewById(R.id.btn_press_e)
        btn_e.setOnClickListener {
            var letter = btn_e.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_e.visibility =View.INVISIBLE
        }

        // BUTTON F
        btn_f = findViewById(R.id.btn_press_f)
        btn_f.setOnClickListener {
            var letter = btn_f.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_f.visibility =View.INVISIBLE
        }

        // BUTTON G
        btn_g = findViewById(R.id.btn_press_g)
        btn_g.setOnClickListener {
            var letter = btn_g.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_g.visibility =View.INVISIBLE
        }

        // BUTTON H
        btn_h = findViewById(R.id.btn_press_h)
        btn_h.setOnClickListener {
            var letter = btn_h.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_h.visibility =View.INVISIBLE
        }

        // BUTTON I
        btn_i = findViewById(R.id.btn_press_i)
        btn_i.setOnClickListener {
            var letter = btn_i.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_i.visibility =View.INVISIBLE
        }

        // BUTTON J
        btn_j = findViewById(R.id.btn_press_j)
        btn_j.setOnClickListener {
            var letter = btn_j.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_j.visibility =View.INVISIBLE
        }

        // BUTTON K
        btn_k = findViewById(R.id.btn_press_k)
        btn_k.setOnClickListener {
            var letter = btn_k.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_k.visibility =View.INVISIBLE
        }

        // BUTTON L
        btn_l = findViewById(R.id.btn_press_l)
        btn_l.setOnClickListener {
            var letter = btn_l.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_l.visibility =View.INVISIBLE
        }

        // BUTTON M
        btn_m = findViewById(R.id.btn_press_m)
        btn_m.setOnClickListener {
            var letter = btn_m.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_m.visibility =View.INVISIBLE
        }

        // BUTTON N
        btn_n = findViewById(R.id.btn_press_n)
        btn_n.setOnClickListener {
            var letter = btn_n.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_n.visibility =View.INVISIBLE
        }

        // BUTTON O
        btn_o = findViewById(R.id.btn_press_o)
        btn_o.setOnClickListener {
            var letter = btn_o.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_o.visibility =View.INVISIBLE
        }

        // BUTTON P
        btn_p = findViewById(R.id.btn_press_p)
        btn_p.setOnClickListener {
            var letter = btn_p.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_p.visibility =View.INVISIBLE
        }

        // BUTTON Q
        btn_q = findViewById(R.id.btn_press_q)
        btn_q.setOnClickListener {
            var letter = btn_q.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_q.visibility =View.INVISIBLE
        }

        // BUTTON R
        btn_r = findViewById(R.id.btn_press_r)
        btn_r.setOnClickListener {
            var letter = btn_r.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_r.visibility =View.INVISIBLE
        }

        // BUTTON S
        btn_s = findViewById(R.id.btn_press_s)
        btn_s.setOnClickListener {
            var letter = btn_s.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_s.visibility =View.INVISIBLE
        }

        // BUTTON T
        btn_t = findViewById(R.id.btn_press_t)
        btn_t.setOnClickListener {
            var letter = btn_t.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_t.visibility =View.INVISIBLE
        }

        // BUTTON U
        btn_u = findViewById(R.id.btn_press_u)
        btn_u.setOnClickListener {
            var letter = btn_u.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_u.visibility =View.INVISIBLE
        }

        // BUTTON V
        btn_v = findViewById(R.id.btn_press_v)
        btn_v.setOnClickListener {
            var letter = btn_v.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_v.visibility =View.INVISIBLE
        }

        // BUTTON W
        btn_w = findViewById(R.id.btn_press_w)
        btn_w.setOnClickListener {
            var letter = btn_w.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_w.visibility =View.INVISIBLE
        }

        // BUTTON X
        btn_x = findViewById(R.id.btn_press_x)
        btn_x.setOnClickListener {
            var letter = btn_x.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_x.visibility =View.INVISIBLE
        }

        // BUTTON Y
        btn_y = findViewById(R.id.btn_press_y)
        btn_y.setOnClickListener {
            var letter = btn_y.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_y.visibility =View.INVISIBLE
        }

        // BUTTON Z
        btn_z = findViewById(R.id.btn_press_z)
        btn_z.setOnClickListener {
            var letter = btn_z.text.toString()
            if (wordList.contains(letter)) {
                for ((item, x) in word.withIndex()) {
                    if (x.toString() == letter) {
                        underscoreList[item] = x.toString()
                    }
                }
            } else {
                strikes--
                incorrect+= letter
            }
            updateUi(underscoreList, strikes, incorrect)
            endGame(underscoreList, wordList, strikes)
            btn_z.visibility =View.INVISIBLE
        }

        // Continue to next activity or return to MainActivity depending on win/loss - only visible in win/loss state
        btn_next = findViewById(R.id.btn_next)
        btn_next.setOnClickListener {
            
            // Return home upon losing
            if (winLoss == 2) {
                startActivity(Intent(this@guessAiWordActivity, MainActivity::class.java))
                finish()
            }
            // Move to next activity upon winning
            if (winLoss == 1) {
                startActivity(Intent(this@guessAiWordActivity, MakeWordActivity::class.java))
                finish()
            }
        }

    }

    // Update on screen visuals given underscore value, strikes, and incorrect guesses - to be updated on letter button press
    private fun updateUi(underscore: MutableList<String>, strikes: Int, incorrect: String) {
        var underscoreString = ""
        for (x in underscore) {
            underscoreString+= x
        }
        // Updated visuals
        view_word = findViewById(R.id.btn_random_word)
        view_strike = findViewById(R.id.btn_strikes_remaining)
        view_incorrect = findViewById(R.id.btn_guesses)

        view_word.text = underscoreString
        view_strike.text = "STRIKES - $strikes"
        view_incorrect.text = "GUESSES - $incorrect"
    }

    // This function is called when the round timer ends - checks if word has not been guessed, and ends the game accordingly
    private fun endTime(underscoreList: MutableList<String>, wordList: MutableList<String>) {
        // Revert wordList into a string - to be checked
        var word = ""
        for (x in wordList) {
            word+= x
        }
        // Revert underscoreList into string - to be checked
        var underscore = ""
        for (x in underscoreList) {
            underscore+= x
        }

        // Ensure word has not been guessed
        if (word.trim() != underscore.trim()) {
            Toast.makeText(
                this@guessAiWordActivity,
                "Times Up!",
                Toast.LENGTH_LONG
            ).show()

            // Update winLoss value so 'next' button proceeds accordingly
            winLoss = 2

            // Update round title
            view_title = findViewById(R.id.view_guess_title)
            view_title.text = "TIMES UP!"

            // Show next button
            btn_next = findViewById(R.id.btn_next)
            btn_next.visibility = View.VISIBLE

        }


    }

    // Determines win/loss state of game - loss is at 0 strikes - win is correctly guessed letter
    private fun endGame(underscoreList: MutableList<String>, wordList: MutableList<String>, strikes: Int) {
        // Revert wordList into a string - to be checked
        var word = ""
        for (x in wordList) {
            word+= x
        }
        // Revert underscoreList into string - to be checked
        var underscore = ""
        for (x in underscoreList) {
            underscore+= x
        }

        // Check if strikes hit 0 - ends in a loss
        // Set loss value - make next button visible
        // Update screen title with loss
        if (strikes == 0) {
            Toast.makeText(
                this@guessAiWordActivity,
                "You have lost! Please return to the home screen!",
                Toast.LENGTH_LONG
            ).show()
            winLoss = 2

            view_title = findViewById(R.id.view_guess_title)
            view_title.text = "YOU LOST!"

            btn_next = findViewById(R.id.btn_next)
            btn_next.visibility = View.VISIBLE
        }

        // Check if word matches underscore(correctly guessed letters)
        // Set win value - make next button visible
        // Update screen title showing a correct guess
        if (word.trim() == underscore.trim()) {
            Toast.makeText(
                this@guessAiWordActivity,
                "Correct! Please continue!",
                Toast.LENGTH_LONG
            ).show()
            winLoss = 1

            view_title = findViewById(R.id.view_guess_title)
            view_title.text = "CORRECT!"

            btn_next = findViewById(R.id.btn_next)
            btn_next.visibility = View.VISIBLE

        }

        // Remove all letters - prevents users from breaking the game by continuing to play
        // Go through each button, disable button functionality
        if (winLoss == 1 || winLoss == 2) {
            disableButtons()
        }
    }

    // Disables all letter buttons on screen
    private fun disableButtons() {

        btn_a = findViewById(R.id.btn_press_a)
        btn_a.isEnabled = false
        btn_a.isClickable = false

        btn_b = findViewById(R.id.btn_press_b)
        btn_b.isEnabled = false
        btn_b.isClickable = false

        btn_c = findViewById(R.id.btn_press_c)
        btn_c.isEnabled = false
        btn_c.isClickable = false

        btn_d = findViewById(R.id.btn_press_d)
        btn_d.isEnabled = false
        btn_d.isClickable = false

        btn_e = findViewById(R.id.btn_press_e)
        btn_e.isEnabled = false
        btn_e.isClickable = false

        btn_f = findViewById(R.id.btn_press_f)
        btn_f.isEnabled = false
        btn_f.isClickable = false

        btn_g = findViewById(R.id.btn_press_g)
        btn_g.isEnabled = false
        btn_g.isClickable = false

        btn_h = findViewById(R.id.btn_press_h)
        btn_h.isEnabled = false
        btn_h.isClickable = false

        btn_i = findViewById(R.id.btn_press_i)
        btn_i.isEnabled = false
        btn_i.isClickable = false

        btn_j = findViewById(R.id.btn_press_j)
        btn_j.isEnabled = false
        btn_j.isClickable = false

        btn_k = findViewById(R.id.btn_press_k)
        btn_k.isEnabled = false
        btn_k.isClickable = false

        btn_l = findViewById(R.id.btn_press_l)
        btn_l.isEnabled = false
        btn_l.isClickable = false

        btn_m = findViewById(R.id.btn_press_m)
        btn_m.isEnabled = false
        btn_m.isClickable = false

        btn_n = findViewById(R.id.btn_press_n)
        btn_n.isEnabled = false
        btn_n.isClickable = false

        btn_o = findViewById(R.id.btn_press_o)
        btn_o.isEnabled = false
        btn_o.isClickable = false

        btn_p = findViewById(R.id.btn_press_p)
        btn_p.isEnabled = false
        btn_p.isClickable = false

        btn_q = findViewById(R.id.btn_press_q)
        btn_q.isEnabled = false
        btn_q.isClickable = false

        btn_r = findViewById(R.id.btn_press_r)
        btn_r.isEnabled = false
        btn_r.isClickable = false

        btn_s = findViewById(R.id.btn_press_s)
        btn_s.isEnabled = false
        btn_s.isClickable = false

        btn_t = findViewById(R.id.btn_press_t)
        btn_t.isEnabled = false
        btn_t.isClickable = false

        btn_u = findViewById(R.id.btn_press_u)
        btn_u.isEnabled = false
        btn_u.isClickable = false

        btn_v = findViewById(R.id.btn_press_v)
        btn_v.isEnabled = false
        btn_v.isClickable = false

        btn_w = findViewById(R.id.btn_press_w)
        btn_w.isEnabled = false
        btn_w.isClickable = false

        btn_x = findViewById(R.id.btn_press_x)
        btn_x.isEnabled = false
        btn_x.isClickable = false

        btn_y = findViewById(R.id.btn_press_y)
        btn_y.isEnabled = false
        btn_y.isClickable = false

        btn_z = findViewById(R.id.btn_press_z)
        btn_z.isEnabled = false
        btn_z.isClickable = false

    }

}
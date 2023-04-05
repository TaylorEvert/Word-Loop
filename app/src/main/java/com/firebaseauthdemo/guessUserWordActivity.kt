package com.firebaseauthdemo

/**
 * This activity is responsible for the final round of the 3 section game loop - AI guesses the user created word
 *  - Displays strikes, incorrect guesses, the user sent word, and the correctly guessed letters
 *  - Correct guesses redirects the user to round 1 for the user to guess the AI word again
 *  - Incorrect guesses redirects the user to the Main Menu and the user wins
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.firebaseauthdemo.randomword.RandomWordInterface
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class guessUserWordActivity : AppCompatActivity() {

    // Declare Activity Button View Variables
    lateinit var show_title: TextView // view_title_guess_user
    lateinit var show_strikes: Button // btn_strikes
    lateinit var show_incorrect: Button // btn_incorrect
    lateinit var show_user_word: Button // btn_user_word
    lateinit var show_guess_word: Button // btn_guess_word
    lateinit var show_next: Button // btn_return
    private var userDao: UserDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_user_word)

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "new-db").build()
        userDao = db.userDao()

        // User word sent from previous activity - to be guessed by AI
        val sendWord = intent.getStringExtra("word")
        val word = sendWord.toString()
        // Show user word
        show_user_word = findViewById(R.id.btn_user_word)
        show_user_word.text = "YOUR WORD - $word"

        // Start with "Next" button as invisible - show after round ends
        show_next = findViewById(R.id.btn_return)
        show_next.visibility = View.INVISIBLE

        // Send word the AI guess function - returns 1 upon winning, otherwise the AI has lost
        // Declare title view - updated upon win/loss
        show_title = findViewById(R.id.view_title_guess_user)
        // The AI has correctly guessed the users word
        // Update title, make toast to return to round 1, make next button visible and allow user to return to round 1 (guessAIWord)
        if (guess(word) == 1) {
            // Update title
            show_title.text = "The AI has guessed your word!"
            // Send user toast message
            Toast.makeText(
                this@guessUserWordActivity,
                "Please return to round 1!",
                Toast.LENGTH_SHORT
            ).show()
            // Show button - send user back to round 1 with a random word
            show_next.visibility = View.VISIBLE
            show_next.setOnClickListener {
                show_next.isClickable = false
                show_next.isEnabled = false
                getRandomWord()
            }

            // The Ai has incorrectly guessed the user word and has lost
            // Update title, make toast to return to main menu, make next button visible and allow user to return to main menu
        } else {
            // Update title
            show_title.text = "You have won!"


            // Send user toast message
            Toast.makeText(
                this@guessUserWordActivity,
                "Please return to the main menu!",
                Toast.LENGTH_SHORT
            ).show()
            // Show button - send user to main menu
            // add win to database
            show_next.visibility = View.VISIBLE
            show_next.setOnClickListener {
                show_next.isClickable = false
                show_next.isEnabled = false
                startActivity(Intent(this@guessUserWordActivity, MainActivity::class.java))
                finish()
            }

        }

    }

    /**
     * --- Logic and Explanation behind the AI guess word function ---
     * --- Attempts to guess a word, provided by the user ---
     *
     *  - Populates a list of each letter from the word provided by the user - Then populates a guessing pool (list) based off of the word
     *      - The guessing pool list contains each letter from the word as well as a random letter to match each.
     *          The guessing pool list has a chance to add one extra letter for each letter added.
     *          This list may not contain duplicate letters. This is the list the AI will pull from to guess a letter.
     *
     *  - Underscores/generateUnderscores and updateUnderscores are all responsible for what is shown on screen related to what is being guesses
     *      - underscores variable is what is actually being shown on screen, the other variations of underscore are what changes this value.
     *          underscores variable is updated when generateUnderscores is updated, and generateUnderscores is updated when a letter is added
     *          to the updateUnderscores() function. A letter is added the AI makes a correct guess of a letter
     *      - generateUnderscores is the list of what needs to be shown on screen.
     *          Starts by adding an underscore for each letter in the users word.
     *          This is updated when the updateUnderscores() function is passed a letter.
     *      - updateUnderscores() function is responsible for resetting the underscore variable, updating the index of generateUnderscores which in hand updates underscore var
     *          Upon being passed a correct letter, the index of the letter in the users word is then found, and that same index is updated in generateUnderscore.
     *          Once the index of generateUnderscore has been updated, the underscore variable is set to the new updated value
     *
     *  - updateView() function is responsible for updating all values on the screen for the user to see
     *      - Starts by updating how many strikes are remaining depending on incorrect/correct guesses
     *      - Updates the underscore value on screen - this is word that is attempting to be guessed
     *      - Updates the 'guesses' section on screen - this shows each incorrect guess that has been made
     *      - updateView() is called upon each guess attempt that has been made
     *
     *  - The attempt to guess the word - The section of the function where attempts are made to guess the users word
     *      - Upon the guessing pool being completed, random letters are selected from the pool, one at a time.
     *          No duplicate attempts are also ensured
     *      - Depending on a correct or incorrect guess, different values and functions are updated
     *          - If a correct guess is made, updateUnderscores() is called, the correct letter is added to a list, and the letter is removed from the guessing pool,
     *            and the updateView() function is called
     *          - If an incorrect guess is made, a strike is removed, the letter is added to a list of incorrect guesses, and the updateView() function is called
     *      - Upon a correctly guessed word, or if strikes hit 0, the game is either won or lost - winLoss is a variable that determines the state of the endgame,
     *        this is the value that the guess() function as a whole returns. The continued route of the game is determined based on this returned value
     *          - If the AI has won, the winLoss value is set to 1 and the user will be routed back to round 1 of the game with a random word
     *          - if the AI has lost, the winLoss value is set to 2 and the user will be routed back to the main menu with a win
     *
     */
    fun guess(guessWord: String): Int {

        // A list of each character within the player given word
        var listWord = mutableListOf<String>()
        // List of the alphabet, to be randomly added to the guess pool
        var listAlpha = mutableListOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
        // A list of each possible choice available to guess
        var guessPool = mutableListOf<String>()
        // Keeps track of incorrectly guessed letters
        var incorrectGuesses = mutableListOf<String>()
        // Keeps track of correctly guessed letters
        var correctGuesses = mutableListOf<String>()
        // String to be updated on screen for the amount of underscores available
        var underscores = ""
        // String to be updated on screen for the letters that have been guessed incorrectly
        var incorrect = ""
        // Keeps track of the amount of lives/strikes remaining
        var strikes = 5
        // Determine if the ai has correctly or incorrectly guessed the word
        // 0: ai in play, 1: ai wins, 2: ai lost
        var winLoss = 0

        // Populates listWord with each character of the player given word
        // Populates guessPool with each character of the player given word as well as one random character to match the player character
        for (x in guessWord) {
            listWord.add(x.toString())
            guessPool.add(x.toString())
            var randomGuessOne = listAlpha.random().toString()
            var randomGuessTwo = listAlpha.random().toString()
            // Ensures multiples are not present in the guess pool
            while(guessPool.contains(randomGuessOne) || guessPool.contains(randomGuessTwo)) {
                randomGuessOne = listAlpha.random().toString()
                randomGuessTwo = listAlpha.random().toString()
            }
            var extraLetter = (0..100).random()
            if (extraLetter < 70) {
                guessPool.add(randomGuessOne)
                guessPool.add(randomGuessTwo)
            } else {
                guessPool.add(randomGuessOne)
            }
        }
        // Sets an amount of underscores equal to the amount of characters in the player given word
        var generateUnderscores = mutableListOf<String>()
        for (x in listWord) {
            generateUnderscores+= "_"
        }
        // Changes the indexed value of the sent letter to match the underscored index
        fun updateUnderscores(letter: String) {
            underscores = ""
            var changeIndex = listWord.indexOf(letter)
            generateUnderscores[changeIndex] = letter
            for (x in generateUnderscores) {
                underscores+= x
            }
        }
        // Updates all visuals on the screen
        fun updateView() {
            // Update strike value
            show_strikes = findViewById(R.id.btn_strikes)
            show_strikes.text = "STRIKES - $strikes"
            // Update underscore value
            show_guess_word = findViewById(R.id.btn_guess_word)
            show_guess_word.text = underscores
            // Update incorrect guesses value
            incorrect = ""
            for (x in incorrectGuesses) {
                incorrect+= x
            }
            show_incorrect = findViewById(R.id.btn_incorrect)
            show_incorrect.text = "GUESSES - $incorrect"
        }


        // Continues to randomly guess letters until strikes run out or the word was correctly guessed
        while (strikes > 0) {

            // A random letter from the guess pool - ensure a letter that has already been guessed, isnt guessed again
            var currentGuess = guessPool.random().toString()
            while(correctGuesses.contains(currentGuess) || incorrectGuesses.contains(currentGuess)) {
                currentGuess = guessPool.random().toString()
            }
            // If correct guess, call underscore function to update correct index of letter and underscore
            // If correct guess, add the letter to correct guess list
            // If correct guess, update visuals on screen
            if (listWord.contains(currentGuess)) {
                updateUnderscores(currentGuess)
                correctGuesses.add(currentGuess)
                guessPool.remove(currentGuess)
                updateView()
                // If incorrect guess, remove a strike from remaining lives
                // If incorrect guess, add letter to incorrect guess list
                // If incorrect guess, update visuals on screen
            } else {
                strikes--
                incorrectGuesses.add(currentGuess)
                updateView()
            }
            // Determine if the ai has won, if so, set winloss accordingly then exit while loop
            if (listWord.size == correctGuesses.size &&
                listWord.containsAll(correctGuesses) && correctGuesses.containsAll(listWord)) {
                winLoss = 1
                break
            }


        }
        // Set winloss to 2(ai lost) if ran out of lives and guessed incorrectly again
        if (strikes <= 0) {
            winLoss = 2
            lifecycleScope.launch {
                userDao?.addVictory()
            }
        }

        return winLoss
    }

    /**
     * - This function is only called upon the AI winning and the user needing to go back to round 1
     * - This function is called here so that the API can be contacted and the AI can receive a random word for the user to guess on round 1
     * - This function is called when the user presses the next button, and will send the user to round 1 with the random word
     */
    // Call API to get a random word - send to next activity with the random word
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
                    this@guessUserWordActivity,
                    word,
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@guessUserWordActivity, guessAiWordActivity::class.java)
                // Clear background activities
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("word", word)
                startActivity(intent)
                finish()

            } else {
                Log.d("GuessUserWord", "getRandomWord: $response")
                // Due to the next button being disabled and only allowing one click, if an API error occurs, send the user home with a win
                Toast.makeText(
                    this@guessUserWordActivity,
                    "An error has occurred, you have won",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@guessUserWordActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
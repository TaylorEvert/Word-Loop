package com.firebaseauthdemo

/**
 * First screen the user has access to upon opening the app
 *  - Provides link to registration screen
 *  - Provides login availability upon entering correct credentials
 *  - Two routes - Either takes user to main menu, or takes user to registration
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    // Declare login view inputs and buttons
    lateinit var login_email: TextInputEditText
    lateinit var login_password: TextInputEditText
    lateinit var login_button: Button
    lateinit var login_register_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Assign register id - set listener for moving to register activity
        login_register_button = findViewById(R.id.btn_register)
        login_register_button.setOnClickListener {
            // Move to register activity
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        // Assign view id for input and login variables
        login_email = findViewById(R.id.et_login_email)
        login_password = findViewById(R.id.et_login_password)
        login_button = findViewById(R.id.btn_login)

        // Login button on click listener
        login_button.setOnClickListener {
            // Check if email and password fields are empty
            when {
                // Make toast if email field is empty
                TextUtils.isEmpty(login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Make toast if password field is empty
                TextUtils.isEmpty(login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                // If email and password checks pass
                else -> {

                    // Declare email and password variables
                    val email: String = login_email.text.toString().trim { it <= ' ' }
                    val password: String = login_password.text.toString().trim{ it <= ' '}

                    // Use Firebase class to get an instance of the connection, and login user given correct information
                    // Use an onCompleteListener to determine success
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        // Results of authentication listener
                        OnCompleteListener<AuthResult> { task ->

                            // If login is successful
                            if (task.isSuccessful) {

                                // Show successful login to user
                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are successfully logged in!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                /**
                                 * Automatically sign in user
                                 * Send user to MainActivity displaying user information
                                 */

                                // Move to Main Activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                // Clear background activities
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                // Send with user information
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                // Finish Login Activity and move to Main Activity
                                finish()

                            } else {
                                // Show error message if login is unsuccessful - EX: no internet connection
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    )

                }
            }

        }

    }
}
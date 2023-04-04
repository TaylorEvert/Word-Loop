package com.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class RegisterActivity : AppCompatActivity() {

    // Declare variables to be assigned with their id
    lateinit var regisrationButton: Button
    lateinit var registerUsername: Button
    lateinit var registerEmailField: TextInputEditText
    lateinit var registerPasswordField: TextInputEditText
    lateinit var return_login: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Assign layout ID to variable declaration
        regisrationButton = findViewById(R.id.btn_register_user)
        registerUsername = findViewById(R.id.et_register_username)
        registerEmailField = findViewById(R.id.et_register_email)
        registerPasswordField = findViewById(R.id.et_register_password)
        return_login = findViewById(R.id.btn_return_login)

        // Return to login activity from register
        return_login.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }

        // On Click of REGISTRATION BUTTON
        regisrationButton.setOnClickListener {
            // Check if email and password fields are empty
            when {
                // Make toast if email field is empty
                TextUtils.isEmpty(registerUsername.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter a username",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Make toast if email field is empty
                TextUtils.isEmpty(registerEmailField.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Make toast if password field is empty
                TextUtils.isEmpty(registerPasswordField.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                // If email and password checks pass
                else -> {

                    // Declare email and password variables
                    val email: String = registerEmailField.text.toString().trim { it <= ' ' }
                    val password: String = registerPasswordField.text.toString().trim{ it <= ' '}
                    val username: String = registerUsername.text.toString().trim(){ it <= ' '}

                    // Use Firebase class to get an instance of the connection, and create a user with the added information
                    // Use an onCompleteListener to determine success
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        // Results of authentication listener
                        OnCompleteListener<AuthResult> { task ->

                            // If registration is successful
                            if (task.isSuccessful) {

                                // Create local user db -- add username to db


                                // Registered User
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                // Show successful registration to user
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You are successfully registered!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                /**
                                 * Automatically sign in new user
                                 * Sign-out user and send to Main Activity with their credentials used for registering
                                 */

                                // Move to Main Activity
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                // Clear background activities
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                // Send with user information
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                // Finish Registration Activity and move to Main Activity
                                finish()

                            } else {
                                // Show error message if registration is unsuccessful
                                Toast.makeText(
                                    this@RegisterActivity,
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
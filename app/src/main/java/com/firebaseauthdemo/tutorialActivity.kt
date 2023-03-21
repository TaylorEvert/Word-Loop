package com.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class tutorialActivity : AppCompatActivity() {

    // Declare return button

    lateinit var return_home: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        return_home = findViewById(R.id.btn_main_return)
        return_home.setOnClickListener {
            startActivity(Intent(this@tutorialActivity, MainActivity::class.java))
            finish()
        }

    }
}
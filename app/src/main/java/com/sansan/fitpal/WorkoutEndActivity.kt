package com.sansan.fitpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WorkoutEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_end)

        var buttonOk: Button = findViewById(R.id.buttonOk)

        buttonOk.setOnClickListener{
            val mainScreen = Intent(this@WorkoutEndActivity, MainActivity::class.java)
            startActivity(mainScreen)
            finish()
        }

    }
}
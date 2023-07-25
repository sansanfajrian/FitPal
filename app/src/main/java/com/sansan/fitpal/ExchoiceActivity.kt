package com.sansan.fitpal

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request

class ExchoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchoice)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        var buttonBackExchoice: Button = findViewById(R.id.buttonBackExchoice)
        var imageButton1: ImageView = findViewById(R.id.imageButton1)
        var imageButton2: ImageView = findViewById(R.id.imageButton2)
        var imageButton3: ImageView = findViewById(R.id.imageButton3)
        var imageButton4: ImageView = findViewById(R.id.imageButton4)
        var imageButton5: ImageView = findViewById(R.id.imageButton5)

        buttonBackExchoice.setOnClickListener{
            val mainScreen = Intent(this@ExchoiceActivity, MainActivity::class.java)
            startActivity(mainScreen)
            finish()
        }

        imageButton1.setOnClickListener {
            if (isApiRunning()) {
                val countDownScreen = Intent(this, ExerciseActivity::class.java)
                countDownScreen.putExtra("exerciseType", "chest")
                startActivity(countDownScreen)
            } else {
                Toast.makeText(
                    this,
                    "There is a problem with related API please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imageButton2.setOnClickListener {
            if (isApiRunning()) {
                val countDownScreen = Intent(this, ExerciseActivity::class.java)
                countDownScreen.putExtra("exerciseType", "waist")
                startActivity(countDownScreen)
            } else {
                Toast.makeText(
                    this,
                    "There is a problem with related API please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imageButton3.setOnClickListener {
            if (isApiRunning()) {
                val countDownScreen = Intent(this, ExerciseActivity::class.java)
                countDownScreen.putExtra("exerciseType", "upper arms")
                startActivity(countDownScreen)
            } else {
                Toast.makeText(
                    this,
                    "There is a problem with related API please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imageButton4.setOnClickListener {
            if (isApiRunning()) {
                val countDownScreen = Intent(this, ExerciseActivity::class.java)
                countDownScreen.putExtra("exerciseType", "back")
                startActivity(countDownScreen)
            } else {
                Toast.makeText(
                    this,
                    "There is a problem with related API please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imageButton5.setOnClickListener {
            if (isApiRunning()) {
                val countDownScreen = Intent(this, ExerciseActivity::class.java)
                countDownScreen.putExtra("exerciseType", "lower legs")
                startActivity(countDownScreen)
            } else {
                Toast.makeText(
                    this,
                    "There is a problem with related API please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isApiRunning(): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/equipment/body%20weight")
            .get()
            .addHeader("X-RapidAPI-Key", "3cbecd911amshfed29649108244fp15570ejsn265c43016c70")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        return response.isSuccessful
    }
}

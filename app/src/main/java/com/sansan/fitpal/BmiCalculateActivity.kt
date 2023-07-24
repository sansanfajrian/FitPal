package com.sansan.fitpal

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request


class BmiCalculateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setContentView(R.layout.activity_bmi_calculate)
        var buttonBack: Button = findViewById(R.id.buttonBack)
        var buttonCalculate: Button = findViewById(R. id.buttonCalculate)
        var weightInput: TextInputEditText = findViewById(R.id.weightInput)
        var heightInput: TextInputEditText = findViewById(R.id.heightInput)
        var textViewBmiScore: TextView = findViewById(R.id.textViewBmiScore)
        var textViewBmiDescription: TextView = findViewById(R.id.textViewBmiDescription)


        buttonBack.setOnClickListener{
            val mainScreen = Intent(this@BmiCalculateActivity, MainActivity::class.java)
            startActivity(mainScreen)
            finish()
        }

        buttonCalculate.setOnClickListener{
            var weight = weightInput.text.toString()
            var height = heightInput.text.toString()
            var url = "https://mega-fitness-calculator1.p.rapidapi.com/bmi?weight=$weight&height=$height"
            if (weight.isNotEmpty() && height.isNotEmpty()){
                try {

                    val client = OkHttpClient()

                    val request = Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("X-RapidAPI-Key", "ba2c4e86cemsh728f246944e81b3p1707aajsn4885f9a6ce93")
                        .addHeader("X-RapidAPI-Host", "mega-fitness-calculator1.p.rapidapi.com")
                        .build()

                    val response = client.newCall(request).execute().body
                    val json_res = Gson().fromJson(response?.string() ?: null, bmi::class.java)
                    var bmiScore = json_res.info.bmi
                    var bmihealth = json_res.info.health
                    var bmihealthrange = json_res.info.healthy_bmi_range
                    textViewBmiScore.setText(bmiScore.toString())

                    if (bmiScore < 19){
                        textViewBmiScore.setTextColor(Color.RED)
                        textViewBmiDescription.setTextColor(Color.RED)
                        textViewBmiDescription.setText("$bmihealth - it's dangerous! You should gain some weight! Your Reccomendation is : $bmihealthrange")
                    }
                    if (bmiScore > 19 && bmiScore < 26){
                        textViewBmiScore.setTextColor(Color.GREEN)
                        textViewBmiDescription.setTextColor(Color.GREEN)
                        textViewBmiDescription.setText("$bmihealth - You have nothing to worry about!")
                    }
                    if (bmiScore > 26 && bmiScore < 30){
                        textViewBmiScore.setTextColor(Color.YELLOW)
                        textViewBmiDescription.setTextColor(Color.YELLOW)
                        textViewBmiDescription.setText("$bmihealth  - Maybe it's time to think about a bit of exercise? Your Reccomendation is : $bmihealthrange")
                    }
                    if (bmiScore > 30){
                        textViewBmiScore.setTextColor(Color.RED)
                        textViewBmiDescription.setTextColor(Color.RED)
                        textViewBmiDescription.setText("$bmihealth - it's dangerous! You should probably lose some weight! Your Reccomendation is : $bmihealthrange")
                    }
                    else {
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    }
                }
                catch(e: java.lang.IllegalArgumentException) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
}
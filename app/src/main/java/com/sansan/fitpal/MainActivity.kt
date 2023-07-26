package com.sansan.fitpal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setContentView(R.layout.activity_main)

        var imageView: ImageView = findViewById(R.id.imageView)
        var textViewAuthor: TextView = findViewById(R.id.textViewAuthor)
        var textViewQuote: TextView = findViewById(R.id.textViewQuote)
        var bmiButton: Button = findViewById(R.id.bmiButton)
        var startButton: Button = findViewById(R.id.startButton)

        var random = Random()
        fun rand(from: Int, to: Int): Int {
            return random.nextInt(to - from) + from
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://quotes-by-api-ninjas.p.rapidapi.com/v1/quotes?category=success&limit=10")
            .get()
            .addHeader("X-RapidAPI-Key", "ba2c4e86cemsh728f246944e81b3p1707aajsn4885f9a6ce93")
            .addHeader("X-RapidAPI-Host", "quotes-by-api-ninjas.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute().body?.string()
        val quotes: Array<Quotes> = Gson().fromJson(response, Array<Quotes>::class.java)
        var randomQuote = rand(0, 10)
        var quotes2 = quotes[randomQuote].quote
        var author2 = quotes[randomQuote].author


        startButton.setOnClickListener{
            val exchoice = Intent(this, ExchoiceActivity::class.java)
            startActivity(exchoice)
        }


        textViewQuote.setText("„$quotes2”")
        textViewAuthor.setText(author2)

        bmiButton.setOnClickListener{
            var bmiScreen = Intent(this, BmiCalculateActivity::class.java)
            startActivity(bmiScreen)
        }

    }
    }
package com.sansan.fitpal

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.droidsonroids.gif.GifImageView
import java.util.*

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var speak: Boolean = false
    private var sound: Boolean = false
    private var workout: Boolean = true
    private var player: MediaPlayer? = null
    private lateinit var currentExerciseLength: List<Long>


    override fun onCreate(savedInstanceState: Bundle?) {
        workout = true
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setContentView(R.layout.activity_exercise)

        val exerciseType = intent.getStringExtra("exerciseType")

        val switchSound: Switch = findViewById(R.id.switchSound)
        val switchSpeak: Switch = findViewById(R.id.switchSpeak)
        val textViewExercise: TextView = findViewById(R.id.textViewExercise)
        val textViewCountDown: TextView = findViewById(R.id.textViewCountDown)
        val imageView2: ImageView = findViewById(R.id.imageView2)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        tts = TextToSpeech(this, this)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/equipment/body%20weight")
            .get()
            .addHeader("X-RapidAPI-Key", "3cbecd911amshfed29649108244fp15570ejsn265c43016c70")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val responseJson = response.body?.string()
        val exerciseList: List<Exercise> =
            Gson().fromJson(responseJson, Array<Exercise>::class.java).toList()


        val currentExercise = mutableListOf<String>()
        currentExercise.add(0, "placeholder")

        val currentExerciseDrawable = mutableListOf<String>()
        currentExerciseDrawable.add(0, "@drawable/placeholder")

        // Filter exerciseList berdasarkan exerciseType
        val filteredList = exerciseList.filter { it.bodyPart == exerciseType }.take(10)

        currentExercise.addAll(filteredList.map { it.name })
        currentExerciseDrawable.addAll(filteredList.map { it.gifUrl })

        var currentExerciseIndex = 0
        var firstTime = false
        if (filteredList.isNotEmpty()) {
            currentExerciseLength = filteredList.mapIndexed { index, exercise ->
                if (index == 0) 11000L else 46000L
            }
        } else {
            Toast.makeText(
                this,
                "No exercises found for the selected body part",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        var count = currentExerciseLength[currentExerciseIndex].toInt() / 1000
        textViewExercise.text =
            "Get ready for: "+filteredList.getOrNull(currentExerciseIndex)?.name ?: "No exercise found"

        Glide.with(this)
            .asGif()
            .load(filteredList.getOrNull(currentExerciseIndex)?.gifUrl ?: "@drawable/placeholder") // exerciseDrawable adalah URL gambar yang sudah Anda dapatkan dari API
            .into(imageView2)

        var exercisesCount = 0


        fun speakOut(text: String) {
            if (speak) {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }

        fun play() {
            if (sound) {
                val soundURI = Uri.parse("android.resource://com.sansan.fitpal/" + R.raw.tick)
                player = MediaPlayer.create(applicationContext, soundURI)
                player?.isLooping = false
                player?.start()
            }
        }

        fun exercise() {

            fun rest() {
                if (currentExercise.size > exercisesCount) {
                    var restCountdown = 15 //set to 15 later
                    val nextExercise = currentExercise[currentExerciseIndex + 1].toString()
                    val exerciseDrawable = currentExerciseDrawable[currentExerciseIndex + 1]
                    textViewExercise.text = "Rest and get ready for: $nextExercise"
                    speakOut(textViewExercise.text.toString())
                    Glide.with(this)
                        .asGif()
                        .load(exerciseDrawable) // exerciseDrawable adalah URL gambar yang sudah Anda dapatkan dari API
                        .into(imageView2)

                    val countDownTimerRest = object :
                        CountDownTimer(15000, 1000) { //set the millisInFuture to 15000 later
                        override fun onTick(p0: Long) {
                            restCountdown = restCountdown - 1
                            textViewCountDown.text = restCountdown.toString()
                            if (count < 4) {
                                play()
                            }
                        }

                        override fun onFinish() {
                            currentExerciseIndex = currentExerciseIndex + 1
                            textViewExercise.text = currentExercise[currentExerciseIndex].toString()
                            speakOut(textViewExercise.text.toString())
                            exercise()
                        }
                    }
                    countDownTimerRest.start()
                } else {
                    if (workout) {
                        Toast.makeText(
                            this@ExerciseActivity,
                            "the Workout is over",
                            Toast.LENGTH_SHORT
                        ).show()
                        speakOut("The Workout is Over!")
                        Thread.sleep(1_000)
                        val overScreen =
                            Intent(this@ExerciseActivity, WorkoutEndActivity::class.java)
                        startActivity(overScreen)
                        finish()
                    }
                }
            }

            val countDownTimer =
                object : CountDownTimer(currentExerciseLength[currentExerciseIndex], 1000) {
                    override fun onTick(p0: Long) {
                        count = count - 1
                        textViewCountDown.text = count.toString()
                        if (count < 1) {
                            play()
                        }
                    }

                    override fun onFinish() {
                        exercisesCount = exercisesCount + 1
                        if (firstTime == false) {
                            currentExerciseIndex = currentExerciseIndex + 1
                            count = (currentExerciseLength[currentExerciseIndex].toInt()  / 1000)
                            textViewExercise.text = currentExercise[currentExerciseIndex].toString()
                            Glide.with(this@ExerciseActivity)
                                .asGif()
                                .load(currentExerciseDrawable[currentExerciseIndex]) // exerciseDrawable adalah URL gambar yang sudah Anda dapatkan dari API
                                .into(imageView2)
                            speakOut(textViewExercise.text.toString())
                            firstTime = true
                            exercise()
                        } else {
                            count = (currentExerciseLength[currentExerciseIndex].toInt()  / 1000)
                            rest()
                        }
                    }
                }
            countDownTimer.start()
        }
        exercise()

        switchSpeak.setOnClickListener {
            speak = switchSpeak.isChecked
        }

        switchSound.setOnClickListener {
            sound = switchSound.isChecked
        }
    }

    private fun onBackPressedDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.backdialog)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.setCanceledOnTouchOutside(false)
        val buttonYes: Button = customDialog.findViewById(R.id.buttonYes)
        val buttonNo: Button = customDialog.findViewById(R.id.buttonNo)

        buttonYes.setOnClickListener {
            sound = false
            speak = false
            workout = false
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        buttonNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onBackPressed(){
        onBackPressedDialog()

    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
        }
    }

    private fun speakOut (text: String){

        if (speak == true){
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }
    }
}
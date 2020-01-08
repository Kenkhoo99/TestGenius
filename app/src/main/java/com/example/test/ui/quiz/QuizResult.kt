package com.example.test.ui.quiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.test.R
import kotlinx.android.synthetic.main.quiz_result.*


class QuizResult : Activity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_result)

        val myIntent = intent
        var correct = myIntent.getIntExtra("score", 0)
        var totalQuestion = myIntent.getIntExtra("totalQuestion", 0)

        val totalQuestionText : TextView = findViewById(R.id.totalQuestionText)
        val correctText : TextView = findViewById(R.id.correctText)
        val wrongText : TextView = findViewById(R.id.wrongText)
        val scoreText : TextView = findViewById(R.id.scoreText)

        var score = correct.toDouble().div(totalQuestion).times(100)

        totalQuestionText.text = "Total Questions: " + totalQuestion
        correctText.text = "Correct Answers: " + correct
        wrongText.text = "Wrong Answers: " + totalQuestion.minus(correct)
        scoreText.text = "Your score: " + score + " %"

        when {
            score <= 25 -> textView2.text = "Nice try, try harder next time!"
            score <= 50 -> textView2.text = "Not bad, keep it up next time!"
            score <= 75 -> textView2.text = "Good!"
            score <= 100 -> textView2.text = "Excellent! You are a GENIUS!"
        }

        val backBut : Button = findViewById(R.id.back)
        val retryBut : Button = findViewById(R.id.retryBut)

        backBut.setOnClickListener {

            finish()
        }

        retryBut.setOnClickListener {

            finish()

            Toast.makeText(this,"Start!", Toast.LENGTH_SHORT).show()
            val myIntent = Intent(this, QuizQuestion::class.java)
            startActivity(myIntent)
        }



    }
}

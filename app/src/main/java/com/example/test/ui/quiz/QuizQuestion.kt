package com.example.test.ui.quiz

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil

import com.example.test.databinding.QuizQuestionBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.quiz_question.*
import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.example.test.Class.Quiz
import com.example.test.R
import com.google.firebase.database.*


class QuizQuestion : AppCompatActivity() {

    data class Question(
        val text: String,
        val answers: List<String>)

    lateinit var mDatabase : DatabaseReference

    private val questions: MutableList<Question> = mutableListOf(


        Question(
            text = "1 + 1 = ",
            answers = listOf("2", "1", "3", "4")
        ),
        Question(
            text = "2 + 2 = ",
            answers = listOf("4", "1", "2", "3")
        ),
        Question(
            text = "3 + 3 = ",
            answers = listOf("6", "1", "2", "3")
        ),
        Question(
            text = "4 + 4 = ",
            answers = listOf("8", "1", "2", "3")
        )
    )

    lateinit var questions2: MutableList<Question>

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = questions.size
    private lateinit var binding: QuizQuestionBinding
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.quiz_question)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24px);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setBackgroundColor(Color.parseColor("#ffff00"))


        readQuiz()






        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        var check = true

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->

            if (check) {

                val checkedId = binding.questionRadioGroup.checkedRadioButtonId
                // Do nothing if nothing is checked (id == -1)
                if (-1 != checkedId) {
                    var answerIndex = 0
                    when (checkedId) {
                        R.id.secondAnswerRadioButton -> answerIndex = 1
                        R.id.thirdAnswerRadioButton -> answerIndex = 2
                        R.id.fourthAnswerRadioButton -> answerIndex = 3
                    }

                    submitButton.text = "Continue"
                    check = false
                    // The first answer in the original question is always the correct one, so if our
                    // answer matches, we have the correct answer.
                    if (answers[answerIndex] == currentQuestion.answers[0]) {

                        Snackbar.make(findViewById(R.id.drawerLayout), "Correct answer! ", Snackbar.LENGTH_SHORT).show()
                        submitButton.setBackgroundColor(Color.GREEN)
                        score += 1

                    } else {

                        Snackbar.make(findViewById(R.id.drawerLayout), "Sorry, the answer is '" + currentQuestion.answers[0] + "'", Snackbar.LENGTH_SHORT).show()
                        submitButton.setBackgroundColor(Color.RED)

                    }
                }
            }

            else {

                questionIndex++

                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.invalidateAll()
                    submitButton.text = "Submit"
                    submitButton.setBackgroundColor(Color.parseColor("#60A9FF"))
                    check = true
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Quiz Completed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                    val myIntent = Intent(this, QuizResult::class.java)
                    myIntent.putExtra("score", score)
                    myIntent.putExtra("totalQuestion", questions.size)
                    startActivity(myIntent)


                }
            }
        }
    }

    private fun readQuiz() {

        mDatabase = FirebaseDatabase.getInstance().getReference("QuizDetail")
        val mDatabaseRef = mDatabase.child(intent.getStringExtra("id"))

        mDatabaseRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (h in p0.children) {

                        Log.d("hahqwea", h.child("question").toString())
//                        questions2.add(Quiz)

                    }


                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        supportActionBar?.title = "Question " + String.format("%d",questionIndex+1)
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@QuizQuestion,
            R.style.AlertDialogCustom
        )

        // Set the alert dialog title
        builder.setTitle("Exit?")

        // Display a message on alert dialog
        builder.setMessage("The quiz progress will be lost!")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Yes"){ _, _ ->
            // Do something when user press the positive button
            Toast.makeText(applicationContext,"Quiz cancelled.",Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }
}

package com.example.test.ui.quiz

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Class.Quiz
import com.example.test.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.quiz_list_layout.view.*


class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel =
            ViewModelProviders.of(this).get(QuizViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        mRecylerView = view.findViewById(R.id.quizRecycler)
        mRecylerView.setLayoutManager(LinearLayoutManager(context))
        mDatabase = FirebaseDatabase.getInstance().getReference("Quiz")

        logRecyclerView()

//
//        val b = view.q1 as Button
//        val buttonText = b.text.toString()
//
//        view.q1.setOnClickListener {
//
//            val builder = AlertDialog.Builder(activity, R.style.AlertDialogCustom)
//
//            builder.setTitle("Quiz Time")
//            builder.setMessage("Start $buttonText?")
//            builder.setPositiveButton("Yes"){dialog, which ->
//                val intent = Intent(activity, QuizQuestion::class.java)
//                startActivity(intent)
//                Toast.makeText(activity,"Start!", Toast.LENGTH_SHORT).show()
//            }
//            builder.setNeutralButton("Cancel"){_,_ ->
//
//            }
//
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
//        }

        return view
    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Quiz, QuizViewHolder>(

            Quiz::class.java,
            R.layout.quiz_list_layout,
            QuizViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : QuizViewHolder?, model: Quiz?, position:Int) {
                viewHolder?.itemView?.quizButton?.text = model?.title
                viewHolder?.itemView?.quizID?.text = model?.id

            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter
    }

    class QuizViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        init {

            itemView!!.setOnClickListener{

                val builder = AlertDialog.Builder(itemView.context, R.style.AlertDialogCustom)

                builder.setTitle("Quiz Time")
                builder.setMessage("Start " + itemView.quizButton.text +"?")
                builder.setPositiveButton("Yes"){dialog, which ->

                    val intent = Intent(itemView.context, QuizQuestion::class.java)
                    intent.putExtra("title", itemView.quizButton.text)
                    intent.putExtra("id", itemView.quizID.text)
                    itemView.context.startActivity(intent)
                    Toast.makeText(itemView.context,"Start!", Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("Cancel"){_,_ ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()



            }
        }
    }

}
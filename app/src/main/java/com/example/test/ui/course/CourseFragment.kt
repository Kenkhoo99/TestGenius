package com.example.test.ui.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Class.Course
import com.example.test.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.course_list_layout.view.*

class CourseFragment : Fragment() {

    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference
    private lateinit var courseViewModel: CourseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseViewModel =
            ViewModelProviders.of(this).get(CourseViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_course, container, false)


//        view.imageBtnComputerScience.setOnClickListener {
//            val intent = Intent(activity, CourseContent::class.java)
//            startActivity(intent)
//        }

        mRecylerView = view.findViewById(R.id.courseRecycler)
        mRecylerView.setLayoutManager(GridLayoutManager(context, 2))
        mDatabase = FirebaseDatabase.getInstance().getReference("Course")

        logRecyclerView()

        return view

    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Course, CourseViewHolder>(

            Course::class.java,
            R.layout.course_list_layout,
            CourseViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : CourseViewHolder?, model: Course?, position:Int) {
                viewHolder?.itemView?.test?.text = model?.title
                viewHolder?.itemView?.courseID?.text = model?.id

            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter
    }

    class CourseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {


        init {

            itemView!!.setOnClickListener{

                val intent = Intent(itemView.context, CourseContent::class.java)
                intent.putExtra("title", itemView.test.text)
                intent.putExtra("id", itemView.courseID.text)
                itemView.context.startActivity(intent)
            }
        }
    }

    private fun addCourse(title: String, desc: String) {
        val course = Course(title, desc)
        mDatabase.child(title).setValue(course)
    }
}
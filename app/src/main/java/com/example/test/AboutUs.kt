package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Class.Course
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.database.FirebaseRecyclerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.course_list_layout.view.*


class AboutUs : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_us)


    }



}

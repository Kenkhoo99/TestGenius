package com.example.test

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.example.test.Class.Tutor
import com.example.test.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_personal_tutor.*
import kotlinx.android.synthetic.main.activity_user__account.*

//Firebase Reference
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

//UI elements
private var userFirstName: EditText? = null
private var userLastName : EditText? = null
//private var userBirthday: EditText? = null
private var userAddress: EditText? = null
private var userCity: EditText? = null
private var userState: EditText? = null
private var userZip: EditText? = null
private var userCountry : EditText? = null


class PersonalTutor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_tutor)


        supportActionBar?.title = "Personal Info"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initialise()
    }

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        userFirstName = findViewById<View>(R.id.first_name) as EditText
        userLastName = findViewById<View>(R.id.last_name) as EditText
        //userBirthday = findViewById<View>(R.id.birthday) as EditText
        userAddress = findViewById<View>(R.id.address) as EditText
        userCity = findViewById<View>(R.id.city) as EditText
        userState = findViewById<View>(R.id.state) as EditText
        userZip = findViewById<View>(R.id.zip) as EditText
        userCountry = findViewById<View>(R.id.country) as EditText
        //userInst = findViewById<View>(R.id.ins_name) as EditText
    }

    override fun onStart() {
        super.onStart()

        val submit = findViewById<Button>(R.id.button_personal)

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        //Log.d("adad", mAuth!!.currentUser!!.email.toString())
        //mUserReference.child(mUser.uid).removeValue() --> delete account

        val dialog = setProgressDialog(this, "Updating Details")

        //Retrieve
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userAddress!!.setText(snapshot.child("address").value as? String)
                //userBirthday!!.setText(snapshot.child("birthDate").value as? String)
                userCity!!.setText(snapshot.child("city").value as? String)
                userCountry!!.setText(snapshot.child("country").value as? String)
                userFirstName!!.setText(snapshot.child("firstName").value as? String)
                userLastName!!.setText(snapshot.child("lastName").value as? String)
                userState!!.setText(snapshot.child("state").value as? String)
                userZip!!.setText(snapshot.child("zip").value as? String)


                //Edit and update
                submit.setOnClickListener {
                var tutor = Tutor(userAddress!!.text.toString(),
                    userCity!!.text.toString(), userCountry!!.text.toString(),
                    userFirstName!!.text.toString(),userLastName!!.text.toString(),
                    userState!!.text.toString(), userZip!!.text.toString())

                mUserReference.setValue(tutor)
                dialog.show()
                Toast.makeText(this@PersonalTutor, "Details Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    //Alternative Progress Dialog
    fun setProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
}

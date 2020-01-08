package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tutor_account.*


private val TAG = "UserAccount"

//Firebase Reference
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

private lateinit var auth: FirebaseAuth

//UI elements
private var textEmail: TextView? = null

class TutorAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_account)

        initialise()

        button_acc.setOnClickListener {
            changePassword()
        }
    }

    private fun initialise() {

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()

        textEmail = findViewById(R.id.current_email_user1)
    }

    private fun changePassword() {
        if (current_password.text.isNotEmpty() && new_password.text.isNotEmpty() && new_password_confirm.text.isNotEmpty()) {
            if(new_password.text.toString(). equals (new_password_confirm.text.toString()))
            {
                val user = auth.currentUser
                if(user != null && user.email !=null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, current_password.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT).show()
                                user?.updatePassword(new_password.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password changed.", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            startActivity(Intent(this,Login1::class.java))
                                            finish()

                                        }
                                    }

                            }else
                            {
                                Toast.makeText(this, "Re-authentication failed. Current Password is incorrect.", Toast.LENGTH_SHORT).show()
                            }
                        }

                }else
                {
                    startActivity(Intent(this,Login1::class.java))
                    finish()
                }

            }else
            {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
            }


        }else
        {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart(){
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabase!!.reference!!.child("Users").child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textEmail!!.text = snapshot.child("email").value as String

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

}

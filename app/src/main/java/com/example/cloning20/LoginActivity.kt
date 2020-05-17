package com.example.cloning20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    lateinit var emailtextview : TextView
    lateinit var passwardtextview : TextView
    lateinit var submitbutton : Button
    lateinit var mauth : FirebaseAuth
    lateinit var fbaseauthListner : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailtextview = findViewById(R.id.emailtext)
        passwardtextview = findViewById(R.id.passwardtext)
        submitbutton = findViewById(R.id.submitutton)
        mauth = FirebaseAuth.getInstance()
        fbaseauthListner = FirebaseAuth.AuthStateListener {
            var user= FirebaseAuth.getInstance().currentUser
            if (user != null) {
                var intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
        submitbutton.setOnClickListener {
            var email: String = emailtextview.text.toString()
            var passward: String = passwardtextview.text.toString()
            mauth.signInWithEmailAndPassword(email, passward)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Error", Toast.LENGTH_SHORT).show()
                    }


                }


        }
    }
    override fun onStart() {
        super.onStart()
        mauth.addAuthStateListener(fbaseauthListner)

    }

    override fun onStop() {
        super.onStop()
        mauth.removeAuthStateListener(fbaseauthListner)
    }
}

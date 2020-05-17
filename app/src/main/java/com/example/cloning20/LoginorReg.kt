package com.example.cloning20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginorReg : AppCompatActivity() {
    lateinit var loginbutton : Button
    lateinit var registrationbutton : Button


    // asking to the user Weather he wants to login or registration ........
    // if user wants to Login in then he Will Go to the Login activity Page.
    //if the user is a new user then he will further proceed with the Registraion activity...


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginor_reg)
        loginbutton=findViewById(R.id.login)
        registrationbutton=findViewById(R.id.registration)
        loginbutton.setOnClickListener {
            var intent : Intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
        registrationbutton.setOnClickListener{
            var intent : Intent = Intent(this,Registrationpage::class.java)
            startActivity(intent)
            finish()


        }
    }
}

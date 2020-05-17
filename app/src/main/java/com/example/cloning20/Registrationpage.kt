package com.example.cloning20

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Registrationpage : AppCompatActivity() {
    lateinit var emailtextview : TextView
    lateinit var passwardtextview : TextView
    lateinit var submitbutton : Button
    lateinit var mauth : FirebaseAuth
    lateinit var fbaseauthListner : FirebaseAuth.AuthStateListener
    lateinit var nametextview : TextView
    lateinit var sexradiogroup : RadioGroup
    lateinit var databaseReference: DatabaseReference
    lateinit var rootnode : FirebaseDatabase
    // This activity is created for the Regritation related information and Ui shown to the user..



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrationpage)
        emailtextview = findViewById(R.id.emailtext)
        passwardtextview = findViewById(R.id.passwardtext)
        submitbutton = findViewById(R.id.submitutton)
        nametextview=findViewById(R.id.nameteview)
        sexradiogroup=findViewById(R.id.sexradiogroup)
        mauth = FirebaseAuth.getInstance()
        fbaseauthListner = FirebaseAuth.AuthStateListener {
            var user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                var intent: Intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            }


        }
        submitbutton.setOnClickListener {
            var selectedId = sexradiogroup.checkedRadioButtonId
            var radiobuttonselected : RadioButton=findViewById(selectedId)




            val email: String = emailtextview.text.toString()
            val name : String= nametextview.text.toString()
            val passward: String = passwardtextview.text.toString()
            mauth.createUserWithEmailAndPassword(email, passward)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Error", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val userid : String =mauth.currentUser!!.uid

                        rootnode= FirebaseDatabase.getInstance()
                        databaseReference=rootnode.reference


                       val firebasename=databaseReference.child("Users").child(radiobuttonselected.text.toString()).child(userid).child("name")
                       firebasename.setValue(name)
                       val usersexlist=databaseReference.child("Users").child(radiobuttonselected.text.toString() + "Users")
                        usersexlist.setValue(userid.toString())




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

package com.example.cloning20

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import kotlin.collections.HashMap


class SettingsActivity : AppCompatActivity() {
    lateinit var settingsubmit :  Button
    lateinit var namec : EditText
    lateinit var mobileno : EditText
    lateinit var profileimage : ImageView
    lateinit var mauth : FirebaseAuth
    lateinit var settingreference : DatabaseReference
    lateinit var userId :  String
    lateinit var profilename :  String
    lateinit var profileImageUrl :  String
    lateinit var phone :String



    var resultUri : Uri? = null
    lateinit var mdata : FirebaseDatabase
    lateinit var firebasename :DatabaseReference
    lateinit var newUri : Uri
    var imageuri : Uri? = null
    lateinit var databaseReference: DatabaseReference
    lateinit var rootnode : FirebaseDatabase
    lateinit var sexradiogroup : RadioGroup
    lateinit var cyclicprogressbar : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsubmit=findViewById(R.id.settingsubmit)
        namec=findViewById(R.id.name)
        profileimage=findViewById(R.id.profileImage)
        mobileno=findViewById(R.id.phone)
        mauth=FirebaseAuth.getInstance()
        userId=mauth.currentUser!!.uid.toString()
        mdata= FirebaseDatabase.getInstance()
        phone= String()
        settingreference=mdata.reference
        cyclicprogressbar=findViewById(R.id.progressBar_cyclic)


        val user : FirebaseUser? =FirebaseAuth.getInstance().currentUser
        val postref = settingreference.child("Users").child("Male")
        cyclicprogressbar.visibility=View.INVISIBLE






        getuserInfo()
        settingsubmit.visibility=View.INVISIBLE



        settingsubmit.setOnClickListener{
            saveuserInformation()


            var intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        //For Getting the User Sex for Updating User Information
        // for Displaying the User Profile in
        rootnode= FirebaseDatabase.getInstance()
        databaseReference=rootnode.reference



        var firebasename=databaseReference.child("User Profile").child(userId).child("profileurl")
        firebasename.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val image_link =p0.value.toString()
                    Glide.with(this@SettingsActivity).load(image_link).into(profileimage)
                }
            }

        })



        profileimage.setOnClickListener{
            if (profileImage.drawable == null){
                var galleryintent : Intent = Intent(Intent.ACTION_PICK)
                galleryintent.type = "image/*"
                startActivityForResult(galleryintent, 1)
                cyclicprogressbar.visibility=View.VISIBLE

            }
            else{

                var galleryintent : Intent = Intent(Intent.ACTION_PICK)
                galleryintent.type = "image/*"
                startActivityForResult(galleryintent, 1)
                cyclicprogressbar.visibility=View.VISIBLE
            }


        }






    }




    // assigning the Xml Tools to the Map.
    private fun saveuserInformation(){
        profilename=namec.text.toString()
        phone=mobileno.text.toString()






        rootnode= FirebaseDatabase.getInstance()
        databaseReference=rootnode.reference
        var hashmap =  HashMap<String, Any>()
        hashmap["name"] = profilename
        firebasename.updateChildren(hashmap)




        if (resultUri != null){








        }else{
            finish()
        }
    }

    // Uploading a image to firebase Storage and Downloading it.



    // This function is Created for User to edit her profile info and we can Update it to the firebase
    //and the Updated User Info is View by all the Users....
    private fun getuserInfo(){

        val user : FirebaseUser? =FirebaseAuth.getInstance().currentUser
        var usercurrentuid : String=user!!.uid

        val checkingusers : DatabaseReference=settingreference.child("Users").child("Male")

        checkingusers.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(usercurrentuid).exists()){
                    firebasename=settingreference.child("Users").child("Male").child(userId)
                    firebasename.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()  && p0.childrenCount > 0){
                                var map : Map<String,Objects> = p0.value as Map<String, Objects>
                                if(map["name"] != null){
                                    profilename=map["name"].toString()
                                    namec.setText(profilename)
                                }
                                if(map["phone"] != null){
                                    profilename=map["phone"].toString()
                                    mobileno.setText(phone)
                                }



                            }

                        }

                    })


                }else{
                    firebasename=settingreference.child("Users").child("Female").child(userId)
                    firebasename.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()  && p0.childrenCount > 0){
                                var map : Map<String,Objects> = p0.value as Map<String, Objects>
                                if(map["name"] != null){
                                    profilename=map["name"].toString()
                                    namec.setText(profilename)
                                }





                            }

                        }

                    })

                }
            }

        })







    }


    // Here an Intent is Created for the User to her Profile Picture from the Gallery  App.
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode == Activity.RESULT_OK){

            val file_uri = data?.data






            uploadImageandsaveuri(file_uri)
            //The Image Uri is Linked to Resultant uri so that the image is set to Updated.

        }
    }
    private fun uploadImageandsaveuri(fileUri :Uri?){
        var filepath : FirebaseStorage=FirebaseStorage.getInstance()
        var filepathref : StorageReference=filepath.reference
        var filepathchild=filepathref.child("profileImages").child(userId)

        if (fileUri != null) {
            val user : FirebaseUser? =FirebaseAuth.getInstance().currentUser
            var usercurrentuid : String=user!!.uid
            val fileName = usercurrentuid +".jpg"

            val database = FirebaseDatabase.getInstance()
            val refStorage = FirebaseStorage.getInstance()
            val refredf =refStorage.reference
            val pp=refredf.child(" Profile Images/$fileName")

            pp.putFile(fileUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        val imageUrl = it.toString()
                        rootnode= FirebaseDatabase.getInstance()
                        databaseReference=rootnode.reference



                        var firebasename=databaseReference.child("User Profile").child(usercurrentuid).child("profileurl")
                        firebasename.setValue(imageUrl)
                    }
                    settingsubmit.visibility=View.VISIBLE
                    cyclicprogressbar.visibility=View.INVISIBLE
                }

                .addOnFailureListener { e ->
                    print(e.message)
                }
        }
    }



}

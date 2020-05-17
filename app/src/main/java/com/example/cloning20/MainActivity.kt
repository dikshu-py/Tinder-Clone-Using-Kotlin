package com.example.cloning20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var card_data : Cards
    lateinit var usersDb : FirebaseDatabase
    lateinit var usersreference : DatabaseReference
    lateinit var currentUID : String
    lateinit var databaseReference: DatabaseReference


    lateinit var arrayAdapter: arrayAdapter
    lateinit var listView: ListView
    lateinit var rowitems : ArrayList<Cards>
    var i = 0
    lateinit var flingContainer: SwipeFlingAdapterView
    lateinit var signout : Button
    lateinit var setting_button : Button
    lateinit var rootnode : FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // A Array List for Storage and Retriving user Name And Data.
        rowitems = ArrayList<Cards>()
        setting_button=findViewById(R.id.settingbutton)
        var  userSEx : String? = intent.getStringExtra("user_sex")
        setting_button.setOnClickListener{
            gotoSettings()
        }

        // An Array Adapter for the ArrayList Created.
        arrayAdapter=arrayAdapter(this, R.layout.item, rowitems)


        // CommandLine For showing the Swipe Card Liberery.
        flingContainer=findViewById(R.id.frame)

        usersDb=FirebaseDatabase.getInstance()
        usersreference=usersDb.reference
        //var userDB = usersreference.child("Users")
        var mAuth =FirebaseAuth.getInstance()
        currentUID=mAuth.uid.toString()



        // Checking Weather the Our conatiners are Working.


        flingContainer.adapter=arrayAdapter
        checkUsersex()


        // Setting OnClickListner Command for the card view When the User Clicks on it.
        flingContainer!!.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!")
                rowitems.removeAt(0);
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                var userDB = usersreference.child("Users")
                var obj : Cards = p0 as Cards
                var userId :String = p0.getuserId()
                var newvalue = usersreference.child("Users").child(notUserSex).child(userId).child("Connections").child("nope").child(currentUID)
                newvalue.setValue(true)
                Toast.makeText(this@MainActivity,"nope",Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(p0: Any?) {
                var userDB = usersreference.child("Users")
                var obj : Cards = p0 as Cards
                var userId :String = p0.getuserId()
                var newvalue = usersreference.child("Users").child(notUserSex).child(userId).child("Connections").child("Yeps").child(currentUID)
                newvalue.setValue(true)
                isConnectionMatched(userId)
                Toast.makeText(this@MainActivity,"Yeps",Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {

            }

            override fun onScroll(p0: Float) {

            }


        })
        flingContainer.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this@MainActivity,"Clicked",Toast.LENGTH_SHORT).show() }


        // Activating the SignOut Button in the MainActivity.
        signout=findViewById(R.id.signout)


        //Giving onClickListner Command to the SigOut button.
        signout.setOnClickListener{
            logoutUser()

        }













        }
    //Function for going to Settings Page

    // Command For the to Logout the System.
    private fun logoutUser(){
        var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        var intent : Intent = Intent(this,LoginorReg::class.java)
        startActivity(intent)
        finish()
    }

    // A Variable to getting the user Type Sex and Opposite type.
    lateinit var userSex : String
    lateinit var notUserSex :String


    // A Firebase fun for getting the list of male and female users.
    private fun checkUsersex(){

        val user : FirebaseUser? =FirebaseAuth.getInstance().currentUser
        val maleDb : FirebaseDatabase =FirebaseDatabase.getInstance()
        val databaseReference1 : DatabaseReference=maleDb.reference
        val maleDb1=databaseReference1.child("Users").child("Male")
        maleDb1.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if (user != null) {
                    if (p0.key.equals(user.uid)){
                        userSex = "Male"
                        notUserSex ="Female"
                        getoppositeSexUsers()
                        copyandpasteprofileUrl()
                    }
                }
                if(user == null){
                    Toast.makeText(this@MainActivity,"Empty",Toast.LENGTH_SHORT).show()

                }


            }



            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })





        val oppositeSexDb :FirebaseDatabase =FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference=oppositeSexDb.reference
        val femaleDb=databaseReference.child("Users").child("Female")
        femaleDb.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if (user != null) {
                    if (p0.key.equals(user.uid)){
                        userSex = "Female"
                        notUserSex ="Male"
                        getoppositeSexUsers()
                        copyandpasteprofileUrl()
                    }
                }

            }



            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })


    }
    private fun isConnectionMatched(userId : String){
        var connectionmatchingDb =usersreference.child("Users").child(userSex).child("Connections").child("Yeps").child(userId)
        connectionmatchingDb.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    Toast.makeText(this@MainActivity,"New Connection",Toast.LENGTH_SHORT).show()
                    var a =usersreference.child("Users").child(p0.key.toString()).child("Connections").child("matches").child(currentUID)
                    var aa = usersreference.child(notUserSex).child(currentUID).child("name")
                    a.setValue(aa.key.toString())
                    var b =usersreference.child(userSex).child(currentUID).child("Connections").child(p0.key.toString())
                    b.setValue(true)
                }

            }

        })






    }


    //Command for the user to show Opposite Ones in the CardList View.
    private fun getoppositeSexUsers(){



        val user : FirebaseUser? =FirebaseAuth.getInstance().currentUser
        val oppositeSexDb :FirebaseDatabase =FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference=oppositeSexDb.reference
        val firebb =databaseReference.child("User Profile").child(currentUID)


        val firebasename = databaseReference.child("Users").child(notUserSex)


        firebasename.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (p0.exists()  && !p0.child("Connections").child("nope").hasChild(currentUID) && !p0.child("Connections").child("Yeps").hasChild(currentUID)){
                    if (p0.hasChild("profileurl")){
                        rowitems.add(Cards(p0.child("name").value.toString(),p0.key.toString(),p0.child("profileurl").value.toString()))

                        arrayAdapter.notifyDataSetChanged()

                    }else{
                        rowitems.add(Cards(p0.child("name").value.toString(),p0.key.toString(),"https://sd.keepcalms.com/i/no-profile-picture-but-i-swear-i-am-very-ugly-1.png"))

                        arrayAdapter.notifyDataSetChanged()

                    }




                }


            }



            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })

    }
    private fun gotoSettings(){
        val intent : Intent = Intent(this,SettingsActivity::class.java)

        startActivity(intent)


    }
    fun copyandpasteprofileUrl(){
        rootnode= FirebaseDatabase.getInstance()
        databaseReference=rootnode.reference


        val firebb=databaseReference.child("Users").child(userSex).child(currentUID).child("profileurl")
        val firebasename=databaseReference.child("User Profile").child(currentUID).child("profileurl")
        firebasename.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val image_link =p0.value.toString()
                    firebb.setValue(image_link)


                }
                else{
                    firebb.setValue("https://firebasestorage.googleapis.com/v0/b/tinder-clone-2cee7.appspot.com/o/%20Profile%20Images%2FDZAYJ9qpCsSOpLXEMkVXYtD2I1G3.jpg?alt=media&token=2a88e809-38df-41d5-ae42-7a2279e43883")
                }
            }

        })

    }



}
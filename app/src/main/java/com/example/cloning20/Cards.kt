package com.example.cloning20

class Cards(var uname : String, var userId : String , var profileImageUrl :String) {


    fun Cards (userId : String ,uname : String ){
        this.userId=userId
        this.uname=uname
    }
    fun getuserId() : String{
        return userId
    }
    fun getusername() : String{
        return uname
    }
    fun setusername(userID :String) {
        this.uname=uname
    }
    fun setuserId(name :String){
         this.userId=userId
    }
    fun getprofileImageUrl() :String{
        return profileImageUrl
    }
    fun setprofileImageUrl(profileImageUrl: String){
        this.profileImageUrl=profileImageUrl
    }





}
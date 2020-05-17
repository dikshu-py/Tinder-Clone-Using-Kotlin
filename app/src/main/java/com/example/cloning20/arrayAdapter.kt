package com.example.cloning20

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

// This is the array adapter That Provides us the username her profile picture including her userId.
//This array Adapter is Linked with Cards class that Stores her Variables.

class arrayAdapter(var mctx : Context, var resources :Int, var items : List<Cards>): ArrayAdapter<Cards>(mctx,resources,items) {
    @SuppressLint("ViewHolder")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView=LayoutInflater.from(context).inflate(R.layout.item,parent,false)

        val mItem:Cards=items[position]
        var name : TextView=convertView.findViewById(R.id.username)
        var image : ImageView=convertView.findViewById(R.id.userprofile)
        name.text=mItem.getusername()
        Glide.with(context).load(mItem.getprofileImageUrl()).into(image)









        return  convertView

    }
}
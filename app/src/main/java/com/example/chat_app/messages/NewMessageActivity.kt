package com.example.chat_app.messages

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app.R
import com.example.chat_app.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class NewMessageActivity : AppCompatActivity() {
    private val adapter  = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"


       // shimmerLayout.startShimmerAnimation()

        Handler().postDelayed( {
            shimmerLayout.stopShimmerAnimation()
            shimmerLayout.visibility = View.GONE


        },5000)

        recyclerview_newmessage.adapter = adapter
        recyclerview_newmessage.layoutManager= LinearLayoutManager(this)
        fetchUsers()

        // val adapter = GroupAdapter<GroupieViewHolder>()

    }
    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
            //  adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
             adapter.setOnItemClickListener { item, view ->
                 val userItem = item as UserItem
                 val intent = Intent(view.context,ChatLogActivity::class.java)
                 //intent.putExtra(USER_KEY,userItem.user.username)
                 intent.putExtra(USER_KEY,userItem.user)
                 startActivity(intent)

                 finish()
             }

                // recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    class UserItem(val user: User) : Item<GroupieViewHolder>() {

        override fun getLayout(): Int {
            return R.layout.user_row_new_message
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.username_textview_new_message.text = user.username

            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
        }
    }
}
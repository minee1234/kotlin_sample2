package com.minee.kotlin_sample2.part3chapter05

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.minee.kotlin_sample2.R

class P3C05MatchedUserActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference
    private val adapter = MatchedUserAdapter()
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c05_matched_user)

        userDB = Firebase.database.reference.child(DBKey.USERS)

        initMatchedUserRecyclerView()
        getMatchedUsers()
        getMatchUsers()
    }

    private fun initMatchedUserRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.matchedUserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getMatchedUsers() {
        val matchedUsersDB = userDB.child(getCurrentUserID())
            .child(DBKey.LIKED_BY)
            .child(DBKey.MATCH)

        matchedUsersDB.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (children in snapshot.children) {
                    Log.d("minee", children.toString())
                    if (children.key?.isNotEmpty() == true) {
                        getUserByKey(children.key.orEmpty())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getMatchUsers() {
        val matchedDB = userDB.child(getCurrentUserID())
            .child(DBKey.LIKED_BY)
            .child(DBKey.MATCH)

        matchedDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getUserByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getUserByKey(userId: String) {
        userDB.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItems.add(CardItem(userId, snapshot.child(DBKey.NAME).value.toString()))
                adapter.submitList(cardItems)
                //adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }
}
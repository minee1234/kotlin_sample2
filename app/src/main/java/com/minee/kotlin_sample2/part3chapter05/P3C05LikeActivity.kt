package com.minee.kotlin_sample2.part3chapter05

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.minee.kotlin_sample2.R
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class P3C05LikeActivity : AppCompatActivity(), CardStackListener {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference

    private val adapter = CardItemAdapter()
    private val cardItems = mutableListOf<CardItem>()

    private val manager by lazy {
        CardStackLayoutManager(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c05_like)

        userDB = Firebase.database.reference.child(DBKey.USERS)
        val currentUserDB = userDB.child(getCurrentUserID())
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(DBKey.NAME).value == null) {
                    showNameInputPopup()
                    return
                }
                getUnSelectedUsers()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        initCardStackView()
        initSignOutButton()
        initMatchedListButton()
    }

    private fun initCardStackView() {
        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }

    private fun initSignOutButton() {
        val singOutButton = findViewById<Button>(R.id.signOutButton)
        singOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, P3C05MainActivity::class.java))
            finish()
        }
    }

    private fun initMatchedListButton() {
        val matchedListButton = findViewById<Button>(R.id.matchListButton)
        matchedListButton.setOnClickListener {
            startActivity(Intent(this, P3C05MatchedUserActivity::class.java))
        }
    }

    private fun getUnSelectedUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child(DBKey.USER_ID).value != getCurrentUserID()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.LIKE)
                        .hasChild(getCurrentUserID()).not()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.DIS_LIKE)
                        .hasChild(getCurrentUserID()).not()
                ) {
                    val userId = snapshot.child(DBKey.USER_ID).value.toString()
                    var name = "undecided"
                    if (snapshot.child(DBKey.NAME).value != null) {
                        name = snapshot.child(DBKey.NAME).value.toString()
                    }

                    cardItems.add(CardItem(userId, name))
                    adapter.submitList(cardItems)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                cardItems.find { it.userId == snapshot.key }?.let {
                    it.name = snapshot.child(DBKey.NAME).value.toString()
                }
                adapter.submitList(cardItems)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showNameInputPopup() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("이름을 입력해주세요")
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    saveUserName(editText.text.toString())
                }
            }
            .setCancelable(false)
            .setView(editText)
            .show()
    }

    private fun saveUserName(name: String) {
        val userId = getCurrentUserID()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user[DBKey.USER_ID] = userId
        user[DBKey.NAME] = name
        currentUserDB.updateChildren(user)

        getUnSelectedUsers()
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }

    private fun like() {
        val card = cardItems[manager.topPosition - 1]
        cardItems.removeFirst()

        userDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.LIKE)
            .child(getCurrentUserID())
            .setValue(true)

        matchUser(card.userId)

        Toast.makeText(this, "${card.name}님을 Like 하셨습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun disLike() {
        val card = cardItems[manager.topPosition - 1]
        cardItems.removeFirst()

        userDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.DIS_LIKE)
            .child(getCurrentUserID())
            .setValue(true)

        Toast.makeText(this, "${card.name}님을 DisLike 하셨습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun matchUser(otherUserId: String) {
        val otherUserDB = userDB.child(getCurrentUserID()).child(DBKey.LIKED_BY).child(DBKey.LIKE)
            .child(otherUserId)
        otherUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    userDB.child(getCurrentUserID())
                        .child(DBKey.LIKED_BY)
                        .child(DBKey.MATCH)
                        .child(otherUserId)
                        .setValue(true)

                    userDB.child(otherUserId)
                        .child(DBKey.LIKED_BY)
                        .child(DBKey.MATCH)
                        .child(getCurrentUserID())
                        .setValue(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> disLike()
            Direction.Right -> like()
            else -> {
            }
        }
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardDisappeared(view: View?, position: Int) {}
}
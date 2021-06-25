package com.minee.kotlin_sample2.part3chapter06.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.databinding.FragmentChatlistBinding
import com.minee.kotlin_sample2.part3chapter06.DBKey.Companion.CHILD_CHAT
import com.minee.kotlin_sample2.part3chapter06.DBKey.Companion.DB_USERS
import com.minee.kotlin_sample2.part3chapter06.chatdetail.ChatRoomActivity

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private var binding: FragmentChatlistBinding? = null
    private val chatRoomList = mutableListOf<ChatListItem>()
    private lateinit var adapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        adapter = ChatListAdapter(onItemClicked = { chatListItem ->
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatListItem.key)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = adapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if (auth.currentUser == null) {
            return
        }

        val chatDB =
            Firebase.database.reference.child(DB_USERS)
                .child(auth.currentUser!!.uid).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { it ->
                    val model = it.getValue(ChatListItem::class.java)
                    model?.let { chatListItem ->
                        chatRoomList.add(chatListItem)
                    }
                }
                adapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }
}
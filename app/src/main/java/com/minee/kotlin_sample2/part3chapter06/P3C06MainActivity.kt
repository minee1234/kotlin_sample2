package com.minee.kotlin_sample2.part3chapter06

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.part3chapter06.chatlist.ChatListFragment
import com.minee.kotlin_sample2.part3chapter06.home.HomeFragment
import com.minee.kotlin_sample2.part3chapter06.mypage.MypageFragment

class P3C06MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c06_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MypageFragment()
        replaceFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}
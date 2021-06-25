package com.minee.kotlin_sample2.part3chapter06.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.databinding.FragmentMypageBinding

class MypageFragment : Fragment(R.layout.fragment_mypage) {

    private var binding: FragmentMypageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentMyPageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMyPageBinding

        fragmentMyPageBinding.signUpButton.setOnClickListener {
            binding?.let {
                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "회원가입에 성공하였습니다. 로그인 버튼을 눌러 주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "회원가입에 실패하였습니다. 이미 가입한 이메일일수 있습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        fragmentMyPageBinding.signInOutButton.setOnClickListener {
            binding?.let {
                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()

                if (auth.currentUser == null) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(
                                    context,
                                    "로그인에 실패하였습니다. 이메일, 패스워드를 확인해 주세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    auth.signOut()

                    it.emailEditText.text.clear()
                    it.emailEditText.isEnabled = true
                    it.passwordEditText.text.clear()
                    it.passwordEditText.isEnabled = true

                    it.signUpButton.isEnabled = false
                    it.signInOutButton.isEnabled = false
                    it.signInOutButton.text = "로그인"
                }
            }
        }

        fragmentMyPageBinding.emailEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }

        fragmentMyPageBinding.passwordEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            binding?.let {
                it.emailEditText.text.clear()
                it.passwordEditText.text.clear()
                it.emailEditText.isEnabled = true
                it.passwordEditText.isEnabled = true

                it.signUpButton.isEnabled = false
                it.signInOutButton.isEnabled = false
                it.signInOutButton.text = "로그인"
            }
        } else {
            binding?.let {
                it.emailEditText.setText(auth.currentUser!!.email)
                it.passwordEditText.setText("********")
                it.emailEditText.isEnabled = false
                it.passwordEditText.isEnabled = false
                it.signUpButton.isEnabled = false
                it.signInOutButton.isEnabled = true
                it.signInOutButton.text = "로그아웃"
            }
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(
                context,
                "로그인에 실패하였습니다. 다시 시도해 주세요",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        binding?.let {
            it.emailEditText.isEnabled = false
            it.passwordEditText.isEnabled = false
            it.signUpButton.isEnabled = false
            it.signInOutButton.isEnabled = true
            it.signInOutButton.text = "로그아웃"
        }

    }
}
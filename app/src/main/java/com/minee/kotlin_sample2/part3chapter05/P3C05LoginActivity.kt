package com.minee.kotlin_sample2.part3chapter05

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.minee.kotlin_sample2.R

class P3C05LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c05_login)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initEmailAndPasswordEditText()
        initLoginButton()
        initSignUpButton()
        initFacebookLoginButton()
    }

    private fun initEmailAndPasswordEditText() {
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        emailEditText.addTextChangedListener {
            val enabled = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enabled
            signUpButton.isEnabled = enabled
        }
        passwordEditText.addTextChangedListener {
            val enabled = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enabled
            signUpButton.isEnabled = enabled
        }
    }

    private fun initFacebookLoginButton() {
        val facebookLoginButton = findViewById<LoginButton>(R.id.facebookLoginButton)

        facebookLoginButton.setPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this@P3C05LoginActivity) { task ->
                            if (task.isSuccessful) {
                                handleSuccessLogin()
                            } else {
                                Toast.makeText(
                                    this@P3C05LoginActivity,
                                    "페이스북 로그인이 실패하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        this@P3C05LoginActivity,
                        "페이스북 로그인이 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                    } else {
                        Toast.makeText(
                            this,
                            "로그인에 실패하였습니다. 이메일과 패스워드를 확인해 주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initSignUpButton() {
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입에 성공하였습니다. 로그인을 해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "이미가입한 이메일 이거나, 회원가입에 실패하였습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun getInputPassword() = findViewById<EditText>(R.id.passwordEditText).text.toString()

    private fun getInputEmail() = findViewById<EditText>(R.id.emailEditText).text.toString()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser!!.uid
        val currentUserDB = Firebase.database.reference.child(DBKey.USERS).child(userId)
        val user = mutableMapOf<String, Any>()
        user[DBKey.USER_ID] = userId
        currentUserDB.updateChildren(user)

        finish()
    }
}
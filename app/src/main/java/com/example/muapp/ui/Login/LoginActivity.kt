package com.example.muapp.ui.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muapp.HomeActivity
import com.example.muapp.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var auth= Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { // 회원가입 버튼 눌렀을때
            val intent = Intent(this, RegisterActivity::class.java) // 회원가입 화면으로 이동
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            //auth에서 있는지 없는지 확인 필요 ,  만약 로그인 통과 -> 액티비티 홈으로 이동
            val userId = binding.etUserName.text.toString()
            val userPassword = binding.etUserPassword.text.toString()

            LoginAuth(userId,userPassword)
        }

    }
    private fun LoginAuth (userId : String, userPassword: String) {
        if(userId.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(this, "빈 값을 입력하셨습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(userId,userPassword).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(this, "로그인 성공!!.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
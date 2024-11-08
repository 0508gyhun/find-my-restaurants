package com.example.muapp.ui.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val  auth =  FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterSubmit.setOnClickListener {

            val userId = binding.etUserName.text.toString()
            val userPassword = binding.etUserPassword.text.toString()
            auth
            RegisterAuth(userId, userPassword)
            //auth에 등록되고

        }
    }

    private fun RegisterAuth (userId : String, userPassword: String) {
        if(userId.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(this, "빈 값을 입력하셨습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(userId,userPassword).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(this, "회원가입 완료!!.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
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
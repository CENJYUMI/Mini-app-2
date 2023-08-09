package com.data.miniapp22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.data.miniapp22.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.LoginBtn.setOnClickListener {
                val username = binding.etrUsername.text.toString()
                val password = binding.etrPassword.text.toString()

                val found = databaseHelper.checkUserCredentials(username,password)
                if (found) {
                    val intent = Intent (this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else
                    Toast.makeText(applicationContext, "Wrong username and password!", Toast.LENGTH_LONG).show()
        }

        binding.clkSignUp.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
package com.data.miniapp22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.data.miniapp22.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.SignUpBtn.setOnClickListener {
            val username = binding.getUsername.text.toString().trim()
            val password = binding.getPassword.text.toString().trim()
            val confirmPw = binding.getConfirmPw.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty() && confirmPw.isNotEmpty()) {
                if (isPasswordValid(password)) { // Validate password first
                    if (databaseHelper.isUsernameAvailable(username)) {
                        val user = User(0, username, password, confirmPw)
                        addUser(user)

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

                        // Clear the input fields
                        binding.getUsername.text?.clear()
                        binding.getPassword.text?.clear()
                        binding.getConfirmPw.text?.clear()

                        // Refresh the displayed data
                        getUser()
                    } else {
                        Toast.makeText(this, "Username already exists. Please choose a different one.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Password must have at least one letter, one number, and be 8 to 20 characters long.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please complete all required information", Toast.LENGTH_LONG).show()
            }
        }

        binding.clkLogin.setOnClickListener{
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)

            startActivity(intent)
            Toast.makeText(this, "Login", Toast.LENGTH_LONG).show()
        }

    }
    private fun isUsernameAvailable(username: String): Boolean {
        val sql = "SELECT COUNT(*) FROM user WHERE username = ?"
        val stmt = databaseHelper.readableDatabase.compileStatement(sql)
        stmt.bindString(1, username)
        val count = stmt.simpleQueryForLong()
        stmt.close()
        return count == 0L
    }
    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$".toRegex()
        return passwordPattern.matches(password)
    }


    private fun getUser(): MutableList<User>{
        return databaseHelper.getAllUsers()
    }

    private fun addUser (user: User){
        databaseHelper.insertUsers(user)

    }

    private fun updateUser (user: User){
        databaseHelper.updateUserData(user)
        getUser()
        Toast.makeText(applicationContext, "User Updated!", Toast.LENGTH_LONG).show()
    }
    private fun deleteUser (userID: Int){
        databaseHelper.deleteUserData(userID)
        getUser()
        Toast.makeText(applicationContext, "User Deleted!", Toast.LENGTH_LONG).show()
    }

}



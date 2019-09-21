package com.anything.chattingapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.anything.chattingapp.R
import com.anything.chattingapp.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.regex.Pattern.compile


class LoginActivity : AppCompatActivity() {

    private val _vm: LoginViewModel by viewModel()
    private var formStatus = FormState()
    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _vm._showProgressBarState.observe(this@LoginActivity, Observer {
            if (it) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE
            }
        })

        _vm._authStatus
            .observe(this@LoginActivity, Observer {
                val i = Intent(this@LoginActivity, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                when {
                    it.result == Result.LOGIN_SUCCESS -> startActivity(i)
                    it.result == Result.LOGIN_FAILED -> showToast(it)
                    it.result == Result.REGISTRATION_SUCCESS -> startActivity(i)
                    it.result == Result.REGISTRATION_FAILED -> showToast(it)
                }
            })

        _vm._formState.observe(this@LoginActivity, Observer {
            login.isEnabled = (it.isEmailValid == true && it.isPasswordValid == true)
            formStatus = it
        })

        username.addTextChangedListener {
            if (Patterns.EMAIL_ADDRESS.matcher(it!!).matches() && it.isNotEmpty()) {
                formStatus.isEmailValid = true
                username_layout.isErrorEnabled = false
            } else {
                username_layout.error = "Invalid Email"
                formStatus.isEmailValid = false
            }
            _vm.updateFormStatus(formStatus)
        }

        password.addTextChangedListener {
            if (it!!.length > 5 && it.isNotEmpty()) {
                formStatus.isPasswordValid = true
                password_layout.isErrorEnabled = false
            } else {
                password_layout.error = "Password must more than 5"
                formStatus.isPasswordValid = false
            }
            _vm.updateFormStatus(formStatus)
        }

        login.setOnClickListener {
            _vm.login(username.text.toString(), password.text.toString())
        }


    }

    override fun onStart() {
        super.onStart()
        if (_vm.currentUser() != null) {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }


    private fun showToast(result: AuthResult) {
        val view = findViewById<View>(R.id.container)
        Snackbar.make(view, result.message!!, Snackbar.LENGTH_LONG)
            .setAction("OK") {
                _vm._authStatus.apply { value = AuthResult(Result.DEFAULT, "") }
            }
            .show()
    }

}

package com.anything.chattingapp.view.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anything.chattingapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginViewModel(private val fba: FirebaseAuth, private val ref: DatabaseReference) :
    ViewModel() {

    val _showProgressBarState = MutableLiveData<Boolean>().apply { value = false }
    val _authStatus = MutableLiveData<AuthResult>().apply { value = AuthResult(Result.DEFAULT, "") }
    val _formState = MutableLiveData<FormState>().apply {
        value = FormState(
            isEmailValid = false,
            isPasswordValid = false
        )
    }


    fun login(email: String, password: String) {
        _showProgressBarState.postValue(true)
        fba.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showProgressBarState.postValue(false)
                if (task.isSuccessful) {
                    _authStatus.postValue(AuthResult(Result.LOGIN_SUCCESS, "Login Successful!"))
                } else {
                    _authStatus.postValue(AuthResult(Result.LOGIN_FAILED, "Login Failed!"))
                    register(email, password)
                }
            }
    }

    fun register(email: String, password: String) {
        _showProgressBarState.postValue(true)
        fba.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showProgressBarState.postValue(false)
                if (task.isSuccessful) {
                    writeUserDetails()
                } else {
                    _authStatus.postValue(
                        AuthResult(
                            Result.REGISTRATION_FAILED,
                            "Registration Failed!"
                        )
                    )
                }
            }
    }


    private fun writeUserDetails() {
        _showProgressBarState.postValue(true)
        val u = currentUser()
        val user = User(username = u?.displayName, email = u?.email, key = u?.uid, userId = u?.uid)
        ref.child("users").child(u?.uid!!).setValue(user).addOnCompleteListener { task ->
            _showProgressBarState.postValue(false)
            if (task.isSuccessful) {
                _authStatus.postValue(
                    AuthResult(
                        Result.REGISTRATION_SUCCESS,
                        "Registration Successful!"
                    )
                )
            } else {
                AuthResult(
                    Result.REGISTRATION_FAILED,
                    "Error Writing User detials"
                )
            }
        }
    }

    fun currentUser() = fba.currentUser

    fun updateFormStatus(formState: FormState) {
        _formState.postValue(formState)
    }
}

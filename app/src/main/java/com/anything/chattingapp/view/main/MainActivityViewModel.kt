package com.anything.chattingapp.view.main

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivityViewModel(private val fba: FirebaseAuth) : ViewModel() {

    fun getCurrentUser() = fba.currentUser

    fun logOut() {
        fba.signOut()
    }

}
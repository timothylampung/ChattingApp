package com.anything.chattingapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    @Exclude
    var key: String? = null,
    var username: String? = null,
    var email: String? = null,
    var userId: String? = null,
    var contactNo: String? = null,
    var profilePicture: String? = null,
    var status: String? = "Hi! I am using chat app!"
)
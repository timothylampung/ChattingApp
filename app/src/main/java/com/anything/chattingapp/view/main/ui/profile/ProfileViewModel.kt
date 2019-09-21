package com.anything.chattingapp.view.main.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anything.chattingapp.data.User
import com.anything.chattingapp.view.auth.AuthResult
import com.anything.chattingapp.view.auth.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ProfileViewModel(
    private val fbs: FirebaseStorage,
    private val fba: FirebaseAuth,
    private val ref: DatabaseReference
) :
    ViewModel() {

    private val _tempPro = File.createTempFile("profile_picture", "jpeg")
    val _userProfile = MutableLiveData<User>()
    val _showProgressBarState = MutableLiveData<Boolean>().apply { value = false }
    val _updatedState = MutableLiveData<EnumUpdateState>().apply { value = EnumUpdateState.DEFAULT }

    private val storageReference = this.fbs.reference

    val _storageState = MutableLiveData<StorageState>().apply {
        value =
            StorageState(StorageStatus.DEFAULT, _tempPro, null)
    }

    init {
        getUserDetail()
        downloadProfilePicture()
    }

     fun signOut() {
        fba.signOut()
    }

    private fun getCurrentUser() = fba.currentUser

    fun writeUserDetails(user: User) {
        _showProgressBarState.postValue(true)
        ref.child("users").child(getCurrentUser()?.uid!!).setValue(user)
            .addOnCompleteListener { task ->
                _showProgressBarState.postValue(false)
                if (task.isSuccessful) {
                    _updatedState.postValue(EnumUpdateState.UPDATED)
                } else {
                    _updatedState.postValue(EnumUpdateState.FAILED)
                }
            }
    }


    private fun getUserDetail() {
        _showProgressBarState.postValue(true)
        ref.child("users")
            .child(getCurrentUser()?.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var value = p0.getValue(User::class.java)
                    _userProfile.postValue(value)
                }
            })
    }


    fun uploadProfilePicture(uri: Uri?) {
        _showProgressBarState.postValue(true)
        if (uri != null) {
            val riversRef = storageReference.child(fba.currentUser?.uid + "/profile_picture.jpeg")
            riversRef.putFile(uri)
                .addOnSuccessListener {
                    _showProgressBarState.postValue(false)
                    downloadProfilePicture()
                }
                .addOnFailureListener {
                    _showProgressBarState.postValue(false)
                }
        }
    }

    fun downloadProfilePicture() {
        _showProgressBarState.postValue(true)
        storageReference.child(fba.currentUser?.uid + "/" + "profile_picture.jpeg")
            .getFile(_tempPro)
            .addOnSuccessListener {
                _showProgressBarState.postValue(false)
                _storageState.postValue(
                    StorageState(
                        StorageStatus.DOWNLOADED,
                        _tempPro,
                        "filed downloaded"
                    )
                )
            }.addOnFailureListener {
                _showProgressBarState.postValue(false)
                _storageState.postValue(
                    StorageState(
                        StorageStatus.DOWNLOAD_ERROR,
                        null,
                        "Fail to downloadProfilePicture file"
                    )
                )
            }

    }

}
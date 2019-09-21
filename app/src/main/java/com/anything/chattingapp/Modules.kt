package com.anything.chattingapp

import android.content.Context
import com.anything.chattingapp.view.auth.LoginViewModel
import com.anything.chattingapp.view.main.MainActivityViewModel
import com.anything.chattingapp.view.main.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


object Modules {

    const val APP_NAME = "ChattingApp"

    val appModules = module {
        single { GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create() }
        single { FirebaseAuth.getInstance() }
        single { FirebaseDatabase.getInstance() }
        single { FirebaseDatabase.getInstance().getReference(APP_NAME) }
        single { FirebaseStorage.getInstance() }
        viewModel { MainActivityViewModel(get()) }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { ProfileViewModel(get(), get(), get()) }
    }
}
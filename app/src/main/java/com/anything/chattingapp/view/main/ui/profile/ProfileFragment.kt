package com.anything.chattingapp.view.main.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.anything.chattingapp.R
import com.anything.chattingapp.view.main.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private val _vm: ProfileViewModel by viewModel()
    private var proPic: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proPic = view.findViewById(R.id.app_bar_image)
        val appbarLayout = view.findViewById<AppBarLayout>(R.id.appbar)
        val params = appbarLayout.layoutParams as CoordinatorLayout.LayoutParams
        appbarLayout.layoutParams = params
        appbarLayout.setExpanded(false)

        _vm._showProgressBarState.observe(this, Observer {
            if (it) {
                view.progress_bar.visibility = View.VISIBLE
            } else {
                view.progress_bar.visibility = View.GONE
            }
        })

        _vm._userProfile.observe(this, Observer {
            view.tv_contact.text = it.contactNo
            view.tv_email.text = it.email
            view.tv_status.text = it.status
            view.tv_username.text = it.username
        })
        _vm.downloadProfilePicture()
        _vm._storageState.observe(this, Observer {

            val file = it.file
            if (file != null) {
                if (file.length() > 0) {
                    appbarLayout.setExpanded(true, true)
                    Picasso.get().load(it.file!!).into(proPic)
                }
            }
        })

        proPic?.setOnClickListener {
            pickFromGallery()
        }

        view.btn_sign_out.setOnClickListener {
            _vm.signOut()
            (activity as MainActivity).initAuth()
        }

    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when {
                requestCode == GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    Picasso.get().load(uri).into(proPic)
                    _vm.uploadProfilePicture(uri)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.menu_edit -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_profile_to_navigation_profile_edit)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 200
        const val CAMERA_REQUEST_CODE = 201
    }
}
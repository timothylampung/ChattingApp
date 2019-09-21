package com.anything.chattingapp.view.main.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anything.chattingapp.R
import com.anything.chattingapp.data.User
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileEditFragment : Fragment() {

    private val _vm: ProfileViewModel by viewModel()

    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vm._userProfile.observe(this, Observer {
            user = it
            view.et_contact_no.setText(user.contactNo)
            view.et_username.setText(user.username)
            view.et_status.setText(user.status)
        })

        view.btn_update.setOnClickListener{
            user.contactNo = et_contact_no.text.toString()
            user.username = et_username.text.toString()
            user.status = et_status.text.toString()
            _vm.writeUserDetails(user)
        }

    }
}

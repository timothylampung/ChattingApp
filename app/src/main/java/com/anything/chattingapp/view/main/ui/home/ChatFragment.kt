package com.anything.chattingapp.view.main.ui.home

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import com.anything.chattingapp.R
import com.anything.chattingapp.view.main.ui.MainFragment

class ChatFragment : MainFragment() {
    override fun onLogOut() {
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    private lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
            ViewModelProviders.of(this).get(ChatViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}
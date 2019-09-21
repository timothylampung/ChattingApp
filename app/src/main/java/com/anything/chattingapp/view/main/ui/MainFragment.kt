package com.anything.chattingapp.view.main.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.anything.chattingapp.R

abstract class MainFragment : Fragment(), SearchView.OnQueryTextListener {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        val searchMenu: MenuItem? = menu.findItem(R.id.app_bar_search)
        val searchView = searchMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.menu_log_out -> {
                onLogOut()
                onStart()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun onLogOut()
}
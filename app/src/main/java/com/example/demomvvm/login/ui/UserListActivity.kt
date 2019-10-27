package com.example.demomvvm.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demomvvm.R
import com.example.demomvvm.database.User
import com.example.demomvvm.login.adapter.UserListAdapter
import com.example.demomvvm.login.viewmodel.BaseViewModelFactory
import com.example.demomvvm.login.viewmodel.UserViewModel
import com.example.demomvvm.util.MyApp
import com.example.demomvvm.util.Util
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity(),View.OnClickListener {


    private val userViewModel: UserViewModel by lazy {
        ViewModelProviders.of(this, BaseViewModelFactory { UserViewModel(this) }).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        setData()

    }

    private fun setData() {
        recycleview.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false);

        if (Util.isNetworkConnected(this))
            userViewModel.callUserListAPi()
        else
            Util.showSnackBar(root, getString(R.string.msg_internet))

        MyApp.userDao.getUserList().observe(this, Observer {

            recycleview.adapter= UserListAdapter(it as ArrayList<User>, this, myCallback = {

            })
        })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.imgLeft->
            {

            }
        }
    }

}

package com.example.demomvvm.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demomvvm.MyApplication
import com.example.demomvvm.R
import com.example.demomvvm.database.User
import com.example.demomvvm.login.LoginViewModel
import com.example.demomvvm.user.adapter.UserListAdapter
import com.example.demomvvm.utility.Util
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListActivity : AppCompatActivity(),View.OnClickListener {

    @Inject
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        setData()

    }

    private fun setData() {
        (application as MyApplication).appComponent.userListComponent().create(this).inject(this)

        recycleview.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false);

        if (Util.isNetworkConnected(this))
            userViewModel.callUserListAPi()
        else
            Util.showSnackBar(root, getString(R.string.msg_internet))

        userViewModel.userListLiveData.observe(this, Observer {

            CoroutineScope(Dispatchers.IO).launch {

                for (user in it.data) {
                    MyApplication.userDao.insert(user = user)
                }
            }
            recycleview.adapter =
                UserListAdapter(it.data as ArrayList<User>, this@UserListActivity, myCallback = {

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

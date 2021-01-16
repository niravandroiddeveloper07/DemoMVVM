package com.example.demomvvm.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.demomvvm.MyApplication
import com.example.demomvvm.R
import com.example.demomvvm.di.CustomScope
import com.example.demomvvm.user.UserListActivity
import com.example.demomvvm.utility.openActivity

import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var loginviewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setData()

    }

    private fun setData() {
        (application as MyApplication).appComponent.loginComponent().create(this).inject(this)

        editEmail.setText("eve.holt@reqres.in")
        editPassword.setText("cityslicka")
        btnLogin.setOnClickListener(this)
        loginviewModel.loginResponseLiveData.observe(this, Observer {
            if (it != null) {
                openActivity(UserListActivity::class.java)
            }

        })
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnLogin->
            {
                loginviewModel.callLoginApi(editEmail.text.toString(),editPassword.text.toString())
            }
        }
    }

}

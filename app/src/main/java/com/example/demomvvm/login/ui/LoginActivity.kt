package com.example.demomvvm.login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demomvvm.R
import com.example.demomvvm.login.viewmodel.BaseViewModelFactory
import com.example.demomvvm.login.viewmodel.LoginViewModel
import com.example.demomvvm.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import androidx.databinding.DataBindingUtil
import com.example.demomvvm.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, BaseViewModelFactory { LoginViewModel(this) })
            .get(LoginViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
   //     binding.loginviewModel=loginViewModel
        binding.loginViewModel=loginViewModel
        setData()

    }

    private fun setData() {

        editEmail.setText("eve.holt@reqres.in")
        editPassword.setText("cityslicka")

        loginViewModel.loginResponseLiveData.observe(this, Observer {
            if (it != null) {
                startActivity(Intent(this@LoginActivity, UserListActivity::class.java))
            }

        })
    }

    override fun onClick(p0: View?) {
      /*  when (p0!!.id) {
            R.id.btnLogin -> {
                val msg = isValidDetail()

                if (msg.isEmpty()) {
                    loginViewModel.callLoginApi(
                        editEmail.text.toString(),
                        editPassword.text.toString()
                    )
                } else {
                    Util.showSnackBar(root, msg)
                }
            }
        }
    */}

    private fun isValidDetail(): String {

        if (!Util.isNetworkConnected(this)) {
            Util.showSnackBar(root, getString(R.string.msg_internet))
        }
        var msg = ""

        when {
            editEmail.text.toString().isEmpty() -> {
                msg = getString(R.string.email_can_not_blank)

            }
            !Util.isValidEmail(editEmail.text.toString()) -> {
                msg = getString(R.string.email_should_valid)

            }
            editPassword.text.toString().isEmpty() -> {
                msg = getString(R.string.password_can_not_blank)

            }
        }
        return msg
    }
}

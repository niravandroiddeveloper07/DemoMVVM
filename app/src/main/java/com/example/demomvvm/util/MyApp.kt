package com.example.demomvvm.util

import android.app.Application
import android.content.SharedPreferences
import com.example.demomvvm.database.UserDao
import com.example.demomvvm.database.UserDatabase
import com.example.demomvvm.util.PreferenceHelper.PREFCUSTOM
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        userDatabase = UserDatabase.getDatabase(this)
        userDao = userDatabase.userdao()
        pref = PreferenceHelper.customPrefs(this,PREFCUSTOM)
        RxPaparazzo.register(this);
    }

    companion object
    {
        lateinit var userDao: UserDao
        lateinit var userDatabase: UserDatabase
        lateinit var pref:SharedPreferences
    }
}
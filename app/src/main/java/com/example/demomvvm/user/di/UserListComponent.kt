package com.example.demomvvm.user.di

import com.example.demomvvm.login.di.LoginActivityModule
import com.example.demomvvm.user.UserListActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface UserListComponent {

    // Factory to create instances of LoginComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: UserListActivity): UserListComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: UserListActivity)
}

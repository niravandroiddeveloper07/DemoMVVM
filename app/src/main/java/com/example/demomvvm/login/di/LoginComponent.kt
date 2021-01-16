/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demomvvm.login.di

import com.example.demomvvm.di.CustomScope
import com.example.demomvvm.login.LoginActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

// Scope annotation that the LoginComponent uses
// Classes annotated with @ActivityScope will have a unique instance in this Component

// Definition of a Dagger subcomponent

@Subcomponent(modules = [LoginActivityModule::class])
interface LoginComponent {

    // Factory to create instances of LoginComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: LoginActivity): LoginComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: LoginActivity)
}

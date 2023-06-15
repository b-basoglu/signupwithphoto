package com.bbasoglu.core.data

import android.app.Application
import com.bbasoglu.core.data.readString
import com.bbasoglu.core.data.writeString

class SignUpCoreDataStore(
    val application: Application,
    ) {

    companion object{
        const val NAME_USER_KEY = "name"
        const val EMAIL_USER_KEY = "email"
        const val PASSWORD_USER_KEY = "password"
        const val PATH_USER_KEY = "path"
        const val WEBADRESS_USER_KEY= "webadress"
    }

    suspend fun saveNameUser(name: String) {
        application.writeString(NAME_USER_KEY, name)
    }

    suspend fun getUserName() = application.readString(NAME_USER_KEY)

    suspend fun saveEmail(email: String) {
        application.writeString(EMAIL_USER_KEY, email)
    }

    suspend fun getEmail() = application.readString(EMAIL_USER_KEY)

    suspend fun savePassword(password: String) {
        application.writeString(PASSWORD_USER_KEY, password)
    }

    suspend fun getPassword() = application.readString(PASSWORD_USER_KEY)

    suspend fun saveImagePath(imagePath: String) {
        application.writeString(PATH_USER_KEY, imagePath)
    }

    suspend fun getImagePath() = application.readString(PATH_USER_KEY)

    suspend fun saveWebAddress(webAddress: String) {
        application.writeString(WEBADRESS_USER_KEY, webAddress)
    }

    suspend fun getWebAddress() = application.readString(WEBADRESS_USER_KEY)

}
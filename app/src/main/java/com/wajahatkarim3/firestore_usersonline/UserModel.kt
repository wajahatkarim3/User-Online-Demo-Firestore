package com.wajahatkarim3.firestore_usersonline

/**
 * Created by wajahat.karim on 15/01/2018.
 */
data class UserModel constructor(
        var userId: String? = null,
        var name: String? = null,
        var status: String? = null,
        var last_active: Int = 0,
        var online: Boolean = true
)
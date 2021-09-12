package com.one.fruitmanbuyer.repositories

import com.google.firebase.messaging.FirebaseMessaging
import com.one.fruitmanbuyer.utils.SingleResponse

interface FirebaseContract{
    fun generateToken(listener : SingleResponse<String>)
}

class FirebaseRepository : FirebaseContract{
    override fun generateToken(listener: SingleResponse<String>) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    it.result?.let {result->
                        listener.onSuccess(result)
                    } ?: kotlin.run { listener.onFailure(Error("Failed to get firebase token")) }
                }
                else -> listener.onFailure(Error("Cannot get firebase token"))
            }
        }
    }

}
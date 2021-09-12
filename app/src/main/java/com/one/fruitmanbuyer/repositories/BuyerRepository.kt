package com.one.fruitmanbuyer.repositories

import com.google.gson.GsonBuilder
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.models.RegisterBuyer
import com.one.fruitmanbuyer.utils.SingleResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface BuyerContract{
    fun register(registerBuyer: RegisterBuyer, listener : SingleResponse<RegisterBuyer>)
    fun login(email : String, password : String, fcmToken : String, listener: SingleResponse<Buyer>)
    fun profile(token : String, listener: SingleResponse<Buyer>)
    fun updateProfile(token : String, buyer: Buyer, listener: SingleResponse<Buyer>)
    fun updatePhotoProfile(token: String, pathImage : String, listener: SingleResponse<Buyer>)
    fun forgotPassword(email: String, listener: SingleResponse<Buyer>)
    fun updatePassword(token : String, password: String, listener: SingleResponse<Buyer>)
    fun premium(token: String, image : String, listener: SingleResponse<Buyer>)
}

class BuyerRepository(private val api : ApiService) : BuyerContract {
    override fun register(registerBuyer: RegisterBuyer, listener: SingleResponse<RegisterBuyer>) {
        val g = GsonBuilder().create()
        val json = g.toJson(registerBuyer)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.register(body).enqueue(object : Callback<WrappedResponse<RegisterBuyer>>{
            override fun onFailure(call: Call<WrappedResponse<RegisterBuyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<RegisterBuyer>>, response: Response<WrappedResponse<RegisterBuyer>>) {
                when {
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun login(email: String, password: String, fcmToken: String, listener: SingleResponse<Buyer>) {
        api.login(email, password, fcmToken).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Buyer>>, response: Response<WrappedResponse<Buyer>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun profile(token: String, listener: SingleResponse<Buyer>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Buyer>>, response: Response<WrappedResponse<Buyer>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateProfile(token: String, buyer: Buyer, listener: SingleResponse<Buyer>) {
        val g = GsonBuilder().create()
        val json = g.toJson(buyer)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.updateProfile(token, body).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Buyer>>,
                response: Response<WrappedResponse<Buyer>>
            ) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updatePhotoProfile(token: String, pathImage: String, listener: SingleResponse<Buyer>) {
        val file = File(pathImage)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
        api.updatePhotoProfile(token, image).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Buyer>>,
                response: Response<WrappedResponse<Buyer>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun forgotPassword(email: String, listener: SingleResponse<Buyer>) {
        api.forgotPassword(email).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Buyer>>, response: Response<WrappedResponse<Buyer>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> {
                        listener.onFailure(Error(response.message()))
                    }
                }
            }

        })
    }

    override fun updatePassword(token: String, password: String, listener: SingleResponse<Buyer>) {
        api.updatePassword(token, password).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Buyer>>, response: Response<WrappedResponse<Buyer>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun premium(token: String, image: String, listener: SingleResponse<Buyer>) {
        val file = File(image)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val img = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
        api.premium(token, img).enqueue(object : Callback<WrappedResponse<Buyer>>{
            override fun onFailure(call: Call<WrappedResponse<Buyer>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Buyer>>, response: Response<WrappedResponse<Buyer>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data) else listener.onFailure(
                            Error(body.message)
                        )
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}
package com.one.fruitmanbuyer.repositories

import com.google.gson.GsonBuilder
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.models.RegisterBuyer
import com.one.fruitmanbuyer.utils.SingleResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BuyerContract{
    fun register(registerBuyer: RegisterBuyer, listener : SingleResponse<RegisterBuyer>)
    fun login(email : String, password : String, listener: SingleResponse<Buyer>)
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

    override fun login(email: String, password: String, listener: SingleResponse<Buyer>) {
        api.login(email, password).enqueue(object : Callback<WrappedResponse<Buyer>>{
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

}
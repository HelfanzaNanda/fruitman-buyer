package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.utils.SingleResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract{
    fun createOrder(token : String, sellerId : String, productId : String, offer_price : String, listener : SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract{
    override fun createOrder(token: String, sellerId: String, productId: String, offer_price: String, listener: SingleResponse<Order>) {
        api.createOrder(token, sellerId.toInt(), productId.toInt(), offer_price.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
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
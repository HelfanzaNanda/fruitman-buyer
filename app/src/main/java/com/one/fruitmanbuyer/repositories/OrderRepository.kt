package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedListResponse
import com.one.fruitmanbuyer.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract{
    fun createOrder(token : String, sellerId : String, productId : String, offer_price : String, listener : SingleResponse<Order>)
    fun orderOrderIn(token : String, listener: ArrayResponse<Order>)
    fun orderInProgress(token : String, listener: ArrayResponse<Order>)
    fun orderComplete(token : String, listener: ArrayResponse<Order>)
    fun cancel(token: String, id : String, listener:SingleResponse<Order>)
    fun arrive(token: String, id : String, listener: SingleResponse<Order>)
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

    override fun orderOrderIn(token: String, listener: ArrayResponse<Order>) {
        api.orderOrderIn(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        listener.onSuccess(response.body()!!.data)
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun orderInProgress(token: String, listener: ArrayResponse<Order>) {
        api.orderInProgress(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        listener.onSuccess(response.body()!!.data)
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun orderComplete(token: String, listener: ArrayResponse<Order>) {
        api.orderComplete(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        listener.onSuccess(response.body()!!.data)
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun cancel(token: String, id: String, listener: SingleResponse<Order>) {
        api.orderCancel(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun arrive(token: String, id: String, listener: SingleResponse<Order>) {
        api.orderArrived(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}
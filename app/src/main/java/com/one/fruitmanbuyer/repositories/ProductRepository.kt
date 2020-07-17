package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ProductContract{
    fun fetchProducts(token : String, listener : ArrayResponse<Product>)
    fun searchProducts(token: String, name : String, listener: ArrayResponse<Product>)
}

class ProductRepository(private val api : ApiService) : ProductContract{
    override fun fetchProducts(token: String, listener: ArrayResponse<Product>) {
        api.fetchProducts(token).enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            println(body.data)
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

    override fun searchProducts(token: String, name: String, listener: ArrayResponse<Product>) {
        api.searchProducts(token, name).enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
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
package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Fruit
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FruitContract{
    fun fetchFruitsByDistrict(token : String, sub_district_id : String, listener : ArrayResponse<Fruit>)
}

class FruitRepository (private val api : ApiService) : FruitContract {
    override fun fetchFruitsByDistrict(token: String, sub_district_id: String, listener: ArrayResponse<Fruit>) {
        api.fetchFruitsByDistrict(token, sub_district_id.toInt()).enqueue(object : Callback<WrappedListResponse<Fruit>>{
            override fun onFailure(call: Call<WrappedListResponse<Fruit>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Fruit>>, response: Response<WrappedListResponse<Fruit>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}
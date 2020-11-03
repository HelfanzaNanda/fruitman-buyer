package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.SubDistrict
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SubDistrictContract{
    fun fetchSubdistricts(token : String, listener : ArrayResponse<SubDistrict>)
}

class SubDistrictRepository (private val api : ApiService) : SubDistrictContract{
    override fun fetchSubdistricts(token: String, listener: ArrayResponse<SubDistrict>) {
        api.fetchSubDistricts(token).enqueue(object : Callback<WrappedListResponse<SubDistrict>>{
            override fun onFailure(call: Call<WrappedListResponse<SubDistrict>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<SubDistrict>>, response: Response<WrappedListResponse<SubDistrict>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}
package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Report
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ReportContract{
    fun report(token : String, listener : ArrayResponse<Report>)
}

class ReportRepository(private val api : ApiService) : ReportContract {
    override fun report(token: String, listener: ArrayResponse<Report>) {
        api.report(token).enqueue(object : Callback<WrappedListResponse<Report>>{
            override fun onResponse(
                call: Call<WrappedListResponse<Report>>,
                response: Response<WrappedListResponse<Report>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }

            }

            override fun onFailure(call: Call<WrappedListResponse<Report>>, t: Throwable) {
                listener.onFailure(Error(t.message.toString()))
            }

        })
    }
}
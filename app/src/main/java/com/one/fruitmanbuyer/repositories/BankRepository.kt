package com.one.fruitmanbuyer.repositories

import com.one.fruitmanbuyer.models.Bank
import com.one.fruitmanbuyer.utils.SingleResponse
import com.one.fruitmanbuyer.webservices.ApiService
import com.one.fruitmanbuyer.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BankContract{
    fun fetchBank(listener : SingleResponse<Bank>)
}

class BankRepository(private val api : ApiService) : BankContract{
    override fun fetchBank(listener: SingleResponse<Bank>) {
        api.fetchBank().enqueue(object : Callback<WrappedResponse<Bank>> {
            override fun onFailure(call: Call<WrappedResponse<Bank>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Bank>>,
                response: Response<WrappedResponse<Bank>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}
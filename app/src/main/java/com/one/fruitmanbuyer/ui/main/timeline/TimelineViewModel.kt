package com.one.fruitmanbuyer.ui.main.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.repositories.ProductRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent

class TimelineViewModel (private val productRepository: ProductRepository) : ViewModel(){
    private val state : SingleLiveEvent<TimelineState> = SingleLiveEvent()
    private val products = MutableLiveData<List<Product>>()

    private fun setLoading(){ state.value = TimelineState.Loading(true) }
    private fun hideLoading(){ state.value = TimelineState.Loading(false) }
    private fun toast(message: String){ state.value = TimelineState.ShowToast(message) }

    fun fetchProducts(token : String){
        setLoading()
        productRepository.fetchProducts(token, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                hideLoading()
                products.postValue(datas)
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun searchProduct(token: String, name : String){
        setLoading()
        productRepository.searchProducts(token, name, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                hideLoading()
                datas?.let { products.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }


    fun listenToState() = state
    fun listenToProducts() = products
}

sealed class TimelineState{
    data class Loading(var state : Boolean = false) : TimelineState()
    data class ShowToast(var message : String) : TimelineState()
}
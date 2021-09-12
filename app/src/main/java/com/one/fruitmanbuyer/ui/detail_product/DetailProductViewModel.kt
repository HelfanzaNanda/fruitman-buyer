package com.one.fruitmanbuyer.ui.detail_product

import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.repositories.OrderRepository
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class DetailProductViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailProductState> = SingleLiveEvent()

    private fun setLoading(){ state.value = DetailProductState.Loading(true) }
    private fun hideLoading(){ state.value = DetailProductState.Loading(false) }
    private fun toast(message: String){ state.value = DetailProductState.ShowToast(message) }
    private fun success() { state.value = DetailProductState.Success }

    fun validate(offerPrice : String) : Boolean {
        if (offerPrice.isEmpty()){
            state.value = DetailProductState.Validate(offrePrice = "masukkan tawaran harga")
            return false
        }
        return true
    }

    fun createOrder(token : String, sellerId : String, productId : String, offerPrice : String){
        setLoading()
        orderRepository.createOrder(token, sellerId, productId, offerPrice, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class DetailProductState{
    data class Loading(var state : Boolean = false) : DetailProductState()
    data class ShowToast(var message : String) : DetailProductState()
    object Success : DetailProductState()
    data class Validate(var offrePrice : String? = null) : DetailProductState()
    object Reset : DetailProductState()

}
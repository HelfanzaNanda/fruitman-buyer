package com.one.fruitmanbuyer.ui.complete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.repositories.OrderRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent

class CompleteViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<CompleteState> = SingleLiveEvent()
    private var ordersComplete = MutableLiveData<List<Order>>()

    private fun setLoading() { state.value = CompleteState.Loading(true) }
    private fun hideLoading() { state.value = CompleteState.Loading(false) }
    private fun toast(message: String) { state.value = CompleteState.ShowToast(message) }

    fun fetchOrderComplete(token : String){
        setLoading()
        orderRepository.orderComplete(token, object : ArrayResponse<Order> {
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { ordersComplete.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToOrdersComplete() = ordersComplete
}

sealed class CompleteState {
    data class Loading(var state : Boolean = false) : CompleteState()
    data class ShowToast(var message : String) : CompleteState()
}
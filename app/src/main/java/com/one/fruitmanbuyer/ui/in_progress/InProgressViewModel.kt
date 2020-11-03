package com.one.fruitmanbuyer.ui.in_progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.repositories.OrderRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class InProgressViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<InProgressState> = SingleLiveEvent()
    private var ordersInProgress = MutableLiveData<List<Order>>()

    private fun setLoading() { state.value = InProgressState.Loading(true) }
    private fun hideLoading() { state.value = InProgressState.Loading(false) }
    private fun toast(message: String) { state.value = InProgressState.ShowToast(message) }
    private fun successArrived() { state.value = InProgressState.SuccessArrived }

    fun fetchInProgress(token : String){
        setLoading()
        orderRepository.orderInProgress(token, object : ArrayResponse<Order> {
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { ordersInProgress.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun orderArrive(token: String, id : String){
        orderRepository.arrive(token, id, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successArrived() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToOrdersInProgress() = ordersInProgress
}

sealed class InProgressState {
    data class Loading(var state : Boolean = false) : InProgressState()
    data class ShowToast(var message : String) : InProgressState()
    object SuccessArrived : InProgressState()
}
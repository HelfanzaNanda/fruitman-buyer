package com.one.fruitmanbuyer.ui.order_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.repositories.OrderRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class OrderInViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<OrderInState> = SingleLiveEvent()
    private var ordersIn = MutableLiveData<List<Order>>()

    private fun setLoading() { state.value = OrderInState.Loading(true) }
    private fun hideLoading() { state.value = OrderInState.Loading(false) }
    private fun toast(message: String) { state.value = OrderInState.ShowToast(message) }
    private fun successCancel() { state.value = OrderInState.SuccessCancel }

    fun fetchOrderIn(token : String){
        setLoading()
        orderRepository.orderOrderIn(token, object : ArrayResponse<Order>{
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { ordersIn.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun cancel (token: String, id : String){
        setLoading()
        orderRepository.cancel(token, id, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successCancel() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToOrdersIn() = ordersIn
}

sealed class OrderInState {
    data class Loading(var state : Boolean = false) : OrderInState()
    data class ShowToast(var message : String) : OrderInState()
    object SuccessCancel : OrderInState()
}
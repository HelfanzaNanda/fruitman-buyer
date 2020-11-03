package com.one.fruitmanbuyer.ui.order_in

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible

import kotlinx.android.synthetic.main.activity_order_in.*
import kotlinx.android.synthetic.main.content_order_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderInActivity : AppCompatActivity() {

    private val orderInViewModel : OrderInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_in)
        setSupportActionBar(toolbar)
        setUpUI()
        observe()
        observer()
    }

    private fun observer() {
        orderInViewModel.listenToState().observer(this, Observer { HandleUiState(it) })
    }

    private fun HandleUiState(it: OrderInState) {
        when(it){
            is OrderInState.Loading -> handleLoading(it.state)
            is OrderInState.ShowToast -> showToast(it.message)
            is OrderInState.SuccessCancel -> handleSuccessCancel()
        }
    }

    private fun handleSuccessCancel() {
        orderInViewModel.fetchOrderIn(Constants.getToken(this@OrderInActivity))
        showToast("berhasil membatalkan pesanan")
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun observe() {
        orderInViewModel.listenToOrdersIn().observe(this, Observer { handleOrdersIn(it) })
    }

    private fun handleOrdersIn(it: List<Order>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is OrderInAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun setUpUI() {
        recyclerView.apply {
            adapter = OrderInAdapter(mutableListOf(), this@OrderInActivity, orderInViewModel)
            layoutManager = LinearLayoutManager(this@OrderInActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        orderInViewModel.fetchOrderIn(Constants.getToken(this@OrderInActivity))
    }
}

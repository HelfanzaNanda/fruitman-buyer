package com.one.fruitmanbuyer.ui.in_progress

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible

import kotlinx.android.synthetic.main.activity_in_progress.*
import kotlinx.android.synthetic.main.content_in_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InProgressActivity : AppCompatActivity() {

    private val inProgressViewModel : InProgressViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_progress)
        setSupportActionBar(toolbar)
        setUpUI()
        observer()
        observe()
    }

    private fun setUpUI() {
        recyclerView.apply {
            adapter = InProgressAdapter(mutableListOf(), this@InProgressActivity, inProgressViewModel)
            layoutManager = LinearLayoutManager(this@InProgressActivity)
        }
    }

    private fun observer() = inProgressViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(it: InProgressState) {
        when(it){
            is InProgressState.Loading -> handleLoading(it.state)
            is InProgressState.ShowToast -> showToast(it.message)
            is InProgressState.SuccessArrived -> handleSuccessArrived()
        }
    }

    private fun handleSuccessArrived() {
        inProgressViewModel.fetchInProgress(Constants.getToken(this@InProgressActivity))
        showToast("anda sudah sampai lokasi")
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun observe() {
        inProgressViewModel.listenToOrdersInProgress().observe(this, Observer { handleOrdersInProgress(it) })
    }

    private fun handleOrdersInProgress(it: List<Order>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is InProgressAdapter){
                adapter.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressViewModel.fetchInProgress(Constants.getToken(this@InProgressActivity))
    }
}

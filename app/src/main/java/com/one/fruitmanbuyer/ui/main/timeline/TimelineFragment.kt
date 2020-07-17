package com.one.fruitmanbuyer.ui.main.timeline

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimelineFragment : Fragment(R.layout.fragment_timeline){
    
    private val timelineViewModel : TimelineViewModel by viewModel()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        observer()
        observe()
        search()
    }

    private fun setUpUI() {
        rv_timeline?.apply {
            adapter = TimelineAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        timelineViewModel.listenToProducts().observe(requireActivity(), Observer { handleProducts(it) })
    }

    private fun handleProducts(it: List<Product>) {
        rv_timeline.adapter?.let { adapter ->
            if (adapter is TimelineAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun observer() {
        timelineViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
    }

    private fun search(){
        img_search.setOnClickListener {
            val token = Constants.getToken(requireActivity())
            val name = search_name.text.toString().trim()
            timelineViewModel.searchProduct(token, name)
        }
    }

    private fun handleUiState(it: TimelineState) {
        when(it){
            is TimelineState.Loading -> handleLoading(it.state)
            is TimelineState.ShowToast -> requireActivity().showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) {

        if (state) pb_timeline.visible() else pb_timeline.gone()
    }

    override fun onResume() {
        super.onResume()
        timelineViewModel.fetchProducts(Constants.getToken(requireActivity()))
    }

    override fun onStart() {
        super.onStart()
        timelineViewModel.fetchProducts(Constants.getToken(requireActivity()))
    }
}
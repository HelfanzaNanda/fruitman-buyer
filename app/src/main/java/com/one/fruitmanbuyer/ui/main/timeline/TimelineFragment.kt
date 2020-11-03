package com.one.fruitmanbuyer.ui.main.timeline

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Fruit
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.models.SubDistrict
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_timeline.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimelineFragment : Fragment(R.layout.fragment_timeline){
    
    private val timelineViewModel : TimelineViewModel by viewModel()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
        fetchSubsdistrict()
    }

    private fun setUpRecyclerView() {
        requireView().rv_timeline.apply {
            adapter = TimelineAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeProducts()
        observeSubDistricts()
    }


    private fun observeState() = timelineViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
    private fun observeProducts() = timelineViewModel.listenToProducts().observe(requireActivity(), Observer { handleProducts(it) })
    private fun observeSubDistricts() = timelineViewModel.listenToSubDistricts().observe(requireActivity(), Observer { handleSubDistricts(it) })
    private fun observeFruits() = timelineViewModel.listenToFruits().observe(requireActivity(), Observer { handleFruits(it) })
    private fun getIdSubDIstrict() = timelineViewModel.getIdSubDistrict().value

    private fun setIdSubDistrict(subDIstrictId : String) = timelineViewModel.setIdSubDistrict(subDIstrictId)
    private fun fetchFruitBySubDistrict(subDistrictId : String) = timelineViewModel.fetchFruitsByDistrict(Constants.getToken(requireActivity()), subDistrictId)
    private fun fetchSubsdistrict() = timelineViewModel.fetchSubdistricts(Constants.getToken(requireActivity()))
    private fun fetchProducts() = timelineViewModel.fetchProducts(Constants.getToken(requireActivity()))
    private fun fetchProductBySubdistrict(subDistrictId : String) = timelineViewModel.fetchProductsBySubDistrict(Constants.getToken(requireActivity()), subDistrictId)
    private fun fetchProductByCriteria(subDistrictId: String, query : String) {
        timelineViewModel.fetchProductsByCriteria(Constants.getToken(requireActivity()), subDistrictId, query)
    }

    private fun handleSubDistricts(list: List<SubDistrict>?) {
        list?.let { lisSubDistrict ->
            val subDistrictNames = mutableListOf("Semua")
            lisSubDistrict.map { subDistrictNames.add(it.name!!) }
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, subDistrictNames)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            requireView().spinner_sub_district.adapter = adapter
            requireView().spinner_sub_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (subDistrictNames[position] == "Semua") {
                        fetchProducts()
                        requireView().rv_timeline.visible()
                        requireView().linear_empty.gone()
                    }else{
                        val filterDistrict = lisSubDistrict.find { subDistrict -> subDistrict.name == subDistrictNames[position] }
                        fetchFruitBySubDistrict(filterDistrict!!.id.toString())
                        setIdSubDistrict(filterDistrict.id.toString())
                        observeFruits()
                    }
                }
            }
        }
    }

    private fun handleFruits(listFruit: List<Fruit>) {
        val distinctFruit = listFruit.distinctBy { fruit -> fruit.name  }
        val fruitNames : MutableList<String> = mutableListOf("Semua")
        distinctFruit.map { fruitNames.add(it.name!!) }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, fruitNames)
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        requireView().spinner_fruit.adapter = adapter
        requireView().spinner_fruit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (fruitNames[position] == "Semua") {
                    fetchProductBySubdistrict(getIdSubDIstrict()!!)
                }else{
                    val query = listFruit.find { fruit ->fruit.name == fruitNames[position]  }
                    fetchProductByCriteria(getIdSubDIstrict()!!, query!!.id.toString())
                }
            }
        }
        if (listFruit.isEmpty()){
            requireView().rv_timeline.gone()
            requireView().linear_empty.visible()
        }else{
            requireView().rv_timeline.visible()
            requireView().linear_empty.gone()
        }
    }

    private fun handleProducts(it: List<Product>) {
        requireView().rv_timeline.adapter?.let { adapter ->
            if (adapter is TimelineAdapter) adapter.changelist(it)
        }
    }

//    private fun search(){
//        img_search.setOnClickListener {
//            val token = Constants.getToken(requireActivity())
//            val name = search_name.text.toString().trim()
//            timelineViewModel.searchProduct(token, name)
//        }
//    }

    private fun handleUiState(it: TimelineState) {
        activity?.let {_->
            when(it){
                is TimelineState.Loading -> handleLoading(it.state)
                is TimelineState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) = if (state) requireView().pb_timeline.visible() else requireView().pb_timeline.gone()

    override fun onResume() {
        super.onResume()
        fetchProducts()
    }
}
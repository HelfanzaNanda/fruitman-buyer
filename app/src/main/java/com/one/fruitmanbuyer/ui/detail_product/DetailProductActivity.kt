package com.one.fruitmanbuyer.ui.detail_product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import coil.api.load
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.content_detail_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductActivity : AppCompatActivity() {

    private val detailProductViewModel : DetailProductViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        setUpUI()
        observer()
        order()
    }

    private fun observer() {
        detailProductViewModel.listenToState().observer(this, Observer { handleUIState(it) })
    }

    private fun handleUIState(it: DetailProductState) {
        when(it){
            is DetailProductState.ShowToast -> showToast(it.message)
            is DetailProductState.Loading -> handleLoading(it.state)
            is DetailProductState.Success -> handleSuccess()
        }
    }

    private fun handleSuccess() {
        popup("anda telah mengorder product")
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
        btn_offer_price.isEnabled = !state
    }

    private fun popup(message: String) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun order(){
        btn_offer_price.setOnClickListener {
            val token = Constants.getToken(this@DetailProductActivity)
            val offerPrice = et_offer_price.text.toString().trim()
            val sellerId =  getPassedProduct()?.seller!!.id.toString()
            val productId = getPassedProduct()?.id.toString()
            detailProductViewModel.createOrder(token, sellerId, productId, offerPrice)
        }
    }


    private fun setUpUI() {
        getPassedProduct()?.let {
            product_image.load(it.image)
            product_name.text = it.name
            product_price.text = Constants.setToIDR(it.price!!)
            product_address.text = it.address
            product_desc.text = it.description
        }
    }

    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
}

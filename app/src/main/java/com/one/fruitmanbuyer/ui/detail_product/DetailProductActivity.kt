package com.one.fruitmanbuyer.ui.detail_product

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import coil.api.load
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.ui.maps.MapsActivity
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.content_detail_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductActivity : AppCompatActivity() {

    private val detailProductViewModel : DetailProductViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        setUpImageSlider()
        setUpUI()
        observer()
        order()
        goToMapsActivity()
    }

    private fun setUpImageSlider() {
        image_slider.apply {
            setSliderAdapter(ImageSliderAdapter(mutableListOf()))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    private fun observer() = detailProductViewModel.listenToState().observer(this, Observer { handleUIState(it) })

    private fun handleUIState(it: DetailProductState) {
        when(it){
            is DetailProductState.ShowToast -> showToast(it.message)
            is DetailProductState.Loading -> handleLoading(it.state)
            is DetailProductState.Success -> handleSuccess()
            is DetailProductState.Validate -> handleValidate(it)
        }
    }

    private fun handleValidate(it: DetailProductState.Validate) = popupValidate(it.offrePrice!!)
    private fun handleSuccess() = popup("anda telah mengorder product")
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

    private fun popupValidate(message: String) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun order(){
        btn_offer_price.setOnClickListener {
            val token = Constants.getToken(this@DetailProductActivity)
            val offerPrice = et_offer_price.text.toString().trim()
            val sellerId = getPassedProduct()?.seller!!.id.toString()
            val productId = getPassedProduct()?.id.toString()
            detailProductViewModel.createOrder(token, sellerId, productId, offerPrice)
        }
    }

    private fun goToMapsActivity(){
        img_maps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java).apply {
                putExtra("LAT", getPassedProduct()?.lat)
                putExtra("LNG", getPassedProduct()?.lng)
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI() {
        if (isOrdered()){
            btn_offer_price.disabled()
            et_offer_price.isFocusableInTouchMode = false
        }else{
            et_offer_price.isFocusableInTouchMode = true
            btn_offer_price.enabled()
        }
        getPassedProduct()?.let {
            txt_seller_name.text = it.seller!!.name
            product_name.text = it.name
            product_price.text = Constants.setToIDR(it.price!!)
            product_address.text = it.address
            product_whatsapp.text = it.seller!!.phone
            product_whatsapp.setOnClickListener { _->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=62${it.seller!!.phone}")))
            }
            product_desc.text = it.description
            println(it.images)
            image_slider.sliderAdapter?.let { pagerAdapter ->
                if (pagerAdapter is ImageSliderAdapter){
                    pagerAdapter.changelist(it.images)
                }
            }
        }
    }

    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
    private fun isOrdered() = intent.getBooleanExtra("IS_ORDER", false)
}

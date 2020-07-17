package com.one.fruitmanbuyer.ui.main.timeline

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.ui.detail_product.DetailProductActivity
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.showToast
import kotlinx.android.synthetic.main.list_item_timeline.view.*

class TimelineAdapter (private val products : MutableList<Product>, private val context: Context)
    : RecyclerView.Adapter<TimelineAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_timeline, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product, context: Context){
            with(itemView){
                txt_name_product.text = product.name
                txt_address_product.text = product.address
                txt_price_product.text = Constants.setToIDR(product.price!!)
                img_product.load(product.image)
                setOnClickListener {
                    context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                        putExtra("PRODUCT", product)
                    })
                }
            }
        }
    }

    fun changelist(c : List<Product>){
        products.clear()
        products.addAll(c)
        notifyDataSetChanged()
    }
}
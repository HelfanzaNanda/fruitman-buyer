package com.one.fruitmanbuyer.ui.complete

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.ui.detail_product.DetailProductActivity
import kotlinx.android.synthetic.main.list_item_complete.view.*

class CompleteAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<CompleteAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_complete, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context){
            with(itemView){
                setOnClickListener {
                    context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                        putExtra("PRODUCT", order.product)
                        putExtra("IS_ORDER", true)
                    })
                }
                tv_desc_complete.text = "transaksi dengan ${order.seller.name} dengan ${order.product.name} sudah selesai"
                tv_date.text = order.updated_at
                tv_name_prodcut.text = order.product.name
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}
package com.one.fruitmanbuyer.ui.order_in

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
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.AlertCancel
import kotlinx.android.synthetic.main.list_item_order_in.view.*

class OrderInAdapter (private var orders : MutableList<Order>,
                      private var context: Context,
                      private var orderInViewModel: OrderInViewModel)
    : RecyclerView.Adapter<OrderInAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_in, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =holder.bind(orders[position], context, orderInViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, orderInViewModel: OrderInViewModel){
            with(itemView){
                setOnClickListener {
                    context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                        putExtra("PRODUCT", order.product)
                        putExtra("IS_ORDER", true)
                    })
                }
                waiting_desc.text = "Menunggu Konfirmasi dari ${order.seller.name} dengan produk tebasan ${order.product.name}"
                btn_decline.setOnClickListener {
                    val token = Constants.getToken(context)
                    val message = "apakah anda yakin ingin membatalkan pesanan ini?"
                    context.AlertCancel(message, token, order.id.toString(), orderInViewModel)
                }
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}
package com.one.fruitmanbuyer.ui.in_progress

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
import com.one.fruitmanbuyer.utils.extensions.AlertArrive
import com.one.fruitmanbuyer.utils.extensions.gone
import kotlinx.android.synthetic.main.list_item_in_progress.view.*

class InProgressAdapter (private var orders : MutableList<Order>,
                         private var context: Context,
                         private var inProgressViewModel: InProgressViewModel)
    : RecyclerView.Adapter<InProgressAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_in_progress, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =holder.bind(orders[position], context, inProgressViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, inProgressViewModel: InProgressViewModel){
            with(itemView){
                setOnClickListener {
                    context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                        putExtra("PRODUCT", order.product)
                        putExtra("IS_ORDER", true)
                    })
                }
                if (order.arrive == false){
                    tv_desc.text = "${order.seller.name} menunggu di tempat dengan product ${order.product.name}"
                    btn_arrived.setOnClickListener {
                        val token  = Constants.getToken(context)
                        val message = "apakah anda sudah sampai lokasi?"
                        context.AlertArrive(message, token, order.id.toString(), inProgressViewModel)
                    }
                }else{
                    tv_desc.text = "dalam proses transaksi dengan ${order.seller.name} dengan ${order.product.name}"
                    btn_arrived.gone()
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
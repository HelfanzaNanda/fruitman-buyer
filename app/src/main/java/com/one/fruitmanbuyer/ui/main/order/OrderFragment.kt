package com.one.fruitmanbuyer.ui.main.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.ui.complete.CompleteActivity
import com.one.fruitmanbuyer.ui.in_progress.InProgressActivity
import com.one.fruitmanbuyer.ui.order_in.OrderInActivity
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment(R.layout.fragment_order){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_buyer_waiting.setOnClickListener { startActivity(Intent(requireActivity(), OrderInActivity::class.java)) }
        btn_buyer_in_progress.setOnClickListener { startActivity(Intent(requireActivity(), InProgressActivity::class.java)) }
        btn_buyer_completed.setOnClickListener { startActivity(Intent(requireActivity(), CompleteActivity::class.java)) }

    }
}
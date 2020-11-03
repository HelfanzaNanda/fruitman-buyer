package com.one.fruitmanbuyer.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.one.fruitmanbuyer.ui.in_progress.InProgressViewModel
import com.one.fruitmanbuyer.ui.order_in.OrderInViewModel

fun Context.showToast(message : String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.AlertRegister(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}

fun Context.AlertCancel(message: String, token :String, id : String, orderInViewModel: OrderInViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
            orderInViewModel.cancel(token, id)
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}

fun Context.AlertArrive(message: String, token :String, id : String, inProgressViewModel: InProgressViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
            inProgressViewModel.orderArrive(token, id)
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}
package amingoli.com.selar.dialog

import amingoli.com.selar.R
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_set_payment.*
import kotlinx.android.synthetic.main.include_item_dialog_set_payment.view.*


class SetPaymentDialog(context: Context) : AlertDialog(context) {


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_set_payment)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        pay.tv_money.setOnClickListener {
            pay.tv_card.animate().alpha(0f).duration = 200
            pay.tv_multi_pay.animate().alpha(0f).duration = 200
            pay.submit.visibility = View.VISIBLE
            pay.submit.animate().alpha(1f).duration = 200
            pay.submit.setBtnText("به صورت نقدی دریافت شد")
            Handler().postDelayed({ dismiss() },2000)
        }
    }
}
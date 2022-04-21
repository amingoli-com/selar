package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.helper.Config.ORDER_STATUS_SUCCESS
import amingoli.com.selar.model.Orders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_set_payment.*
import kotlinx.android.synthetic.main.include_item_dialog_set_payment.view.*


class SetPaymentDialog(context: Context, var orders: Orders, val listener: Listener?) : AlertDialog(context) {


    interface Listener{
        fun onPayedByMoneyCash(orders: Orders)
        fun onPayedByCard(orders: Orders)
        fun onPayedByMultiCash(orders: Orders)
        fun onPayedByDebit(orders: Orders)
    }

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_set_payment)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        pay.tv_money.setOnClickListener {
            setOrderPiedByCashMoney()
            paid("money")
            show_message_result("به صورت نقدی پرداخت شد",null,null)
            listener?.onPayedByMoneyCash(orders)
        }
    }

    private fun paid(type:String){
        pay.tv_money.animate().alpha(if (type == "money") 1f else 0f).duration = 200
        pay.tv_card.animate().alpha(if (type == "card") 1f else 0f).duration = 200
        pay.tv_multi_pay.animate().alpha(if (type == "multi") 1f else 0f).duration = 200
        pay.tv_debit.animate().alpha(if (type == "debit") 1f else 0f).duration = 200
    }

    private fun show_message_result(message:String, text_color:Int?, bg_color:Int?){
        pay.tv_submit.visibility = View.VISIBLE
        pay.tv_submit.animate().alpha(1f).duration = 200
        pay.tv_submit.setText(message)
        if (text_color != null) pay.tv_submit.setTextColor(text_color)
        if (bg_color != null) pay.tv_submit.backgroundTintList = ContextCompat.getColorStateList(context, bg_color)
    }

    private fun total_all_order(): Double{
        return orders.total_price_order + orders.total_tax + orders.totla_shipping
    }

    private fun setOrderPiedByCashMoney(){
        orders.amount_cash = total_all_order()
        orders.status = ORDER_STATUS_SUCCESS
    }
}
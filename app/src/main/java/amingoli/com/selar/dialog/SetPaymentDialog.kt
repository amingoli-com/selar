package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_SUCCESS
import amingoli.com.selar.model.Orders
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_set_payment.*
import kotlinx.android.synthetic.main.include_item_dialog_set_multi_pay.view.*
import kotlinx.android.synthetic.main.include_item_dialog_set_payment.view.*


class SetPaymentDialog(context: Context, var orders: Orders, val listener: Listener?) : AlertDialog(context) {

    private var TYPE_CLICK_PAY = "show"

    interface Listener{
        fun onPayedByMoneyCash(dialog: SetPaymentDialog, orders: Orders)
        fun onPayedByCard(dialog: SetPaymentDialog, orders: Orders)
        fun onPayedByMultiCash(dialog: SetPaymentDialog, orders: Orders)
        fun onPayedByDebit(dialog: SetPaymentDialog, orders: Orders)
    }

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_set_payment)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        tv_amount.setText(context.getString(R.string.amount_pay_vlaue, App.priceFormat(orders.totla_all,true)))

        pay.tv_money.setOnClickListener {
            setOrderPiedByCashMoney()
            paid("money")
            show_message_result("برای پرداخت نقدی لمس‌کنید",R.color.green_2)
        }

        pay.tv_card.setOnClickListener {
            setOrderPiedByCard()
            paid("card")
            show_message_result("برای پرداخت کارتخوان لمس‌کنید",R.color.blue)
        }

        pay.tv_multi_pay.setOnClickListener {
            setOrderPiedByMultiPay()
            paid("multi_pay")
            Handler().postDelayed({
                pay.visibility = View.GONE
                box_multi.visibility = View.VISIBLE
            },100)
        }

        pay.tv_debit.setOnClickListener {
            setOrderPiedByDebit()
            paid("debit")
            show_message_result("برای پرداخت نسیه لمس‌کنید",R.color.complementary)
        }

        pay.close.setOnClickListener {
            backToMainPay()
            paid("show")
        }

        pay.tv_submit.setOnClickListener {
            when(TYPE_CLICK_PAY){
                "show"-> paid(TYPE_CLICK_PAY)
                "money"-> listener?.onPayedByMoneyCash(this, orders)
                "card"-> listener?.onPayedByCard(this, orders)
                "multi_pay"-> listener?.onPayedByMultiCash(this, orders)
                "debit"-> listener?.onPayedByDebit(this, orders)
            }
        }

        box_multi.back.setOnClickListener {
            backToMainPay()
            box_multi.edt_pay_cash_money.text.clear()
            box_multi.edt_pay_card.text.clear()
            box_multi.edt_pay_debit.text.clear()
            paid("show")
        }

        box_multi.submit.btn.setOnClickListener {
            if (payIsValid()){
                backToMainPay()
                setOrderPiedByMultiPay()
                listener?.onPayedByMultiCash(this, orders)
            }
        }


        box_multi.edt_pay_cash_money.addTextChangedListener(PriceTextWatcher(box_multi.edt_pay_cash_money) { calculate("cash_money") })
        box_multi.edt_pay_card.addTextChangedListener(PriceTextWatcher(box_multi.edt_pay_card) { calculate("card") })
        box_multi.edt_pay_debit.addTextChangedListener(PriceTextWatcher(box_multi.edt_pay_debit) { calculate("debit") })
    }

    private fun paid(type:String){
        TYPE_CLICK_PAY = type
        pay.tv_money.animate().alpha(if (type == "money" || type == "show") 1f else 0f).duration = 200
        pay.tv_card.animate().alpha(if (type == "card" || type == "show") 1f else 0f).duration = 200
        pay.tv_multi_pay.animate().alpha(if (type == "multi_pay" || type == "show") 1f else 0f).duration = 200
        pay.tv_debit.animate().alpha(if (type == "debit" || type == "show") 1f else 0f).duration = 200

        pay.tv_money.visibility = if (type == "money" || type == "show") View.VISIBLE else View.INVISIBLE
        pay.tv_card.visibility = if (type == "card" || type == "show") View.VISIBLE else View.INVISIBLE
        pay.tv_multi_pay.visibility = if (type == "multi_pay" || type == "show") View.VISIBLE else View.INVISIBLE
        pay.tv_debit.visibility = if (type == "debit" || type == "show") View.VISIBLE else View.INVISIBLE

    }

    private fun backToMainPay(){
        box_multi.visibility = View.GONE
        pay.visibility = View.VISIBLE

        pay.tv_submit.visibility = View.GONE
        pay.tv_submit.animate().alpha(0f).duration = 200
        pay.close.visibility = View.GONE
        pay.close.animate().alpha(0f).duration = 200
    }

    private fun show_message_result(message:String, bg_color:Int?){
        pay.tv_submit.visibility = View.VISIBLE
        pay.tv_submit.animate().alpha(1f).duration = 200
        pay.close.visibility = View.VISIBLE
        pay.close.animate().alpha(1f).duration = 200
        pay.tv_submit.setText(message)
//        if (text_color != null) pay.tv_submit.setTextColor(text_color)
        if (bg_color != null) pay.tv_submit.backgroundTintList = ContextCompat.getColorStateList(context, bg_color)
    }

    private fun total_all_order(): Double{
        return orders.total_price_order + orders.total_tax + orders.totla_shipping
    }

    private fun setOrderPiedByCashMoney(){
        orders.pay_cash = total_all_order()
        orders.pay_card = 0.0
        orders.pay_card_info = null
        orders.customer_debtor = 0.0
        orders.status = ORDER_STATUS_SUCCESS
    }

    private fun setOrderPiedByCard(){
        orders.pay_cash = 0.0
        orders.pay_card = total_all_order()
        orders.pay_card_info = "TEST_INFO_CARD"
        orders.customer_debtor = 0.0
        orders.status = ORDER_STATUS_SUCCESS
    }

    private fun setOrderPiedByMultiPay(){
        orders.pay_cash = App.convertToDouble(box_multi.edt_pay_cash_money)
        orders.pay_card = App.convertToDouble(box_multi.edt_pay_card)
        orders.pay_card_info = "TEST_INFO_CARD"
        orders.customer_debtor = App.convertToDouble(box_multi.edt_pay_debit)
        orders.status = ORDER_STATUS_SUCCESS
    }

    private fun setOrderPiedByDebit(){
        orders.pay_cash = 0.0
        orders.pay_card = 0.0
        orders.pay_card_info = null
        orders.customer_debtor = total_all_order()
        orders.status = ORDER_STATUS_SUCCESS
    }

    private fun calculate(type: String){
        when(type){
            "cash_money"-> {
                if (totla_paied() >= total_all_order()){
                    box_multi.box_edt_pay_card.helperText = null
                } else{
                    box_multi.box_edt_pay_card.helperText = "کارتخوان " + "(${App.priceFormat(amount_for_pay(), true)})"
                }
            }
            "card"-> {
                if (totla_paied() >= total_all_order()){
                    box_multi.box_edt_pay_debit.helperText = null
                } else{
                    box_multi.box_edt_pay_debit.helperText = "نسیه " + "(${App.priceFormat(amount_for_pay(), true)})"
                }
            }
        }

        when {
            totla_paied() > total_all_order() -> {
                box_multi.submit.animate().alpha(0.3f).duration = 200
                tv_amount.setTextColor(ContextCompat.getColor(context,R.color.red))
                tv_amount.setText("مبالغ پرداختی از سفارش بیشتر است " + " ${App.priceFormat(amount_for_pay(), true)}")
            }
            totla_paied() == total_all_order() -> {
                tv_amount.setTextColor(ContextCompat.getColor(context,R.color.complementary))
                tv_amount.setText("مبالغ پرداختی " + " ${App.priceFormat(amount_for_pay(), true)}")
                box_multi.submit.animate().alpha(1f).duration = 200
            }
            else -> {
                box_multi.submit.animate().alpha(0.3f).duration = 200
                tv_amount.setTextColor(ContextCompat.getColor(context,R.color.complementary))
                tv_amount.setText("باقی مانده" + " ${App.priceFormat(amount_for_pay(), true)}")
            }
        }

    }

    private fun totla_paied(): Double{
        val cash_money = App.convertToDouble(box_multi.edt_pay_cash_money)
        val card = App.convertToDouble(box_multi.edt_pay_card)
        val debit = App.convertToDouble(box_multi.edt_pay_debit)
        return cash_money + card + debit
    }

    private fun amount_for_pay(): Double{
        return total_all_order() - totla_paied()
    }

    private fun payIsValid() :Boolean{
        return totla_paied() == total_all_order()
    }
}
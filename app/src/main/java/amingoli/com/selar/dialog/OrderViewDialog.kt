package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.activity.add_order.AddOrderActivity
import amingoli.com.selar.adapter.OrderDetailAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_SUCCESS
import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_order_view.*
import java.util.*
import kotlin.collections.ArrayList

class OrderViewDialog(val _context: Context, val order_id:Int, val position: Int?, val listener: Listener?) : DialogFragment() {

    private var this_order = App.database.getAppDao().selectOrdersById(order_id)
    private var adapterTag : TagInfoAdapter? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_order_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        initOrderData()
        initOnClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener{
        fun onEditOrder(dialog: OrderViewDialog, order: Orders?, position: Int)
    }

    private fun initOrderData(){
        tv_order_status.text = when{
            this_order.status == ORDER_STATUS_WAITING -> _context.getString(R.string.order_status_waiting)
            this_order.status == ORDER_STATUS_SUCCESS ->{
                if (this_order.customer_debtor <= 0){
                    "پرداخت شده"
                }else "تسویه نشده"
            }
            else -> "سفارش #${this_order.id}"
        }

        tv_business_name.setText(Session.getInstance().businessName)

        tv_customer_name.setText(if (this_order.customer_name.isNullOrEmpty()) _context.getString(R.string.order) else this_order.customer_name)
        tv_customer_phone.setText(if (this_order.customer_phone.isNullOrEmpty()) "#${this_order.id}" else this_order.customer_phone)

        tv_order_code.setText("#${this_order.order_code}")

        initRecyclerView()

        total_order.setText(_context.getString(R.string.all_order_one), this_order.total_price_order)
        total_tax.setText(_context.getString(R.string.tax), this_order.total_tax)
        total_shipping.setText(_context.getString(R.string.shipping_price), this_order.totla_shipping)
        total_discount_free.setText(_context.getString(R.string.price_discount_on_product), this_order.amount_discount)
        total_pay.setText(_context.getString(R.string.amount_pay), this_order.totla_all)

        cash_money.setText(_context.getString(R.string.cash_money_pay), this_order.pay_cash)
        cash_card.setText(_context.getString(R.string.card_pay), this_order.pay_card)
        if (!this_order.pay_card_info.isNullOrEmpty()) {
            cash_card_info.setText(this_order.pay_card_info)
            cash_card_info.visibility = View.VISIBLE
        }
        cash_debit.setText(_context.getString(R.string.debit), this_order.customer_debtor)
        cash_discount.setText(_context.getString(R.string.price_discount_order), this_order.pay_discount_code)
        if (!this_order.discount_code.isNullOrEmpty()) {
            cash_discount_info.setText(this_order.discount_code)
            cash_discount_info.visibility = View.VISIBLE
        }

        total_profit.setText(_context.getString(R.string.profit_order), this_order.total_price_profit)

        tv_date.setText("در تاریخ ${App.getFormattedDate(this_order.update_at?:Date())} ویرایش شده \n در تاریخ ${App.getFormattedDate(this_order.create_at?: Date())} ثبت شده")
    }

    private fun initOnClick(){
        ic_edit.setOnClickListener {
            if (listener == null){
                val i = Intent(_context, AddOrderActivity::class.java)
                i.putExtra("order_id", this_order.id)
                startActivity(i)
                dismiss()
            }else listener.onEditOrder(this,this_order, position!!)
        }
        ic_close.setOnClickListener {
            dismiss()
        }
    }

    private fun initRecyclerView(){
        val arrayList = ArrayList<OrderDetail>(App.database.getAppDao().selectOrdersDetailByOrderCode(App.branch(), this_order.order_code!!))
        recyclerView.adapter = OrderDetailAdapter(_context,arrayList,null)
    }
}
package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.activity.first_open.FirstOpenActivity
import amingoli.com.selar.adapter.BusinessListAdapter
import amingoli.com.selar.adapter.OrderDetailAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Orders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_order_view.*
import kotlinx.android.synthetic.main.include_item_amount_bold.view.*
import java.util.*
import kotlin.collections.ArrayList

class OrderViewDialog(val _context: Context, val order_id:Int, val listener: Listener?) : DialogFragment() {

    private var this_order = App.database.getAppDao().selectOrdersById(order_id)

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
        fun onEditBusiness(dialog: OrderViewDialog, order: Orders?)
    }

    private fun initOrderData(){
        tv_order_status.setText("در انتظار")
//        tv_order_status.setCompoundDrawables(null,null,null,null)

        tv_business_name.setText(Session.getInstance().businessName)

        tv_customer_name.setText(if (this_order.customer_name.isNullOrEmpty()) _context.getString(R.string.order) else this_order.customer_name)
        tv_customer_phone.setText(if (this_order.customer_phone.isNullOrEmpty()) "#${this_order.id}" else this_order.customer_phone)

        tv_order_code.setText("#${this_order.order_code}")

        initRecyclerView()

        total_order.setText(_context.getString(R.string.all_order_one), this_order.total_price_order)
        total_tax.setText(_context.getString(R.string.tax), this_order.total_tax)
        total_shipping.setText(_context.getString(R.string.shipping_price), this_order.totla_shipping)
        total_discount_free.setText(_context.getString(R.string.price_discount), this_order.amount_discount)
        total_pay.setText(_context.getString(R.string.amount_pay), this_order.totla_all)
    }

    private fun initOnClick(){
//        tv_edit.setOnClickListener {
//            listener?.onEditBusiness(this, this_order)
//            dismiss()
//        }
        ic_close.setOnClickListener {
            dismiss()
        }
    }

    private fun initRecyclerView(){
        val arrayList = ArrayList<OrderDetail>(App.database.getAppDao().selectOrdersDetailByOrderCode(this_order.order_code!!))
        recyclerView.adapter = OrderDetailAdapter(_context,arrayList,object : OrderDetailAdapter.Listener{
            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {

            }

            override fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>) {

            }

        })
    }
}
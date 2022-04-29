package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.OrderListHorizontalAdapter
import amingoli.com.selar.adapter.ProductListHorizontalAdapter_3
import amingoli.com.selar.adapter.TagAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_view_customer.*
import java.util.*

class CustomerViewDialog(val _context: Context, val customer_id:Int, val position: Int?, val listener: Listener?) : DialogFragment() {

    private var this_customer = App.database.getAppDao().selectCustomer(customer_id)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_view_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        initData()
        initOnClick()
        initAdapterTagList()
        initRecyclerViewOrder()
        initRecyclerViewProduct()
        chart_bar_price.barChartAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener{
        fun onEditCustomer(dialog: CustomerViewDialog, customer: Customers?, position: Int?)
    }


    private fun initData(){
        tv_id.setText("#${this_customer?.id}")
        tv_customer_name.setText(this_customer?.name)

        tv_order_count.setText("700 سفارش")
        tv_amount_all_order.setText(App.priceFormat(28000000.0,true))
        tv_order_last_date.setText(App.getFormattedDate(Date()))

        tv_date.setText("در تاریخ ${App.getFormattedDate(this_customer?.updated_at)}ویرایش شده \n در تاریخ${App.getFormattedDate(this_customer?.created_at)} ثبت شده")
    }

    private fun initOnClick(){
        ic_close.setOnClickListener {
            dismiss()
        }

        tv_edit.setOnClickListener {
            listener?.onEditCustomer(this,this_customer, position)
        }
    }

    private fun initAdapterTagList(){
        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("۸۵۰,۰۰۰ تومان بدهکار"))
        array_tag.add(TagList("۲۰,۰۰۰ تومان سود سفارشات"))
        array_tag.add(TagList("۱۰,۰۰۰ تومان تخفیف گرفته"))
        array_tag.add(TagList("میانگین سفارش ۲۸۰,۰۰۰ تومان"))
        array_tag.add(TagList("۸۵۰,۰۰۰ تومان خرید نقدی"))
        array_tag.add(TagList("۸۵۰,۰۰۰ تومان خرید کارتخوان"))
        array_tag.add(TagList(" بانک سامان ۱۹۰,۰۰۰ تومان"))
        array_tag.add(TagList("بانک کشاورزی ۸۵,۰۰۰ تومان"))
        array_tag.add(TagList("بانک ملت ۸۲,۰۰۰ تومان"))

        recyclerView_tag.adapter = TagAdapter(_context, array_tag, null)
    }

    private fun initRecyclerViewOrder(){
        recyclerView_order.adapter = OrderListHorizontalAdapter(_context, ArrayList(App.database.getAppDao().selectOrders()),
            object : OrderListHorizontalAdapter.Listener{
                override fun onItemClicked(position: Int, item: Orders) {

                }
            })
    }

    private fun initRecyclerViewProduct(){
        recyclerView_product.adapter = ProductListHorizontalAdapter_3(_context,
            ArrayList(App.database.getAppDao().selectProduct()),
            object : ProductListHorizontalAdapter_3.Listener {
                override fun onItemClicked(position: Int, product: Product) {

                }
            })
    }
}
package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_view_product.*

class ProductViewDialog(val _context: Context, val product_id:Int, val position: Int, val listener: Listener?) : DialogFragment() {

    private var this_product = App.database.getAppDao().selectProduct(product_id)
    private var adapterTag : TagInfoAdapter? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_view_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        initOrderData()
        initOnClick()
        initAdapterTagList()
        chart_bar_price.barChartAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener{
        fun onEditProduct(dialog: ProductViewDialog, product: Product?, position: Int)
    }

    private fun initOrderData(){

    }

    private fun initOnClick(){
        ic_close.setOnClickListener {
            dismiss()
        }

        tv_edit.setOnClickListener {
            listener?.onEditProduct(this,this_product, position)
        }
    }

    private fun initAdapterTagList(){
//        val array_tag = ArrayList<TagList>()
//        array_tag.add(TagList("ویرایش", R.drawable.ic_baseline_extension_24,"all"))
//        array_tag.add(TagList("حذف", R.drawable.ic_baseline_delete_24,"all"))
//
//        adapterTag = TagInfoAdapter(_context,
//            array_tag,
//            object : TagInfoAdapter.Listener {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onItemClicked(position: Int, item: TagList) {
//
//                }
//            })
//
//        recyclerView_tag.adapter = adapterTag
    }

    private fun initRecyclerView(){
//        val arrayList = ArrayList<OrderDetail>(App.database.getAppDao().selectOrdersDetailByOrderCode(this_order.order_code!!))
//        recyclerView.adapter = OrderDetailAdapter(_context,arrayList,object : OrderDetailAdapter.Listener{
//            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {
//
//            }
//
//            override fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>) {
//
//            }
//
//        })
    }
}
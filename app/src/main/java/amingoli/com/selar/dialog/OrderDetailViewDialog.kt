package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.widget.BasketAdder
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_order_detail_view.*


class OrderDetailViewDialog(
    private val _context: Context, val orderDetail: OrderDetail, val position: Int,
    val listener: Listener) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_order_detail_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true
        title.text = orderDetail.name
        if (!orderDetail.product_image.isNullOrEmpty()){
            image.visibility = View.VISIBLE
            Glide.with(_context).load(orderDetail.product_image).into(image)
        }else image.visibility = View.GONE
        basketAdder.show(orderDetail, object : BasketAdder.Listener{
            override fun onChangeBasketAdder(count: Double) {
                orderDetail.stock = count
                if (orderDetail.stock == 0.0){
                    listener.onRemoveOrderDetail(this@OrderDetailViewDialog,position, orderDetail)
                    dismiss()
                }else listener.onEditOrderDetail(this@OrderDetailViewDialog,position, orderDetail)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener {
        fun onEditOrderDetail(dialog: OrderDetailViewDialog,position: Int, orderDetail: OrderDetail)
        fun onRemoveOrderDetail(dialog: OrderDetailViewDialog,position: Int, orderDetail: OrderDetail)
    }
}
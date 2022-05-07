package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.OrderDetail
import android.graphics.Paint
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product_7.view.*
import java.io.File
import java.util.*


class OrderDetailAdapter(
    val context: Context,
    val list: ArrayList<OrderDetail>,
    val listener: Listener?
): RecyclerView.Adapter<OrderDetailAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, orderDetail: OrderDetail)
        fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_7, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]
//        Text Main For Product
        item.tv_title.setText(model.name)
        item.tv_content.setText("${App.stockFormat(model.stock!!)} ${model.increase_name} ✖️ ${App.priceFormat(model.price_sale!!)}")
        item.tv_price.setText(App.priceFormat(model.price_sale!!*model.stock!!))
//        Text Content_2 & Price_2 For Discount
/*        val visibility = if (model.price_discount!= null && model.price_discount!! >= 1) View.VISIBLE else View.GONE
        if (visibility == View.VISIBLE){
            val price_on_product = if (model.price_discount!= null && model.price_discount!! >= 1) model.price_discount!! + model.price_sale!! else 0.0
            val text_content_2 = "${App.stockFormat(model.stock!!)} ${model.increase_name} ✖️ ${App.priceFormat(price_on_product)}"
            item.tv_content_2.paintFlags = item.tv_content_2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            item.tv_content_2.setText(text_content_2)
            item.tv_price_2.setText("${App.priceFormat(model.price_discount!!)} تخفیف")
        }
        item.tv_content_2.visibility = visibility
        item.tv_price_2.visibility = visibility*/
//        Product Image
        if (!model.product_image.isNullOrEmpty()){
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(model.product_image).into(item.image)
        }
//        Listener Item Clicked
        item.setOnClickListener { listener?.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(orderDetail: OrderDetail){

        for (i in 0 until list.size){
            if (list[i].product_id == orderDetail.id){
                list[i].stock = list[i].stock!! + 1.0
                notifyItemChangedBySwap(position_old = i, position_new = list.size-1)
                listener?.onChangeListener(i,list)
                return
            }
        }
        list.add(list.size,orderDetail)
        notifyItemInserted(list.size)
        listener?.onChangeListener(list.size,list)
    }

    private fun notifyItemChangedBySwap(position_old: Int, position_new: Int){
        if (position_old != position_new){
            Collections.swap(list, position_old, position_new)
            notifyItemMoved(position_old, position_new)
            notifyItemChanged(position_new)
        }else{
            notifyItemChanged(position_old)
        }
    }
}
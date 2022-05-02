package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product_2.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AddOrderAdapter(
    val context: Context,
    val ORDER_CODE: String,
    val list: ArrayList<OrderDetail>,
    val listener: Listener
): RecyclerView.Adapter<AddOrderAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, orderDetail: OrderDetail)
        fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>)
        fun onReadyOrderDetailForDatabase(newListOrderDetail: ArrayList<OrderDetail>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_2, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.tv_title.setText(model.name)
        item.tv_content.setText("${App.stockFormat(model.stock!!)} ${model.increase_name} ✖️ ${App.priceFormat(model.price_sale!!)}")
        item.tv_price.setText(App.priceFormat(model.price_sale!!*model.stock!!))

        if (!model.product_image.isNullOrEmpty()){
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(File(model.product_image)).into(item.image)
        }

        item.setOnClickListener { listener.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(product: Product){

        Log.e("aminqqqq", "addItem: ${product.id}" )

        for (i in 0 until list.size){
            if (list[i].product_id == product.id){
                list[i].stock = list[i].stock!! + 1.0
                notifyItemChangedBySwap(position_old = i, position_new = list.size-1)
                listener.onChangeListener(i,list)
                return
            }
        }
        list.add(list.size,convertProductToOrderDetail(product))
        notifyItemInserted(list.size)
        listener.onChangeListener(list.size,list)
    }

    fun populateOrderDetailToDatabase(customer_id :Int?){
        for (i in 0 until list.size){
            list[i].customer_id = customer_id ?: 0
        }
        listener.onReadyOrderDetailForDatabase(list)
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

    private fun convertProductToOrderDetail(p: Product): OrderDetail{
        val price_sale = if (p.price_sale!! > 0 ) p.price_sale else p.price_sale_on_product
        return OrderDetail(null,App.branch(),ORDER_CODE,p.id,p.qrcode,p.image_defult,p.name,1.0,0,p.increase,
            p.price_buy,
            price_sale,
            p.price_discount,p.price_profit,p.tax_percent)
    }
}
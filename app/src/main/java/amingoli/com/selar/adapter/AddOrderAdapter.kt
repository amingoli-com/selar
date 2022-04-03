package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product_2.view.*
import kotlinx.android.synthetic.main.item_product_result_camera.view.*
import kotlinx.android.synthetic.main.item_product_result_camera.view.tv_price
import kotlinx.android.synthetic.main.item_product_result_camera.view.tv_title


class AddOrderAdapter(
    val context: Context,
    val list: ArrayList<OrderDetail>,
    val listener: Listener
): RecyclerView.Adapter<AddOrderAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, orderDetail: OrderDetail)
        fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>)
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

        item.setOnClickListener { listener.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(product: Product){
        for (i in 0 until list.size){
            if (list[i].product_id == product.id){
                list[i].stock = list[i].stock!! + 1.0
                notifyItemChanged(i)
                listener.onChangeListener(i,list)
                return
            }
        }
        list.add(list.size,convertProductToOrderDetail(product))
        notifyItemInserted(list.size)
        listener.onChangeListener(list.size,list)
    }

    private fun convertProductToOrderDetail(p: Product): OrderDetail{
        return OrderDetail(null,p.qrcode,p.id,p.name,1.0,0,p.increase,
            p.price_buy,p.price_sale,p.price_discount,0.0,0.0)
    }
}
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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product_result_camera.view.*
import java.io.File


class AddOrderCameraAdapter(
    val context: Context,
    val list: ArrayList<OrderDetail>,
    val listener: Listener
): RecyclerView.Adapter<AddOrderCameraAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, orderDetail: OrderDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_result_camera, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.tv_title.setText(model.name)
        item.tv_count.setText(App.stockFormat(model.stock!!))
        item.tv_price.setText(App.priceFormat(model.price_sale!!))

        if (!model.product_image.isNullOrEmpty()){
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(File(model.product_image)).into(item.image)
        }

        item.setOnClickListener { listener.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(product: Product){
        for (i in 0 until list.size){
            if (list[i].product_id == product.id){
                list[i].stock = list[i].stock!! + 1.0
                notifyItemChanged(i)
                return
            }
        }
        list.add(list.size,convertProductToOrderDetail(product))
        notifyItemInserted(list.size)
    }

    private fun convertProductToOrderDetail(p: Product): OrderDetail{
        return OrderDetail(null,p.qrcode,p.id,p.image_defult,p.name,1.0,0,p.increase,
            p.price_buy,p.price_sale,p.price_discount,0.0,0.0)
    }
}
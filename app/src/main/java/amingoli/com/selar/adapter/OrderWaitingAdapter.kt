package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_order_waiting.view.*


class OrderWaitingAdapter(
    val context: Context,
    val list: ArrayList<Orders>,
    val listener: Listener?
): RecyclerView.Adapter<OrderWaitingAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, item: Orders)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_waiting, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.title.setText(if (model.customer_name.isNullOrEmpty()) "سفارش #${model.id}" else model.customer_name)
        item.desc.setText(App.priceFormat(model.totla_all))

        if (listener != null) item.setOnClickListener { listener.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Orders){
        list.add(item)
        notifyDataSetChanged()
    }
}
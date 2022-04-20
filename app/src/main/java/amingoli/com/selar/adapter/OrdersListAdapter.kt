package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.Orders
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_order.view.*

class OrdersListAdapter(val context: Context,
                        val list: ArrayList<Orders>,
                        val listener: Listener
) : RecyclerView.Adapter<OrdersListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, item: Orders)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.title.setText(context.getString(R.string.order_customer_name,model.customer_name))
        item.content.text = App.getFormattedDate(model.create_at?.time)
        item.amount.text = App.priceFormat(model.total_price_order!!,true)

        item.background_card.backgroundTintList = if (model.status == 1){
            ContextCompat.getColorStateList(context, R.color.white)
        }else ContextCompat.getColorStateList(context, R.color.red_70)

        item.setOnClickListener {
            listener.onItemClicked(position,model)
        }
    }

    fun addItem(item: Orders){
        list.add(list.size,item)
        notifyItemInserted(list.size)
    }

    fun addItem(item: Orders, position: Int){
        if (position == -1) addItem(item)
        else {
            Log.e("qqq", "addItem status is pos: $position" )
            list[position] = item
            notifyItemChanged(position,item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list : List<Orders>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
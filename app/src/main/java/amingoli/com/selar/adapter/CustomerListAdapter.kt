package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.model.Customers
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_customer.view.*

class CustomerListAdapter(val context: Context,
                          val list: ArrayList<Customers>,
                          val boxAction: Boolean,
                          val listener: Listener
) : RecyclerView.Adapter<CustomerListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, item: Customers, action: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.title.setText("${model.name}")
        item.content.text = model.phone

        item.background_card.backgroundTintList = if (model.status == 0){
            ContextCompat.getColorStateList(context, R.color.red_70)
        }else ContextCompat.getColorStateList(context, R.color.white)

        if (boxAction){
            item.box_action.visibility = View.VISIBLE
            item.box_action.alpha = if (model.phone.isNullOrEmpty()) 0.24f else 1f
            if (!model.phone.isNullOrEmpty()){
                item.action_sms.setOnClickListener {
                    listener.onItemClicked(position,model,"sms")
                }
                item.action_call.setOnClickListener {
                    listener.onItemClicked(position,model,"tel")
                }
            }
        }else{
            item.box_action.visibility = View.GONE
        }
        item.setOnClickListener {
            listener.onItemClicked(position,model,null)
        }
    }

    fun addItem(item: Customers){
        list.add(list.size,item)
        notifyItemInserted(list.size)
    }

    fun addItem(item: Customers, position: Int){
        if (position == -1) addItem(item)
        else {
            Log.e("qqq", "addItem status is pos: $position" )
            list[position] = item
            notifyItemChanged(position,item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list : List<Customers>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main.view.*


class ItemMainAdapter(
    val context: Context,
    val list: ArrayList<TagList>,
    val listener: Listener?
): RecyclerView.Adapter<ItemMainAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, item: TagList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        val s = model.title!!.split("-")

        item.title.setText(s[0].replace("-",""))
        if (s.size > 1) item.desc.setText(s[1].replace("-",""))
        item.content.setText(model.tag)

        if (listener != null) item.setOnClickListener { listener.onItemClicked(position, model) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: TagList){
        list.add(item)
        notifyDataSetChanged()
    }
}
package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_category_2.view.*

class CategoryListAdapter(val context: Context,
                          val list: ArrayList<Category>,
                          val listener: Listener
) : RecyclerView.Adapter<CategoryListAdapter.ListViewHolder>() {

    var position_selected = 0

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, item: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_2, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.title.text = model.name
        if (model.image != null) {
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(model.image!!).into(item.image)
        }
        else item.image.visibility = View.GONE

        item.setOnClickListener {
            val back_position_selected = position_selected
            position_selected = position
            listener.onItemClicked(position,model)
            notifyItemChanged(position_selected, model)
            notifyItemChanged(back_position_selected, model)
        }
    }

    fun addItem(item: Category){
        list.add(list.size,item)
        notifyItemInserted(list.size)
    }

    fun addItem(item: Category, position: Int){
        if (position == -1) addItem(item)
        else {
            Log.e("qqq", "addItem status is pos: $position" )
            list[position] = item
            notifyItemChanged(position,item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list : List<Category>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private fun setItemSelected(is_selected:Boolean, imageView:ImageView, textView : TextView){
        if (is_selected){
            imageView.setBackgroundColor(ContextCompat.getColor(context,R.color.blue))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            textView.setTextColor(ContextCompat.getColor(context,R.color.blue))
        }else{
            imageView.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
            textView.setTextColor(ContextCompat.getColor(context,R.color.black))
        }
    }
}
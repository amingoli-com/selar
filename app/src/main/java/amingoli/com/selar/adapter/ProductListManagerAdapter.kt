package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product_5.view.*
import java.io.File

class ProductListManagerAdapter(
    val context: Context,
    val list: ArrayList<Product>,
    val listener: Listener
) : RecyclerView.Adapter<ProductListManagerAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onEmpty(size : Int)
        fun onItemClicked(position: Int, product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_5, parent, false))
    }

    override fun getItemCount(): Int {
        listener.onEmpty(list.size)
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.title.text = model.name
        item.price_sela.text = App.priceFormat(model.price_sale!!, true)

        if (!model.image_defult.isNullOrEmpty()){
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(File(model.image_defult)).into(item.image)
        }else item.image.visibility = View.GONE

        item.setOnClickListener {
            listener.onItemClicked(position,model)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list : List<Product>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
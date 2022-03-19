package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*
import java.io.File

class ProductListManagerAdapter(
    val context: Context,
    val list: ArrayList<Product>,
    val listener: Listener
) : RecyclerView.Adapter<ProductListManagerAdapter.ListViewHolder>() {

    private var packId = -1

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        try {
            item.title.text = model.name
            item.price_sela.text = App.priceFormat(model.price_sale!!, true)
            item.price_on_product.text = App.priceFormat(model.price_sale_on_product!!, true)
            item.price_buy.text = App.priceFormat(model.price_buy!!, true)

            item.stock.text = "${App.priceFormat(model.stock!!)} ${model.increase}"
            item.barcode.text = model.qrcode

        }catch (e : Exception){
        }

        if (!model.image_defult.isNullOrEmpty()){
            item.image.visibility = View.VISIBLE
            Glide.with(context).load(File(model.image_defult)).into(item.image)
        }else item.image.visibility = View.GONE

        item.setOnClickListener {
            listener.onItemClicked(position,model)
        }
    }
}
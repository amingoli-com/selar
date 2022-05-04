package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product_3.view.*
import java.io.File

class ProductListAdapter(
    val context: Context,
    val list: ArrayList<Product>,
    val listener: Listener
) : RecyclerView.Adapter<ProductListAdapter.ListViewHolder>() {

    private var packId = -1

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_3, parent, false))
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
            item.price_sela.text = App.priceFormat(model.price_sale!!)
            item.price_on_product.text = App.priceFormat(model.price_sale_on_product!!)
//            item.basketAdder.setText("10 عدد")

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
package amingoli.com.selar.adapter

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
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
import kotlinx.android.synthetic.main.item_category.view.*
import java.io.File

class CategoryListManagerAdapter(
    val context: Context,
    val list: ArrayList<Category>,
    val listener: Listener
) : RecyclerView.Adapter<CategoryListManagerAdapter.ListViewHolder>() {

    private var packId = -1

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onItemClicked(position: Int, category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        try {
            if (model.status == 0) item.alpha = 0.24f
            else item.alpha = 1f

            item.title.text = model.name
            item.content.text = model.content
            Glide.with(context).load(File(model.image)).into(item.image)
        }catch (e : Exception){

        }


        item.setOnClickListener {
            listener.onItemClicked(position,model)
        }
    }
}
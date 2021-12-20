package ir.trano.keeper.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.trano.keeper.R
import ir.trano.keeper.model.Image
import kotlinx.android.synthetic.main.item_image.view.*


class ImagesAdapter(
    val context: Context,
    val list: ArrayList<Image>,
    val onClick: listener): RecyclerView.Adapter<ImagesAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface listener{
        fun openImage(image:String)
        fun addImage()
        fun delete(position: Int, element: Image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = list[position]

        item.delete.setOnClickListener { onClick.delete(position, model) }
        item.tv_add_image.setOnClickListener {
            onClick.addImage()
        }

        when {
            model.bitmap != null -> {
                item.image.setImageBitmap(model.bitmap)
            }
            model.url != null -> {
                val picasso = Picasso.get()
                picasso.load(model.url).into(item.image)
            }
            else -> Log.d("amin78", "onBindViewHolder: ")
        }

        item.image.setOnClickListener {
            if (model.file != null){
                onClick.openImage(model.file!!.path)
            }else if (model.url != null){
                onClick.openImage(model.url!!)
            }
        }
    }

    fun addList(image: ArrayList<Image>){
        list.addAll(image)
        notifyDataSetChanged()
    }

    fun updateList(image: Image){
        list.add(image)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyDataSetChanged()
    }

    @JvmName("getList1")
    fun getList() : ArrayList<Image>{
        return list
    }
}
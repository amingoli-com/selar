package ir.trano.keeper.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import ir.trano.keeper.R
import ir.trano.keeper.dialog.ConfirmDialog
import ir.trano.keeper.model.Loads
import ir.trano.keeper.model.Packings
import ir.trano.keeper.model.Region
import ir.trano.keeper.helper.App
import ir.trano.keeper.widget.NumberTextWatcher
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_product.view.atc_type
import kotlinx.android.synthetic.main.item_product.view.edt_count
import kotlinx.android.synthetic.main.item_product.view.edt_title
import kotlinx.android.synthetic.main.item_product.view.edt_weight
import kotlin.toString as toString

class ProductsAdapter(
    val activity: Activity,
    val context: Context,
    val id_order: Int,
    var editable: Boolean,
    var canRemove: Boolean,
    val listPack: ArrayList<Packings.PackingsItem>,
    var loadList: ArrayList<Loads>,
    val listener: Listener
) : RecyclerView.Adapter<ProductsAdapter.ListViewHolder>() {

    private var packId = -1

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Listener{
        fun onUpdateLoad(position_load: Int, load: Loads)
        fun onRemoveLoad(position_load: Int, id_load: Int,removeOrder: Int, dialog: Dialog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return loadList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = holder.itemView
        val model = loadList[position]

        packId = model.packing!!.id!!
        initPackList(item.atc_type)
        item.btn_close.visibility = View.GONE
        item.btn_edit.visibility = View.GONE
        item.btn_edit.hideLoader()
        item.edt_title.isEnabled = false
        item.atc_type.isEnabled = false
        item.edt_count.isEnabled = false
        item.edt_weight.isEnabled = false

        if (editable){
            item.icon_edit.visibility = View.VISIBLE
        }else {
            item.icon_edit.visibility = View.GONE
        }

        if (canRemove){
            item.icon_remove.visibility = View.VISIBLE
        }else item.icon_remove.visibility = View.GONE


        item.edt_count.addTextChangedListener(NumberTextWatcher(item.edt_count))
        item.edt_weight.addTextChangedListener(NumberTextWatcher(item.edt_weight))

        item.edt_title.setText(model.title)
        if (model.packing != null) item.atc_type.setText(model.packing!!.title)
        if (model.count != null) item.edt_count.setText(model.count.toString())
        if (model.weight != null) item.edt_weight.setText(model.weight.toString())

        item.icon_edit.setOnClickListener {
            item.edt_title.requestFocus()
            item.edt_title.isEnabled = true
            item.atc_type.isEnabled = true
            item.edt_count.isEnabled = true
            item.edt_weight.isEnabled = true
            item.btn_edit.visibility = View.VISIBLE
            item.btn_close.visibility = View.VISIBLE
            item.icon_edit.visibility = View.GONE
            item.icon_remove.visibility = View.GONE
        }

        item.btn_close.setOnClickListener {
            notifyItemChanged(position)
        }

        item.btn_edit.btn.setOnClickListener {
            if (App.formIsValid(item.edt_title,item.atc_type,packId,item.edt_count,item.edt_weight)){

                val newLoad = Loads()
                newLoad.id = model.id
                newLoad.title = App.getTextEdt(item.edt_title)
                newLoad.count = App.getTextPriceOrIntForce(item.edt_count).toInt()
                newLoad.weight = App.getTextPriceOrIntForce(item.edt_weight).toDouble()
                newLoad.packing = model.packing
                newLoad.packing?.title = App.getTextEdt(item.atc_type)

                listener.onUpdateLoad(position,newLoad)
                item.btn_edit.showLoader()
            }
        }

        item.icon_remove.setOnClickListener {
            showConfirmDialog(position,model.id!!)
        }
    }

    /**
     *  Function
     * */
    private fun initPackList(atc_type: AutoCompleteTextView) {
        val region = ArrayList<Region>()
        for (i in 0 until listPack.size) {
            region.add(Region(listPack[i].id!!, listPack[i].title!!))
        }
        val adapter = RegionAdaptor(context, R.layout.list_item, region, packId)
        atc_type.setAdapter(adapter)
        atc_type.setOnItemClickListener { parent, view, position, id ->
            var r: Region = parent.getItemAtPosition(position) as Region
            packId = r.id
            atc_type.error = null
        }
        atc_type.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) atc_type.showDropDown()
        }
        atc_type.setOnClickListener { atc_type.showDropDown() }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setArrayList(list: ArrayList<Loads>){
        loadList = list
        notifyDataSetChanged()
    }

    fun setArrayList(list: ArrayList<Loads>, position_load: Int){
        loadList = list
        notifyItemChanged(position_load)
    }

    fun setItem(load: Loads){
        loadList.add(load)
        notifyDataSetChanged()
    }

    /**
     * Dialog
     * */
    private fun showConfirmDialog(position: Int, id_load: Int) {
        Log.e("qqqq", "load size " + loadList.size)
        val removeOrder = if (loadList.size == 1 ) id_order else -1

        val dialog = ConfirmDialog(context, object : ConfirmDialog.Listener{
            override fun delete(dialog: Dialog) {
                listener.onRemoveLoad(position, id_load,removeOrder, dialog)
            }
        })
        dialog.show()
    }
}
package amingoli.com.selar.widget.select_product

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListAdapter
import amingoli.com.selar.adapter.ProductListHorizontalAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.widget_select_product.view.*

class SelectProduct (context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var listener: Listener? = null
    private var listProducts = ArrayList<Product>(App.database.getAppDao().selectProduct())
    private var listCategory = ArrayList<Category>(App.database.getAppDao().selectUnderCategory(0))
    private var adapterProduct : ProductListHorizontalAdapter? = null
    private var adapterTagList : CategoryListAdapter? = null

    init {
        View.inflate(context, R.layout.widget_select_product, this)
        initAdapter()
        initRecyclerCategory()
        initRecyclerProduct()
    }

    interface Listener{
        fun onCategory(category: Category)
        fun onProduct(product: Product)
    }

    fun setListener(listener: Listener){
        this.listener = listener
    }

    private fun initAdapter(){
        adapterTagList = CategoryListAdapter(context, ArrayList(), object : CategoryListAdapter.Listener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int, item: Category) {
                listener?.onCategory(category = item)
                
                adapterProduct?.updateList(App.database.getAppDao().selectProductByCategory(item.id!!))
                adapterTagList?.updateList(App.database.getAppDao().selectUnderCategory(item.id!!), item.id_mother!!)
            }
        })

        adapterProduct = ProductListHorizontalAdapter(context,listProducts,object : ProductListHorizontalAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {
                listener?.onProduct(product)
            }
        })
    }

    private fun initRecyclerCategory() {


        recyclerView_category.adapter = adapterTagList
    }

    private fun initRecyclerProduct(){
        recyclerView_product.adapter = adapterProduct
    }
}
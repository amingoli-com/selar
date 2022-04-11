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
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.widget_select_product.view.*

class SelectProduct (context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.widget_select_product, this)
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

    private fun initRecyclerCategory() {
        val categoryList = ArrayList<Category>(App.database.getAppDao().selectUnderCategory(0))
        val adapterTagList = CategoryListAdapter(context,
            categoryList,
            object : CategoryListAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: Category) {
                    listener?.onCategory(category = item)
                }
            })

        recyclerView_category.adapter = adapterTagList
    }

    private fun initRecyclerProduct(){
        val listProducts = ArrayList<Product>(App.database.getAppDao().selectProduct())
        recyclerView_product.adapter = ProductListHorizontalAdapter(context,listProducts,object : ProductListHorizontalAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {
                listener?.onProduct(product)
            }

        })
    }
}
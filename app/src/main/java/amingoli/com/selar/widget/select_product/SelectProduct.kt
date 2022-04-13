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
    private var listCategoryForBack = ArrayList<Int>()
    private var adapterProduct : ProductListHorizontalAdapter? = null
    private var adapterTagList : CategoryListAdapter? = null

    init {
        View.inflate(context, R.layout.widget_select_product, this)
        initAdapter()
        initRecyclerCategory()
        initRecyclerProduct()

//        Test
        ic_back.setOnClickListener {
            if (!listCategoryForBack.isNullOrEmpty()){
                val pos = listCategoryForBack.size-1

                adapterTagList?.updateList(App.database.getAppDao().selectUnderCategory(listCategoryForBack[pos]))
                if (listCategoryForBack[pos] == 0){
                    adapterProduct?.updateList(App.database.getAppDao().selectProduct())
                }else adapterProduct?.updateList(App.database.getAppDao().selectProductByCategory(listCategoryForBack[pos]))


                listCategoryForBack.removeAt(pos)
            }
            initVisibilityIcBack()
        }
    }

    private fun initVisibilityIcBack(){
        ic_back.visibility = if (listCategoryForBack.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    interface Listener{
        fun onCategory(category: Category)
        fun onProduct(product: Product)
    }

    fun setListener(listener: Listener){
        this.listener = listener
    }

    private fun initAdapter(){
        adapterTagList = CategoryListAdapter(context, listCategory, object : CategoryListAdapter.Listener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int, item: Category) {
                listener?.onCategory(category = item)

//                Test
                listCategoryForBack.add(listCategoryForBack.size, item.id_mother!!)
                initVisibilityIcBack()

                adapterProduct?.updateList(App.database.getAppDao().selectProductByCategory(item.id!!))
                adapterTagList?.updateList(App.database.getAppDao().selectUnderCategory(item.id!!))
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
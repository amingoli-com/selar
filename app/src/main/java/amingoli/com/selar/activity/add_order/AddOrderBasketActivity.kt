package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListAdapter
import amingoli.com.selar.adapter.ProductListAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_order_basket.*

class AddOrderBasketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_basket)

        initRecyclerCategory()
        initRecyclerProduct()
    }

    private fun initRecyclerCategory() {
        val categoryList = ArrayList<Category>(App.database.getAppDao().selectUnderCategory(0))
        val adapterTagList = CategoryListAdapter(this,
            categoryList,
            object : CategoryListAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: Category) {
                }
            })

        recyclerView_category.adapter = adapterTagList
    }

    private fun initRecyclerProduct(){
        val listProducts = ArrayList<Product>(App.database.getAppDao().selectProduct())
        recyclerView_product.adapter = ProductListAdapter(this,listProducts,object : ProductListAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {
            }

        })
    }
}
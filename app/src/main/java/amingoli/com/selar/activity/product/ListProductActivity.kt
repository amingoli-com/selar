package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.adapter.ProductListManagerAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_product.*
import kotlinx.android.synthetic.main.item_product.view.*

class ListProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        toolbar.title.text = "محصولات"

        val listProducts = ArrayList<Product>(App.database.getAppDao().selectProduct())
        recyclerView.adapter = ProductListManagerAdapter(this,listProducts,object : ProductListManagerAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {

            }

        })
    }
}
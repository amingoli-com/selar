package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.adapter.ProductListManagerAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_list_product.*
import kotlinx.android.synthetic.main.item_product.view.title
import kotlinx.android.synthetic.main.item_toolbar.view.*

class ListProductActivity : AppCompatActivity() {

    val listProducts = ArrayList<Product>(App.database.getAppDao().selectSmallSizeProduct())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        initToolbar()
        initAdapterTagList()
        initAdapterListProducts()

        if (listProducts.isNullOrEmpty()) statuser.onEmpty()
    }

    private fun initToolbar(){
        toolbar.title.text = "محصولات"
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            startActivity(Intent(this,ProductActivity::class.java))
        }
    }

    private fun initAdapterTagList(){

    }

    private fun initAdapterListProducts(){
        recyclerView.adapter = ProductListManagerAdapter(this,listProducts,object : ProductListManagerAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {
                val i = Intent(this@ListProductActivity, ProductActivity::class.java)
                i.putExtra("id_product", product.id)
                startActivity(i)
            }

        })
    }
}
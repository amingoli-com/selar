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
import kotlinx.android.synthetic.main.activity_list_product.recyclerView
import kotlinx.android.synthetic.main.activity_list_product.recyclerView_tag
import kotlinx.android.synthetic.main.activity_list_product.toolbar
import kotlinx.android.synthetic.main.item_product.view.title
import kotlinx.android.synthetic.main.item_toolbar.view.*

class ListProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        initToolbar()
        initAdapterTagList()
        initAdapterListProducts()
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

        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("همه", R.drawable.ic_baseline_category_24,"all"))
        array_tag.add(TagList("پرفروش", R.drawable.ic_baseline_extension_24,""))
        array_tag.add(TagList("منتشرشده", R.drawable.ic_baseline_category_24,""))
        array_tag.add(TagList("موجود", R.drawable.ic_baseline_category_24,""))
        array_tag.add(TagList("درحال‌اتمام", R.drawable.ic_baseline_category_24,"out_of_stock_alarm"))
        array_tag.add(TagList("درحال‌انقضا", R.drawable.ic_baseline_category_24,"date_expire_alarm"))
        array_tag.add(TagList("تمام‌شده", R.drawable.ic_baseline_category_24,"out_of_stock"))

        val adapterTagList = TagInfoAdapter(this,
            array_tag,
            object : TagInfoAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
                }
            })

        recyclerView_tag.adapter = adapterTagList
    }

    private fun initAdapterListProducts(){
        val listProducts = ArrayList<Product>(App.database.getAppDao().selectProduct())
        recyclerView.adapter = ProductListManagerAdapter(this,listProducts,object : ProductListManagerAdapter.Listener{
            override fun onItemClicked(position: Int, product: Product) {

            }

        })
    }
}
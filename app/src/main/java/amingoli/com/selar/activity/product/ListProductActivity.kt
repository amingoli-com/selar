package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.adapter.*
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_list_product.*
import kotlinx.android.synthetic.main.item_product.view.title
import kotlinx.android.synthetic.main.item_toolbar.view.*
import kotlinx.android.synthetic.main.item_toolbar.view.ic_back
import kotlinx.android.synthetic.main.widget_select_product.view.*

class ListProductActivity : AppCompatActivity() {

    private var listCategoryForBack = ArrayList<TagList>()
    private var adapterProduct : ProductListManagerAdapter? = null
    private var adapterTagList : CategoryListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        initToolbar()
        initAdapter()
        initRecyclerCategory()
        initRecyclerProduct()
    }

    private fun initToolbar(){
        toolbar.title.text = resources.getString(R.string.toolbar_title_product)
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            startActivity(Intent(this,ProductActivity::class.java))
        }
    }

    private fun initVisibilityIcBack(){
        tv_back_category.visibility = if (listCategoryForBack.isNullOrEmpty()) View.GONE else View.VISIBLE
        if (tv_back_category.visibility == View.VISIBLE){
            tv_back_category.setText(listCategoryForBack[listCategoryForBack.size-1].title)
        }
    }

    private fun initAdapter(){
        adapterTagList = CategoryListAdapter(this,
            ArrayList(App.database.getAppDao().selectUnderCategory(0)),
            object : CategoryListAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: Category) {
//                Test
                    listCategoryForBack.add(listCategoryForBack.size, TagList(item.name,item.id_mother.toString()))
                    initVisibilityIcBack()

                    adapterProduct?.updateList(App.database.getAppDao().selectSmallSizeProductByCategory(item.id!!))
                    adapterTagList?.updateList(App.database.getAppDao().selectUnderCategory(item.id!!))
                }
            })

        adapterProduct = ProductListManagerAdapter(this,
            ArrayList(App.database.getAppDao().selectSmallSizeProduct()),
            object : ProductListManagerAdapter.Listener{
                override fun onEmpty(size: Int) {
                    if (size == 0) statuser.onEmpty()
                    else statuser.onFinish()
                }

                override fun onItemClicked(position: Int, product: Product) {
                    val i = Intent(this@ListProductActivity, ProductActivity::class.java)
                    i.putExtra("id_product", product.id)
                    startActivity(i)
                }

            })
    }

    private fun initRecyclerCategory() {
        recyclerView_category.adapter = adapterTagList

        tv_back_category.setOnClickListener {
            if (!listCategoryForBack.isNullOrEmpty()){
                val pos = listCategoryForBack.size-1
                adapterTagList?.updateList(App.database.getAppDao().selectUnderCategory(listCategoryForBack[pos].tag!!.toInt()))
                if (listCategoryForBack[pos].tag!!.toInt() == 0){
                    adapterProduct?.updateList(App.database.getAppDao().selectProduct())
                }else adapterProduct?.updateList(App.database.getAppDao().selectProductByCategory(listCategoryForBack[pos].tag!!.toInt()))
                listCategoryForBack.removeAt(pos)
            }
            initVisibilityIcBack()
        }
    }

    private fun initRecyclerProduct(){
        recyclerView_product.adapter = adapterProduct
    }
}
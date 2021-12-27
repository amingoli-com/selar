package amingoli.com.selar.activity.category

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListManagerAdapter
import amingoli.com.selar.adapter.ProductListManagerAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initToolbar("دسته بندی ها")

        val list = ArrayList<Category>(App.database.getAppDao().selectCategory())
        recyclerView.adapter = CategoryListManagerAdapter(this,list,object : CategoryListManagerAdapter.Listener{
            override fun onItemClicked(position: Int, category: Category) {

            }

        })
    }

    private fun initToolbar(title:String){
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            val c = Category()
            c.name = "مواد غذایی"
            c.content = "۱۸۰۰ محصول"
        }
    }
}
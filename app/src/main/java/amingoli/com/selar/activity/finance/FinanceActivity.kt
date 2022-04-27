package amingoli.com.selar.activity.finance

import amingoli.com.selar.R
import amingoli.com.selar.adapter.OrderWaitingAdapter
import amingoli.com.selar.adapter.ProductListHorizontalAdapter_2
import amingoli.com.selar.adapter.TagAdapter
import amingoli.com.selar.dialog.ProductViewDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_finance.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class FinanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance)

        initToolbar(getString(R.string.finance_toolbar))
        chart_bar_price.barChartAdapter()
        initTagInfo()
        initAdapterOrders()
        initAdapterProduct()
    }


    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
    }

    private fun initTagInfo(){
        val array_tag_info = ArrayList<TagList>()
        array_tag_info.add(TagList("درآمد نقدی ۲۹۰,۰۰۰ تومان"))
        array_tag_info.add(TagList("درآمد کارتخوان ۲۹۰,۰۰۰ تومان"))
        array_tag_info.add(TagList("نسیه ۲۹۰,۰۰۰ تومان"))
        array_tag_info.add(TagList("تخفیف ۲۹۰,۰۰۰ تومان"))
        array_tag_info.add(TagList("هزینه ارسال ۲۹۰,۰۰۰ تومان"))
        recyclerView_tag_info.adapter = TagAdapter(this,array_tag_info,null)
    }

    private fun initAdapterProduct(){
        recyclerView_product.adapter = ProductListHorizontalAdapter_2(this,
            ArrayList(App.database.getAppDao().selectProduct()),
            object : ProductListHorizontalAdapter_2.Listener {
                override fun onItemClicked(position: Int, product: Product) {
                    ProductViewDialog(this@FinanceActivity,product.id!!,null)
                        .show(supportFragmentManager,"order_view")
                }
            })
    }

    private fun initAdapterOrders(){
        recyclerView_order.adapter = OrderWaitingAdapter(this, ArrayList(App.database.getAppDao().selectOrders()),
            object : OrderWaitingAdapter.Listener{
                override fun onItemClicked(position: Int, item: Orders) {

                }
            })
    }
}
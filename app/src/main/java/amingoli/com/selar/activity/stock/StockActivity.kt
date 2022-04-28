package amingoli.com.selar.activity.stock

import amingoli.com.selar.R
import amingoli.com.selar.activity.add_order.AddOrderActivity
import amingoli.com.selar.adapter.*
import amingoli.com.selar.dialog.OrderViewDialog
import amingoli.com.selar.dialog.ProductViewDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_stock.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class StockActivity : AppCompatActivity() {

    private var adapter : ProductListAdapter_2? =null
    private var adapterTag : TagInfoAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)


        initToolbar(getString(R.string.stock_toolbar))
        initAdapterTagList()
        initTagInfo()
        initAdapterOrders()
        barChartAdapter()

    }



    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
    }

    private fun initTagInfo(){
        val array_tag_info = ArrayList<TagList>()
        array_tag_info.add(TagList("۳۰۰ قلم کالا"))
        array_tag_info.add(TagList("۱۲۰ محصول فعال"))
        array_tag_info.add(TagList("۲۹۰,۰۰۰,۰۰۰ تومان سرمایه انبار"))
        recyclerView_info.adapter = TagAdapter(this,array_tag_info,null)
    }

    val array_tag = ArrayList<TagList>()
    private fun initAdapterTagList(){
        array_tag.add(TagList("درحال فروش", R.drawable.ic_baseline_storefront_24,"all"))
        array_tag.add(TagList("درحال انتقضا", R.drawable.ic_baseline_browse_gallery_24,"all"))
        array_tag.add(TagList("درحال اتمام", R.drawable.ic_baseline_error_24,"all"))
        array_tag.add(TagList("منقضی شده", R.drawable.ic_baseline_calendar_month_24,"all"))
        array_tag.add(TagList("تمام شده", R.drawable.ic_baseline_extension_24,"all"))
        array_tag.add(TagList("بیشترین فروش", R.drawable.ic_baseline_monetization_on_24,"all"))
        array_tag.add(TagList("کمترین فروش", R.drawable.ic_percent_black_24dp,"all"))
        array_tag.add(TagList("بیشترین سود", R.drawable.ic_baseline_add_circle_24,"all"))
        array_tag.add(TagList("کمترین سود", R.drawable.ic_baseline_remove_circle_24,"all"))
        array_tag.add(TagList("بیشترین موجودی", R.drawable.ic_baseline_category_24,"all"))

        adapterTag = TagInfoAdapter(this,
            array_tag,
            object : TagInfoAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
//                    adapter?.updateList(selectOrder(item.tag!!))
                }
            })

        recyclerView_tag.adapter = adapterTag
    }

    private fun initAdapterOrders(){
        adapter = ProductListAdapter_2(this,
            ArrayList(selectOrder("all")),
            object : ProductListAdapter_2.Listener {
                override fun onItemClicked(position: Int, product: Product) {
                    ProductViewDialog(this@StockActivity,product.id!!,position,null)
                        .show(supportFragmentManager,"order_view")
                }
            })
        recyclerView.adapter = adapter
    }

    private fun selectOrder(query: String) : List<Product>{
        return when(query){
            "all"->         App.database.getAppDao().selectProduct()
            else ->         App.database.getAppDao().selectProduct()
        }
    }

//    Test

    private var barEntryArrayList: ArrayList<BarEntry> = ArrayList()
    private var labelNames: ArrayList<String> = ArrayList()
    private fun barChartAdapter() {
        for (i in 0 until array_tag.size) {
            val sales: Int = i+100*i
            barEntryArrayList.add(BarEntry(i.toFloat(), sales.toFloat()))
            labelNames.add(array_tag[i].title!!)
        }
        val barDataSet = BarDataSet(barEntryArrayList, "فروش ماهانه")
        barDataSet.color = resources.getColor(R.color.primary)
        val description = Description()
        description.setText("فروش ۱۵ روز اخیر")
        barChart?.setDescription(description)
        val barData = BarData(barDataSet)
        barChart?.setData(barData)
        val xAxis: XAxis = barChart?.xAxis!!
        xAxis.valueFormatter = IndexAxisValueFormatter(labelNames)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelNames.size
        xAxis.labelRotationAngle = 270f
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
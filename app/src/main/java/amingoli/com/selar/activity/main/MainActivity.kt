package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.add_order.AddOrderActivity
import amingoli.com.selar.activity.category.CategoryActivity
import amingoli.com.selar.activity.customer.CustomerActivity
import amingoli.com.selar.activity.order.OrderActivity
import amingoli.com.selar.activity.product.ListProductActivity
import amingoli.com.selar.activity.product.ProductActivity
import amingoli.com.selar.adapter.ItemMainAdapter
import amingoli.com.selar.adapter.OrderWaitingAdapter
import amingoli.com.selar.dialog.BusinessMenuDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.TagList
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity(), ItemMainAdapter.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

//        test
        initRecyclerViewOrderWaiting()
        initRecyclerViewItemMain()
        barChartAdapter()


//        test add data
        for (i in 11 .. 20){
            App.database.getAppDao().insertOrder(Orders(
                1,
                Session.getInstance().branch,
                129098200.0,
                2000.0,
                "احمد $i",
                i*1000.0,
                i*10000.0,
                0.0,"saman bank",
                0.0,
                29000.0,
                Date()))
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initToolbar(){
        toolbar.content.setOnClickListener {
            BusinessMenuDialog(this,object : BusinessMenuDialog.Listener{
                override fun onEditBusiness(dialog: BusinessMenuDialog, business: Business?) {
                    initDataSetText()
                }
                override fun onBusinessList(dialog: BusinessMenuDialog, business: Business) {
                    initData()
                }
            }).show(supportFragmentManager,"menu")
        }
    }

    private fun initData(){
        initDataSetText()
    }

    private fun initDataSetText(){
        toolbar.title.setText(resources.getString(R.string.welcome_owner,Session.getInstance().businessOwnerName))
        toolbar.content.setText(resources.getString(R.string.welcome_to_business,Session.getInstance().businessName))
    }

//    test
    private fun initRecyclerViewOrderWaiting(){
        val arrayList = ArrayList<TagList>()
        arrayList.add(TagList("محمدحسین آقایی","290,000 تومان"))
        arrayList.add(TagList("مشتری متفرقه","900,000 تومان"))
        arrayList.add(TagList("اصغر فرهادی","870,000 تومان"))
        arrayList.add(TagList("علیرضا اسماعیلی","20,000 تومان"))
        arrayList.add(TagList("میز ۲","170,000 تومان"))
        arrayList.add(TagList("میز ۸","6,200,900 تومان"))
        recyclerView_order_waiting.adapter = OrderWaitingAdapter(this,arrayList,null)
    }

    private fun initRecyclerViewItemMain(){
        val arrayList = ArrayList<TagList>()
        arrayList.add(TagList("محصولات-همه محصولات ثبت شده","290,000 محصول"))
        arrayList.add(TagList("سفارشات-همه سفارشات انجام شده","13,290 سفارش"))
        arrayList.add(TagList("دسته‌بندی-دسته‌بندی‌های فعال","290 دسته‌بندی"))
        arrayList.add(TagList("مشتریان-همه خریداران شما","1,290 مشتری"))
        arrayList.add(TagList("گزارشات انبار-کالاهای موجود","18,290 کالا"))
        arrayList.add(TagList("گزارشات مالی-سرمایه موجود","180,920,290 تومان"))
        arrayList.add(TagList("تنظیمات-",""))
        arrayList.add(TagList("پشتیبانی-",""))
        recyclerView.adapter = ItemMainAdapter(this,arrayList,this)
    }


    private var barEntryArrayList: ArrayList<BarEntry> = ArrayList()
    private var labelNames: ArrayList<String> = ArrayList()

    private fun barChartAdapter() {
        for (i in 0 until 10) {
            val month: String = "فروردین"
            val sales: Int = i+10000*i
            barEntryArrayList.add(BarEntry(i.toFloat(), sales.toFloat()))
            labelNames.add(month)
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
        barChart.animateY(2000)
        barChart.invalidate()
    }

    /**
     * Listener
     * */
    override fun onItemClicked(position: Int, item: TagList) {
        when(position){
            0 -> startActivity(Intent(this, ListProductActivity::class.java))
            1 -> startActivity(Intent(this, OrderActivity::class.java))
            2 -> startActivity(Intent(this, CategoryActivity::class.java))
            3 -> startActivity(Intent(this, CustomerActivity::class.java))
        }
    }
}
package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.category.CategoryActivity
import amingoli.com.selar.activity.customer.CustomerActivity
import amingoli.com.selar.activity.finance.FinanceActivity
import amingoli.com.selar.activity.order.OrderActivity
import amingoli.com.selar.activity.product.ListProductActivity
import amingoli.com.selar.activity.setting.SettingActivity
import amingoli.com.selar.activity.stock.StockActivity
import amingoli.com.selar.activity.support.SupportActivity
import amingoli.com.selar.adapter.ItemMainAdapter
import amingoli.com.selar.adapter.OrderWaitingAdapter
import amingoli.com.selar.dialog.BusinessMenuDialog
import amingoli.com.selar.dialog.OrderViewDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
import amingoli.com.selar.model.MainModel
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.TagList
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main_toolbar.view.*


class MainActivity : AppCompatActivity(), ItemMainAdapter.Listener, OnChartValueSelectedListener {

    private var ordersWaiting = ArrayList<Orders>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

//        test
        initRecyclerViewOrderWaiting()
        initRecyclerViewItemMain()
        barChartAdapter()

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
        initVisibilityOrderWaiting()
    }

    private fun initDataSetText(){
        toolbar.title.setText(resources.getString(R.string.welcome_owner,Session.getInstance().businessOwnerName))
        toolbar.content.setText(resources.getString(R.string.welcome_to_business,Session.getInstance().businessName))
    }

    private fun initVisibilityOrderWaiting(){
        if (!ordersWaiting.isNullOrEmpty()){
            box_order_waiting.visibility = View.VISIBLE
        }else if (box_order_waiting.visibility != View.GONE) box_order_waiting.visibility = View.GONE
    }

//    test
    private fun initRecyclerViewOrderWaiting(){
        ordersWaiting = ArrayList(App.database.getAppDao().selectOrders(ORDER_STATUS_WAITING))
        recyclerView_order_waiting.adapter = OrderWaitingAdapter(this,ordersWaiting,object : OrderWaitingAdapter.Listener{
            override fun onItemClicked(position: Int, item: Orders) {
                OrderViewDialog(this@MainActivity,item.id!!,null, null)
                    .show(supportFragmentManager,"order_view")
            }

        })
    }

    private fun initRecyclerViewItemMain(){
        val arrayList = ArrayList<MainModel>()
        arrayList.add(MainModel(R.drawable.ic_baseline_extension_24,"محصولات","محصولات ثبت شده","290,000 محصول", ListProductActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_shopping_cart_24,"سفارشات","سفارشات انجام شده","290 سفارش", OrderActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_category_24,"دسته‌بندی","دسته‌بندی های ثبت شده","290,000 محصول", CategoryActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_account_circle_black_24dp,"مشتریان","خریداران شما","290,000 محصول", CustomerActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_storefront_24,"گزارش انبار","کالاهای موجود","290,000 محصول", StockActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_monetization_on_24,"گزارش مالی","سرمایه موجود","290,000 محصول", FinanceActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_settings_24,"تنظیمات","مالیات، واحدپول و..","", SettingActivity::class.java))
        arrayList.add(MainModel(R.drawable.ic_baseline_import_contacts_24,"پشتیبانی","راه های ارتباطی","", SupportActivity::class.java))
        recyclerView.adapter = ItemMainAdapter(this,arrayList,this)
    }

//    Test
    private var barEntryArrayList: ArrayList<BarEntry> = ArrayList()
    private var labelNames: ArrayList<String> = ArrayList()
    private fun barChartAdapter() {
        for (i in 0 until 10) {
            val month: String = "$i فروردین"
            val sales: Int = i+10*i
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
        barChart.animateY(1000)
        barChart.invalidate()
    }

    /**
     * Listener
     * */
    override fun onItemClicked(position: Int, item: MainModel) {
        startActivity(Intent(this, item.action))
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }
}
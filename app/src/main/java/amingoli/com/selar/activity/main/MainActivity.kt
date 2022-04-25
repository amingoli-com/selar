package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.category.CategoryActivity
import amingoli.com.selar.activity.customer.CustomerActivity
import amingoli.com.selar.activity.order.OrderActivity
import amingoli.com.selar.activity.product.ListProductActivity
import amingoli.com.selar.adapter.ItemMainAdapter
import amingoli.com.selar.adapter.OrderWaitingAdapter
import amingoli.com.selar.dialog.BusinessMenuDialog
import amingoli.com.selar.dialog.OrderViewDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
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
        pieChartAdapter()

//        test add data
        for (i in 1 .. 50){
//            App.database.getAppDao().insertOrder(Orders(
//                1,
//                Session.getInstance().branch,
//                129098200.0,
//                2000.0,
//                "احمد $i",
//                "0919519137$i",
//                i*1000.0,
//                i*10000.0,
//                0.0,"saman bank",
//                29000.0,
//                Date()))
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
                OrderViewDialog(this@MainActivity,item.id!!,null)
                    .show(supportFragmentManager,"order_view")
            }

        })
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

//    Test
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


    private fun pieChartAdapter(){
        chart.setUsePercentValues(true)
        chart.getDescription().setEnabled(false)
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.setDragDecelerationFrictionCoef(0.95f)

//        chart.setCenterTextTypeface(tfLight)
//        chart.setCenterText(generateCenterSpannableText())

        chart.setDrawHoleEnabled(true)
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.setHoleRadius(58f)
        chart.setTransparentCircleRadius(61f)

        chart.setDrawCenterText(true)

        chart.setRotationAngle(0f)
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true)
        chart.setHighlightPerTapEnabled(true)

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        // chart.spin(2000, 0, 360);
        val l: Legend = chart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
//        chart.setEntryLabelTypeface(tfRegular)
        chart.setEntryLabelTextSize(12f)

        setData(3,520f)
    }

    private fun setData(count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
//                    parties.get(i % parties.length),
                    resources.getDrawable(R.drawable.ic_baseline_close_24)
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
//        data.setValueTypeface(tfLight)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
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

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }
}
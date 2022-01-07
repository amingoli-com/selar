package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.activity.category.CategoryActivity
import amingoli.com.selar.activity.product.ListProductActivity
import amingoli.com.selar.activity.product.ProductActivity
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.KEY_EXTRA_BARCODE
import amingoli.com.selar.helper.Config.REQUEST_INTENT_GET_BARCODE
import amingoli.com.selar.model.Branch
import amingoli.com.selar.model.Product
import amingoli.com.selar.widget.CardBoxMain
import amingoli.com.selar.widget.MessageBox
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.widget_message_box.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title.text = "پیشخان فروشنده"

        messageBox.setValue("هشدار\nاشتراک شما منقضی شده","لطفا هرچه سریعنر نسبت به تمدید سرویس فروشنده اقدام نمایید","تمدید اشتراک",false,
            object : MessageBox.Listener{
            override fun onButtonClicked() {
                messageBox.visibility = View.GONE
            }

            override fun onCloseClicked() {
                messageBox.visibility = View.GONE
            }

        })

        cardBoxProduct.build("محصولات",
            App.getDrawable(this,R.drawable.ic_baseline_extension_24),
            null,
            "${App.database.getAppDao().getAllProductCount()}\nثبت شده",
            "190\nپرفروش",
            "${App.database.getAppDao().getOutOfStockProductCount()}\nتمام شده",
            object : CardBoxMain.Listener{
                override fun onAddClicked() {
                    startActivity(Intent(this@MainActivity,ProductActivity::class.java))
                }

                override fun onActOneClicked() {
                    startActivity(Intent(this@MainActivity,ListProductActivity::class.java))
                }

                override fun onActTwoClicked() {
                }

                override fun onActTreeClicked() {
                }

            })

        cardBoxCategory.build("دسته‌بندی‌ها",
            App.getDrawable(this, R.drawable.ic_baseline_category_24),
            "",
            "${App.database.getAppDao().selectCategory().size}\nهمه","${App.database.getAppDao().selectCategory(1).size}\nفعال",null,
            object : CardBoxMain.Listener{
                override fun onAddClicked() {
                    val i = Intent(this@MainActivity,CategoryActivity::class.java)
                    i.putExtra("add",true)
                    startActivity(i)
                }

                override fun onActOneClicked() {
                    val i = Intent(this@MainActivity,CategoryActivity::class.java)
//                    i.putExtra("status",true)
                    startActivity(i)                }

                override fun onActTwoClicked() {
                    val i = Intent(this@MainActivity,CategoryActivity::class.java)
                    i.putExtra("status",1)
                    startActivity(i)
                }

                override fun onActTreeClicked() {
                }

            })

        val p = Branch()
        p.name = "صفائییه"
        p.type = "شیرینی فروشی"

        App.database.getAppDao().insertBranch(p)

        var i = App.database.getAppDao().selectProduct().size
        Log.e("qqqq", "onCreate: $i" )

        create_chart()
    }

    fun create_chart() {
        val bar_chart = findViewById<BarChart>(R.id.chart_bar)
        val visitor: ArrayList<BarEntry> = ArrayList()

        for (i in 0..10) {
            visitor.add(BarEntry(i.toFloat(),Math.random().toFloat()))
        }
        val barDataSet = BarDataSet(visitor, "")
        barDataSet.setColors(Color.rgb(241, 92, 65))
        barDataSet.valueTextSize = 0f
        val barData = BarData(barDataSet)
        bar_chart.setFitBars(true)
        bar_chart.data = barData
        bar_chart.description.text = ""
        bar_chart.animateY(1000)
    }
}
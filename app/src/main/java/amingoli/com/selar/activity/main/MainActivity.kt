package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
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
            "${App.database.getAppDao().productSize().size}\nثبت شده","190\nپرفروش","32\nتمام شده",
            object : CardBoxMain.Listener{
                override fun onAddClicked() {
                    startActivity(Intent(this@MainActivity,ProductActivity::class.java))
                }

                override fun onActOneClicked() {
                }

                override fun onActTwoClicked() {
                }

                override fun onActTreeClicked() {
                }

            })

        cardBoxCategory.build("دسته‌بندی‌ها",
            App.getDrawable(this, R.drawable.ic_baseline_category_24),
            "",
            "280\nثبت شده","190\nمنتخب",null,
            object : CardBoxMain.Listener{
                override fun onAddClicked() {
//                    startActivity(Intent(this@MainActivity,ProductActivity::class.java))
                }

                override fun onActOneClicked() {
                }

                override fun onActTwoClicked() {
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

    }
}
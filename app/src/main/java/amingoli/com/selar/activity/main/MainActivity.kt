package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.product.ProductActivity
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import amingoli.com.selar.widget.MessageBox
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.widget_message_box.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        messageBox.setValue("هشدار","این یک پیام تست است","متوجه شدم",false,
            object : MessageBox.Listener{
            override fun onButtonClicked() {
                messageBox.visibility = View.GONE
            }

            override fun onCloseClicked() {
                messageBox.visibility = View.GONE
            }

        })

        toolbar.title.text = "پیشخان فروشنده"

        val p = Product()
        p.name = ""

        App.database.getAppDao().insertProduct(p)

        var i = App.database.getAppDao().selectProduct().size
        Log.e("qqqq", "onCreate: $i" )

        startActivity(Intent(this,ProductActivity::class.java))
    }
}
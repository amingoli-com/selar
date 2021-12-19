package amingoli.com.selar.activity.main

import amingoli.com.selar.R
import amingoli.com.selar.activity.product.ProductActivity
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Product
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val p = Product()
        p.name = ""

        App.database.getAppDao().insertProduct(p)

        var i = App.database.getAppDao().selectProduct().size
        Log.e("qqqq", "onCreate: $i" )

        startActivity(Intent(this,ProductActivity::class.java))
    }
}
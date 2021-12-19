package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.include_toolbar.view.*

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        toolbar.title.text = "ثبت محصول جدید"
    }
}
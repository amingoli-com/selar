package amingoli.com.selar.widget

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.include_item_amount.view.*

class ItemAmount (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.include_item_amount, this)
    }

    fun setText(title: String, content: String){
        tv_title.setText(title)
        tv_content.setText(content)
    }

    fun setText(title: String, content: Double){
        tv_title.setText(title)
        tv_content.setText(App.priceFormat(content,true))
    }

    fun setText(title: String){
        tv_title.setText(title)
    }

    fun setText(content: Double){
        tv_content.setText(App.priceFormat(content,true))
    }

    fun setText(title: String, content: Double, color: Int){
        tv_title.setText(title)
        tv_content.setText(App.priceFormat(content,true))

        tv_title.setTextColor(color)
        tv_content.setTextColor(color)
    }

}
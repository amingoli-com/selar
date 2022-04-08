package amingoli.com.selar.widget

import amingoli.com.selar.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.widget_card_box_main.view.*

class BasketAdder (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.widget_basket_adder, this)
    }

    interface Listener{
        fun onAdd()
        fun onRemove()
        fun onText()
    }
}
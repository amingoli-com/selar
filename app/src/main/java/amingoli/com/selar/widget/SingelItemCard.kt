package amingoli.com.selar.widget

import amingoli.com.selar.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.widget_singel_item_card.view.*

class SingelItemCard (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.widget_singel_item_card, this)
    }

    fun build(title:String, drawable:Drawable, content:String?, onListener: Listener){
        this.listener = onListener
        tv_title.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null)
        tv_title.setText(title)
        if (content != null){
            line.visibility = View.VISIBLE
            tv_content.visibility = View.VISIBLE
            tv_content.setText(content)
        }
        view.setOnClickListener { listener?.onItemClicked() }
    }

    interface Listener{
        fun onItemClicked()
    }
}

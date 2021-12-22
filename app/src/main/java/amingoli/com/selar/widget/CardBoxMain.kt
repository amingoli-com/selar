package amingoli.com.selar.widget

import amingoli.com.selar.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.widget_card_box_main.view.*

class CardBoxMain (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.widget_card_box_main, this)
    }

    fun build(title:String, drawable:Drawable, buttonTitle:String, actOneTitle:String,
              actTwoTitle:String, actTreeTitle:String, onListener: Listener){
        this.listener = onListener
        tv_title.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null)
        tv_title.setText(title)
        tv_add.setText(buttonTitle)
        tv_act_one.setText(actOneTitle)
        tv_act_two.setText(actTwoTitle)
        tv_act_tree.setText(actTreeTitle)

        tv_add.setOnClickListener { listener?.onAddClicked() }
        tv_act_one.setOnClickListener { listener?.onActOneClicked() }
        tv_act_two.setOnClickListener { listener?.onActTwoClicked() }
        tv_act_tree.setOnClickListener { listener?.onActTreeClicked() }
    }

    interface Listener{
        fun onAddClicked()
        fun onActOneClicked()
        fun onActTwoClicked()
        fun onActTreeClicked()
    }
}

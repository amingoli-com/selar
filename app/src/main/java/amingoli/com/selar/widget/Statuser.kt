package amingoli.com.selar.widget

import amingoli.com.selar.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.widget_statuser.view.*

class Statuser (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.widget_statuser, this)
    }

    fun onEmpty(){
        box_empty.visibility = View.VISIBLE
    }

}
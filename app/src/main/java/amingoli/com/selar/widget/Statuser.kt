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

    interface Listener{
        fun onTryAgain()
    }

    fun onProgress(){
        progress.visibility = View.VISIBLE
        box_empty.visibility = View.GONE
        box_error.visibility = View.GONE
    }

    fun onEmpty(){
        box_empty.visibility = View.VISIBLE
        box_error.visibility = View.GONE
    }

    fun onError(){
        box_error.visibility = View.VISIBLE
        box_empty.visibility = View.GONE
    }

    fun onError(listener: Listener){
        onError()
        box_error.btn_try.setOnClickListener {
            onProgress()
            listener.onTryAgain()
        }
    }

    fun onFinish(){
        box_empty.visibility = View.GONE
        box_error.visibility = View.GONE
        progress.visibility = View.GONE
    }

}
package amingoli.com.selar.widget

import amingoli.com.selar.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.widget_message_box.view.*

class MessageBox(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.widget_message_box, this)
    }

    fun setValue(_title:String, content:String, buttonText: String, cancelable:Boolean, _listener: Listener?){

        if (!_title.isNullOrEmpty()){
            title.visibility = View.VISIBLE
            title.setText(_title)
        }

        if (!content.isNullOrEmpty()){
            desc.visibility = View.VISIBLE
            desc.setText(content)
        }

        if (!buttonText.isNullOrEmpty()){
            button.visibility = View.VISIBLE
            button.setText(buttonText)
        }

        if (_listener != null){
            this.listener = _listener

            button.setOnClickListener {
                listener?.onButtonClicked()
            }

            if (cancelable){
                ic_close.visibility = View.VISIBLE
                ic_close.setOnClickListener {
                    listener?.onCloseClicked()
                }
            }
        }

    }

    interface Listener{
        fun onButtonClicked()
        fun onCloseClicked()
    }
}
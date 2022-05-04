package amingoli.com.selar.widget

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.OrderDetail
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.widget_basket_adder.view.*

class BasketAdder (context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var listener: Listener? = null
    private var count = 0.0
    private var unit = ""

    init {
        View.inflate(context, R.layout.widget_basket_adder, this)

        ic_add.setOnClickListener {
            calculate("add")
        }
        ic_remove.setOnClickListener {
            calculate("remove")
        }
        ic_remove.setOnLongClickListener {
            calculate("delete")
            return@setOnLongClickListener true
        }
    }

    fun show(orderDetail: OrderDetail, listener: Listener){
        this.listener = listener
        count = orderDetail.stock?:0.0
        unit = orderDetail.increase_name?:""
        updateView()
    }

    private fun calculate(type:String){
        when(type){
            "add"->{
                if (count >=0) count++
            }
            "remove"->{
                if (count >=1) count--
            }
            "delete"->{
                count = 0.0
            }
        }
        listener?.onChangeBasketAdder(count)
        updateView()
    }

    interface Listener{
        fun onChangeBasketAdder(count:Double)
    }

    private fun updateView(){
        setImageRemoveAndDelete()
        setText()
    }

    @SuppressLint("SetTextI18n")
    private fun setText(){
        tv.text = "${App.priceFormat(count)} ${unit}"
    }

    private fun setImageRemoveAndDelete(){
        if (count <=1) ic_remove.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_delete_24))
        else ic_remove.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_remove_circle_24))
    }
}
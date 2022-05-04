package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_edit_price.*


class EditPriceDialog(context: Context,val type:String, val hint:String, val price: Double,
                      val listener: Listener) : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true
        initOnClick()
        box_edit_price.hint = hint
        edt_price.addTextChangedListener(PriceTextWatcher(edt_price){})
        edt_price.setText(price.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener {
        fun onEditPrice(dialog: EditPriceDialog, price:Double, type: String)
    }

    private fun initOnClick(){
        submit.btn.setOnClickListener {
            listener.onEditPrice(this, App.convertToDouble(edt_price),type)
        }
    }

}
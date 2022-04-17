package amingoli.com.selar.dialog

import amingoli.com.selar.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class BusinessMenuDialog : DialogFragment {

    private var activity: AppCompatActivity? = null
    private var listener: Listener? = null

    constructor(
        activity: AppCompatActivity,
        listener: Listener
    ) {
        this.activity = activity
        this.listener = listener
    }

    constructor(
        activity: AppCompatActivity
    ) {
        this.activity = activity
    }

    constructor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_menu_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bind
        this.isCancelable = true

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.TOP or Gravity.START)
    }

    interface Listener{
        fun onAddOrder(dialog: BusinessMenuDialog)
        fun onRequestCar(dialog: BusinessMenuDialog)
    }
}
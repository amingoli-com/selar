package ir.trano.keeper.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ir.trano.keeper.R
import kotlinx.android.synthetic.main.dialog_menu_floor_store.*

class DialogMenuFloorStore : DialogFragment {

    private var activity: AppCompatActivity? = null
    private var listener: Listener? = null

    constructor(
        activity: AppCompatActivity,
        listener: Listener
    ) {
        this.activity = activity
        this.listener = listener
    }

    constructor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_menu_floor_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bind
        this.isCancelable = true

        tv_add_order.setOnClickListener {
            listener?.onAddOrder(this)
        }

        tv_request_car.setOnClickListener {
            listener?.onRequestCar(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.TOP or Gravity.START)
    }

    interface Listener{
        fun onAddOrder(dialog: DialogMenuFloorStore)
        fun onRequestCar(dialog: DialogMenuFloorStore)
    }
}
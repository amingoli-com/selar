package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Customers
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_insert_customer.*
import java.util.*


class InsertCustomerDialog(val _context: Context, val _customer: Customers?, val _position: Int,
                           val listener: Listener) : DialogFragment() {

    private var _ID = -1
    private var _POS = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_insert_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true
        initOnClick()
        if (_customer != null) setValue(_customer, _position)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener {
        fun insert(dialog: InsertCustomerDialog, customer: Customers, position: Int)
    }

    private fun initOnClick(){
        submit.btn.setOnClickListener {
            if (formIsValid()) listener.insert(this, getValue(), _POS)
        }
    }

    private fun formIsValid() :Boolean{
        if(App.getString(edt_name).isNullOrEmpty()){
            edt_name.setError(_context.resources.getString(R.string.not_valid))
            return false
        }
        return true
    }

    private fun setValue(category: Customers, position: Int){
        _POS = position
        if (category.id != null) _ID = category.id!!
        if (!category.name.isNullOrEmpty()) edt_name.setText(category.name)
        if (!category.phone.isNullOrEmpty()) edt_tel.setText(category.phone)
        checkbox.isChecked = category.status != null && category.status == 1
    }

    private fun getValue(): Customers{
        val category = Customers()
        if (_ID != -1) category.id = _ID
        category.name = App.getString(edt_name)
        category.phone = App.getString(edt_tel)
        category.branch = Session.getInstance().branch
        category.status = if(checkbox.isChecked) 1 else 0
        if (category.created_at == null) category.created_at = Date()
        category.updated_at = Date()
        return category
    }
}
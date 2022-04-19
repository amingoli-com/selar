package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Customers
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_insert_customer.*


class InsertCustomerDialog(context: Context, val _customer: Customers?, val _position: Int,
                           val listener: Listener) : AlertDialog(context) {

    private var _ID = -1
    private var _POS = -1


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_insert_customer)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        initOnClick()
        if (_customer != null) setValue(_customer, _position)
    }


    interface Listener {
        fun insert(dialog: AlertDialog, customer: Customers, position: Int)
    }

    private fun initOnClick(){
        submit.btn.setOnClickListener {
            if (formIsValid()) listener.insert(this, getValue(), _POS)
        }
    }

    private fun formIsValid() :Boolean{
        if(App.getString(edt_name).isNullOrEmpty()){
            edt_name.setError(context.resources.getString(R.string.not_valid))
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
        return category
    }
}
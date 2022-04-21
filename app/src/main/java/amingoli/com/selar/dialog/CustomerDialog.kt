package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CustomerListAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Customers
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.edt_name
import kotlinx.android.synthetic.main.dialog_customer.*
import kotlinx.android.synthetic.main.dialog_insert_customer.*
import kotlinx.android.synthetic.main.item_menu_dialog_add_customer.view.*

class CustomerDialog(val _context: Context, val listener: Listener?) : DialogFragment() {

    private var business = App.database.getAppDao().selectBusiness(Session.getInstance().businessID)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        initOnClick()
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.TOP or Gravity.CENTER)
    }

    interface Listener{
        fun onCustomerClicked(dialog: CustomerDialog, customers: Customers?)
    }

    private fun initOnClick(){
        tv_add.setOnClickListener {
            recyclerView.visibility = View.GONE
            box_add.visibility = View.VISIBLE
        }

        box_add.submit.btn.setOnClickListener {
            if (formIsValid()){
                val c = getValue(App.database.getAppDao().insertCustomer(getValue(null)))
                listener?.onCustomerClicked(this, c)
                dismiss()
            }
        }
    }

    private fun initRecyclerView(){
        val arrayList = ArrayList<Customers>(App.database.getAppDao().selectCustomerActive())
        recyclerView.adapter = CustomerListAdapter(_context, arrayList,object : CustomerListAdapter.Listener{
            override fun onItemClicked(position: Int, item: Customers, action: String?) {
                listener?.onCustomerClicked(this@CustomerDialog,item)
                dismiss()
            }

        })
    }

    private fun formIsValid(): Boolean{
        if (App.getString(box_add.edt_name).isNullOrEmpty()){
            box_add.edt_name.setError(_context.getString(R.string.not_valid))
            return false
        }
        return true
    }

    private fun getValue(id : Long?): Customers{
        val category = Customers()
        if (id != null) category.id = id.toInt()
        category.name = App.getString(box_add.edt_name)
        category.phone = App.getString(box_add.edt_tel)
        category.branch = Session.getInstance().branch
        category.status = 1
        return category
    }
}
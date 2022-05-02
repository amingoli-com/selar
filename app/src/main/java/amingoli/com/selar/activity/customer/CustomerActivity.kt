package amingoli.com.selar.activity.customer

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CustomerListAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.dialog.CustomerViewDialog
import amingoli.com.selar.dialog.InsertCustomerDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.item_toolbar.view.*
import java.lang.Exception

class CustomerActivity : AppCompatActivity(), InsertCustomerDialog.Listener, CustomerViewDialog.Listener {

    private var adapter : CustomerListAdapter? =null
    private var adapterTag : TagInfoAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        initToolbar(getString(R.string.customers))
        initAdapterTagList()
        initAdapterCustomer()
    }

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            InsertCustomerDialog(this, null,-1,this)
                .show(supportFragmentManager,"customer")
        }


        toolbar.ic_search.visibility = View.VISIBLE
        toolbar.ic_search.setOnClickListener {
            toolbar.ic_back.visibility = View.GONE
            toolbar.title.visibility = View.GONE
            toolbar.ic_add.visibility = View.GONE
            toolbar.edt_search.visibility = View.VISIBLE
            toolbar.ic_close.visibility = View.VISIBLE
            toolbar.edt_search.setSelection(0)
        }
        toolbar.ic_close.setOnClickListener {
            toolbar.edt_search.text.clear()
            toolbar.ic_back.visibility = View.VISIBLE
            toolbar.title.visibility = View.VISIBLE
            toolbar.ic_add.visibility = View.VISIBLE
            toolbar.edt_search.visibility = View.GONE
            toolbar.ic_close.visibility = View.GONE
            App.closeKeyboard(this)
        }

        toolbar.edt_search.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_GO,
                EditorInfo.IME_ACTION_SEARCH->{
                    adapter?.updateList(App.database.getAppDao().searchCustomer(App.branch(), App.getString(toolbar.edt_search)))
                    adapterTag?.removeSelection()
                    App.closeKeyboard(this)
                }
            }
            return@setOnEditorActionListener false
        }

    }

    private fun initAdapterTagList(){
        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("همه", R.drawable.ic_account_circle_black_24dp,"all"))
        array_tag.add(TagList("فعال", R.drawable.ic_baseline_emoji_emotions_24,"active"))
        array_tag.add(TagList("بیشترین سفارش", R.drawable.ic_baseline_shopping_cart_24,"most_order"))
        array_tag.add(TagList("بدهکاران", R.drawable.ic_local_sela_black_24dp,"debtor"))
        array_tag.add(TagList("غیرفعال", R.drawable.ic_baseline_help_24,"inactive"))
        array_tag.add(TagList("کمترین سفارش", R.drawable.ic_baseline_calendar_month_24,"least_order"))

        adapterTag = TagInfoAdapter(this,
            array_tag,
            object : TagInfoAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
                    adapter?.updateList(selectCustomer(item.tag!!))
                }
            })

        recyclerView_tag.adapter = adapterTag
    }

    private fun initAdapterCustomer(){
        adapter = CustomerListAdapter(this,
            ArrayList(selectCustomer("all")),
            true,
            object : CustomerListAdapter.Listener {
                override fun onItemClicked(position: Int, item: Customers, action: String?) {
                    when(action){
                        "tel", "sms" -> {
                            try {
                                val i = Intent(ACTION_VIEW)
                                i.setData(Uri.parse("${action}:${item.phone}"))
                                startActivity(i)
                            }catch (e:Exception) {
                                App.toast(getString(R.string.your_device_hasnt_this_feathure))
                            }
                        }
                        else -> {
                            CustomerViewDialog(this@CustomerActivity,item.id!!,position,this@CustomerActivity)
                                .show(supportFragmentManager, "customer")
                        }
                    }
                }
            })
        recyclerView.adapter = adapter
    }

    private fun selectCustomer(query: String) : List<Customers>{
        return when(query){
            "active" -> App.database.getAppDao().selectCustomerByStatus(App.branch(),1)
            "inactive" -> App.database.getAppDao().selectCustomerByStatus(App.branch(),0)
            "most_order" -> App.database.getAppDao().selectCustomer(App.branch())
            "least_order" -> App.database.getAppDao().selectCustomer(App.branch())
            "debtor" -> App.database.getAppDao().selectCustomer(App.branch())
            else -> App.database.getAppDao().selectCustomer(App.branch())
        }
    }

    /**
     * Listener
     * */

    override fun insert(dialog: InsertCustomerDialog, customer: Customers, position: Int) {
        customer.id = App.database.getAppDao().insertCustomer(customer).toInt()
        adapter?.addItem(customer, position)
        dialog.dismiss()
    }

    override fun onEditCustomer(dialog: CustomerViewDialog, customer: Customers?, position: Int?) {
        InsertCustomerDialog(this@CustomerActivity, customer,position!!, this@CustomerActivity)
            .show(supportFragmentManager,"customer")
        dialog.dismiss()
    }

}
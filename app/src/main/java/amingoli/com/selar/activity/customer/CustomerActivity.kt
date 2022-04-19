package amingoli.com.selar.activity.customer

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CustomerListAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.dialog.InsertCustomerDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class CustomerActivity : AppCompatActivity(), InsertCustomerDialog.Listener {

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
            InsertCustomerDialog(this,
                null,-1,this).show()
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
                    adapter?.updateList(App.database.getAppDao().searchCustomer(App.getString(toolbar.edt_search)))
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
            object : CustomerListAdapter.Listener {
                override fun onItemClicked(position: Int, item: Customers, action: String?) {
                    when(action){
                        "tel", "sms" -> {
                            val i = Intent(ACTION_DIAL)
                            i.setData(Uri.parse("${action}:${item.phone}"))
                            startActivity(i)
                        }
                        else -> {
                            InsertCustomerDialog(this@CustomerActivity, item,position,this@CustomerActivity).show()
                        }
                    }
                }
            })
        recyclerView.adapter = adapter
    }

    private fun selectCustomer(query: String) : List<Customers>{
        return when(query){
            "active" -> App.database.getAppDao().selectCustomerByStatus(1)
            "inactive" -> App.database.getAppDao().selectCustomerByStatus(0)
            "most_order" -> App.database.getAppDao().selectCustomer()
            "least_order" -> App.database.getAppDao().selectCustomer()
            "debtor" -> App.database.getAppDao().selectCustomer()
            else -> App.database.getAppDao().selectCustomer()
        }
    }

    /**
     * Listener
     * */

    override fun insert(dialog: AlertDialog, customer: Customers, position: Int) {
        customer.id = App.database.getAppDao().insertCustomer(customer).toInt()
        adapter?.addItem(customer, position)
        dialog.dismiss()
    }

}
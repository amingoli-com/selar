package amingoli.com.selar.activity.order

import amingoli.com.selar.R
import amingoli.com.selar.activity.add_order.AddOrderActivity
import amingoli.com.selar.activity.product.ProductActivity
import amingoli.com.selar.adapter.CustomerListAdapter
import amingoli.com.selar.adapter.OrdersListAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.dialog.InsertCustomerDialog
import amingoli.com.selar.dialog.OrderViewDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class OrderActivity : AppCompatActivity(), OrderViewDialog.Listener {

    private var adapter : OrdersListAdapter? =null
    private var adapterTag : TagInfoAdapter? =null
    private val branch = App.branch()

    private val resultAdd =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data!= null){
                    if (result.data!!.extras!= null){
                        val order_id:Int = result.data!!.getIntExtra("order_id",-1)
                        adapter?.add(App.database.getAppDao().selectOrdersById(order_id))
                    }
                }
            }
        }

    private val resultUpdate =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data!= null){
                    if (result.data!!.extras!= null){
                        val order_id:Int = result.data!!.getIntExtra("order_id",-1)
                        val order_position:Int = result.data!!.getIntExtra("order_position",-1)
                        Log.e("qqqq", "id: ${order_id} - pos: ${order_position}")
                        adapter?.add(App.database.getAppDao().selectOrdersById(order_id),order_position)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        initToolbar(getString(R.string.orders))
        initAdapterTagList()
        initAdapterOrders()
    }

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            resultAdd.launch(Intent(this, AddOrderActivity::class.java))
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
                    adapter?.updateList(App.database.getAppDao().searchOrders(branch, App.getString(toolbar.edt_search)))
                    adapterTag?.removeSelection()
                    App.closeKeyboard(this)
                }
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initAdapterTagList(){
        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("آخرین‌سفارشات", R.drawable.ic_baseline_storefront_24,"all"))
        array_tag.add(TagList("در انتظار", R.drawable.ic_baseline_browse_gallery_24,"waiting"))
        array_tag.add(TagList("بیشترین سود", R.drawable.ic_baseline_add_circle_24,"most_gain"))
        array_tag.add(TagList("کمترین سود", R.drawable.ic_baseline_remove_circle_24,"least_gain"))
        array_tag.add(TagList("تسویه‌شده", R.drawable.ic_baseline_monetization_on_24,"paid"))
        array_tag.add(TagList("تسویه‌نشده", R.drawable.ic_baseline_error_24,"unpaid"))
        array_tag.add(TagList("نقدی", R.drawable.ic_local_sela_black_24dp,"money"))
        array_tag.add(TagList("کارتخوان", R.drawable.ic_baseline_credit_card_24,"card"))
        array_tag.add(TagList("ترکیبی", R.drawable.ic_unit_symbols_black_24dp,"multi_pay"))
        array_tag.add(TagList("بیشترین اقلام", R.drawable.ic_baseline_emoji_emotions_24,"most_count"))
        array_tag.add(TagList("کمترین اقلام", R.drawable.ic_baseline_shopping_cart_24,"least_count"))

        adapterTag = TagInfoAdapter(this,
            array_tag,
            object : TagInfoAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
                    adapter?.updateList(selectOrder(item.tag!!))
                }
            })

        recyclerView_tag.adapter = adapterTag
    }

    private fun initAdapterOrders(){
        adapter = OrdersListAdapter(this,
            ArrayList(selectOrder("all")),
            object : OrdersListAdapter.Listener {
                override fun onItemClicked(position: Int, item: Orders) {
                    OrderViewDialog(this@OrderActivity,item.id!!,position,
                        this@OrderActivity).show(supportFragmentManager,"order_view")
                }
            })
        recyclerView.adapter = adapter
    }

    private fun selectOrder(query: String) : List<Orders>{
        return when(query){
            "all"->         App.database.getAppDao().selectOrders(branch)
            "waiting"->     App.database.getAppDao().selectOrders(branch, ORDER_STATUS_WAITING)
            "most_gain"->   App.database.getAppDao().selectOrdersByMostProfit(branch)
            "least_gain"->  App.database.getAppDao().selectOrdersByLeastProfit(branch)
            "paid"->        App.database.getAppDao().selectOrdersByPied(branch)
            "unpaid"->      App.database.getAppDao().selectOrdersByUnPied(branch)
            "money"->       App.database.getAppDao().selectOrdersByMoney(branch)
            "card"->        App.database.getAppDao().selectOrdersByCard(branch)
            "multi_pay"->   App.database.getAppDao().selectOrdersMultiPay(branch)
            "most_count"->  App.database.getAppDao().selectOrdersMostCount(branch)
            "least_count"-> App.database.getAppDao().selectOrdersLeastCount(branch)
            else ->         App.database.getAppDao().selectOrders(branch)
        }
    }

    override fun onEditOrder(dialog: OrderViewDialog, order: Orders?, position: Int) {
        val i = Intent(this, AddOrderActivity::class.java)
        i.putExtra("order_id", order?.id)
        i.putExtra("order_position", position)
        resultUpdate.launch(i)
        dialog.dismiss()
    }

    /**
     * Listener
     * */

}
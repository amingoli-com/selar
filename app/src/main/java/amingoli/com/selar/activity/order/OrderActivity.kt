package amingoli.com.selar.activity.order

import amingoli.com.selar.R
import amingoli.com.selar.activity.add_order.AddOrderActivity
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
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class OrderActivity : AppCompatActivity() {

    private var adapter : OrdersListAdapter? =null
    private var adapterTag : TagInfoAdapter? =null

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
            startActivity(Intent(this, AddOrderActivity::class.java))
            finish()
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
                    adapter?.updateList(App.database.getAppDao().searchOrders(App.getString(toolbar.edt_search)))
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
                    OrderViewDialog(this@OrderActivity,item.id!!,null).show(supportFragmentManager,"order_view")
                }
            })
        recyclerView.adapter = adapter
    }

    private fun selectOrder(query: String) : List<Orders>{
        return when(query){
            "all"->         App.database.getAppDao().selectOrders()
            "waiting"->     App.database.getAppDao().selectOrders(ORDER_STATUS_WAITING)
            "most_gain"->   App.database.getAppDao().selectOrdersByMostProfit()
            "least_gain"->  App.database.getAppDao().selectOrdersByLeastProfit()
            "paid"->        App.database.getAppDao().selectOrdersByPied()
            "unpaid"->      App.database.getAppDao().selectOrdersByUnPied()
            "money"->       App.database.getAppDao().selectOrdersByMoney()
            "card"->        App.database.getAppDao().selectOrdersByCard()
            "multi_pay"->   App.database.getAppDao().selectOrdersMultiPay()
            "most_count"->  App.database.getAppDao().selectOrdersMostCount()
            "least_count"-> App.database.getAppDao().selectOrdersLeastCount()
            else ->         App.database.getAppDao().selectOrders()
        }
    }

    /**
     * Listener
     * */

}
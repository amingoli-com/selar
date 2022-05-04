package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.AddOrderAdapter
import amingoli.com.selar.dialog.CustomerDialog
import amingoli.com.selar.dialog.EditPriceDialog
import amingoli.com.selar.dialog.OrderDetailViewDialog
import amingoli.com.selar.dialog.SetPaymentDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.*
import amingoli.com.selar.widget.select_product.SelectProduct
import amingoli.com.selar.widget.text_watcher.EditTextWatcher
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_add_order.*
import kotlinx.android.synthetic.main.activity_add_order_camera.edt
import kotlinx.android.synthetic.main.activity_add_order_camera.recyclerView
import java.util.*
import kotlin.collections.ArrayList

class AddOrderActivity : AppCompatActivity(), SetPaymentDialog.Listener, SelectProduct.Listener,
    EditPriceDialog.Listener, OrderDetailViewDialog.Listener {

    private var ORDER_CODE = System.currentTimeMillis().toString()
    private var EDIT = false
    private var _POSITION: Int? = null
    private var TAX_ALL = 0

    private var this_order = Orders()
    private var order_detail = ArrayList<OrderDetail>()
    private var adapter: AddOrderAdapter? = null
    private var codeScanner: CodeScanner? = null
    private var sound_scaner: MediaPlayer? = null

    private var total_price_profit = 0.0
    private var totla_count_orders = 0.0
    private var totla_price_orders_sale = 0.0
    private var totla_tax = 0.0
    private var total_shipping = 0.0
    private var totla_discount = 0.0
    private var totla_all = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        TAX_ALL = Session.getInstance().taxPercent
        total_shipping = Session.getInstance().shippingPrice
        initExtrasDataIntent()
        initOtherView()
        initOnClick()
        initOrderList()
        initCameraScanner()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun initExtrasDataIntent(){
        if (intent?.extras?.getInt("order_id",-1) != null){
            EDIT = true
            val order_id = intent?.extras?.getInt("order_id",-1)
            this_order = App.database.getAppDao().selectOrdersById(order_id!!.toInt())
            ORDER_CODE = this_order.order_code!!
            order_detail = ArrayList(App.database.getAppDao().selectOrdersDetailByOrderCode(App.branch(), ORDER_CODE))
            initCustomerView()
        }

        this_order = if (intent != null && intent?.extras != null){
            val extra = intent!!.extras!!.getInt("order_id", -1)
            App.database.getAppDao().selectOrdersById(extra)
        }else Orders()

        _POSITION = if (intent != null && intent?.extras != null){
            intent!!.extras!!.getInt("order_position", -1)
        }else null
    }

    @SuppressLint("StringFormatInvalid")
    private fun calculator(list:ArrayList<OrderDetail>){
        totla_count_orders = 0.0
        totla_price_orders_sale = 0.0
        total_price_profit = 0.0
        totla_tax = 0.0
        totla_discount = 0.0
        totla_all = 0.0

        if (!list.isNullOrEmpty()){
            for (i in 0 until list.size){
                totla_price_orders_sale += list[i].price_sale!! * list[i].stock!!
                totla_discount += list[i].price_discount!! * list[i].stock!!
                total_price_profit += list[i].price_profit!! * list[i].stock!!
                totla_tax += ((list[i].price_sale!! * list[i].stock!!) / 100) * calculateTax(list[i].tax_percent)
                totla_count_orders += list[i].stock!!
            }
            totla_all = totla_price_orders_sale + totla_tax + total_shipping
        }
        setTextBoxFactor()
        showViewFactor()
    }

    private fun calculateTax(product_tax : Int?): Int {
        return product_tax?:TAX_ALL
    }

    private fun setTextBoxFactor(){
        view_orders.setText(resources.getString(R.string.all_order_one),totla_price_orders_sale)
        view_tax.setText(resources.getString(R.string.tax),totla_tax)
        view_shipping.setText(resources.getString(R.string.shipping_price),total_shipping)
        view_discount.setText(resources.getString(R.string.price_discount),totla_discount,ContextCompat.getColor(this,R.color.green))
        view_total_price.setText(resources.getString(R.string.amount_pay),totla_all)

        tv_payment_product_size.setText(resources.getString(R.string.stock_count, totla_count_orders.toInt().toString()))
        tv_payment_amount.setText(App.priceFormat(totla_all,true))
    }

    private fun showViewFactor(){
        if (totla_count_orders > 0){
            box_price.visibility = View.VISIBLE
            box_pay.visibility = View.VISIBLE
            box_button.visibility = View.VISIBLE
        }else{
            box_price.visibility = View.GONE
            box_pay.visibility = View.GONE
            box_button.visibility = View.GONE
        }
    }

    /**
    * initial view
    * */

    private fun initOtherView(){
        sound_scaner = MediaPlayer.create(this,R.raw.scan)
        selectProduct.setListener(this)
        edt.addTextChangedListener(EditTextWatcher { text -> resultScan(text.toString(), "scanner") })
    }

    private fun initOrderList(){
        adapter = AddOrderAdapter(this,ORDER_CODE, order_detail,object : AddOrderAdapter.Listener{
            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {
                OrderDetailViewDialog(this@AddOrderActivity,orderDetail,position,
                    this@AddOrderActivity).show(supportFragmentManager,"order_detail")
            }
            override fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>) {
                calculator(listOrderDetail)
            }

            override fun onReadyOrderDetailForDatabase(newListOrderDetail: ArrayList<OrderDetail>) {
                insertOrder(newListOrderDetail)
            }

        })
        recyclerView.adapter = adapter
        if (!order_detail.isNullOrEmpty()) calculator(order_detail)
    }

    private fun initCameraScanner(){
        val scannerView = findViewById<CodeScannerView>(R.id.camera_scanner)
        codeScanner = CodeScanner(this, scannerView)
        // Parameters (default values)
        codeScanner?.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner?.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner?.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner?.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner?.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner?.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner?.decodeCallback = DecodeCallback {
            runOnUiThread {
                resultScan(it.text,"camera")
            }
        }
        codeScanner?.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                App.toast("Camera initialization error: ${it.message}")
            }
        }
        scannerView.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun initOnClick(){
        ic_qr_code.setOnClickListener {
            if (box_camera.visibility == View.GONE){
                codeScanner?.startPreview()
                box_camera.visibility = View.VISIBLE
                ic_qr_code.imageTintList = ContextCompat.getColorStateList(this,R.color.red)
            }else{
                codeScanner?.releaseResources()
                codeScanner?.stopPreview()
                ic_qr_code.imageTintList = ContextCompat.getColorStateList(this,R.color.blue)
                box_camera.visibility = View.GONE
            }
        }
        ic_select_product.setOnClickListener {
            if (selectProduct.visibility == View.GONE){
                selectProduct.visibility = View.VISIBLE
                ic_select_product.imageTintList = ContextCompat.getColorStateList(this,R.color.red)
            }else{
                ic_select_product.imageTintList = ContextCompat.getColorStateList(this,R.color.blue)
                selectProduct.visibility = View.GONE
            }
        }
        tv_customer.setOnClickListener {
            CustomerDialog(this, object : CustomerDialog.Listener{
                @SuppressLint("SetTextI18n")
                override fun onCustomerClicked(dialog: CustomerDialog, customers: Customers?) {
                    this_order.customer = customers?.id
                    this_order.customer_name = customers?.name
                    this_order.customer_phone = customers?.phone
                    initCustomerView()
                }
            }).show(supportFragmentManager,"customer")
        }
        view_shipping.setOnClickListener {
            EditPriceDialog(this,"total_shipping",
                resources.getString(R.string.shipping_price),total_shipping,this)
                .show(supportFragmentManager,"edit_price")
        }
        submit_order.setOnClickListener {
            SetPaymentDialog(this,getOrder(),this).show()
        }
        submit_order_waiting.setOnClickListener {
            this_order.status = ORDER_STATUS_WAITING
            this_order.customer_debtor = totla_all
            this_order.pay_cash = 0.0
            this_order.pay_card = 0.0
            this_order.pay_card_info = null
            getOrder()
            adapter?.populateOrderDetailToDatabase(this_order.customer)
        }
        exit.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCustomerView(){
        tv_customer.text = if (this_order.customer != null && this_order.customer_name != null){
            "${this_order.customer_name} ${if (this_order.customer_phone.isNullOrEmpty()) "" else this_order.customer_phone}"
        }else getString(R.string.choose_customer)
    }

    /**
     * get
     * */
    private fun getOrder(): Orders{
        this_order.branch = App.branch()
        this_order.order_code = ORDER_CODE
        this_order.orders_count = totla_count_orders
        this_order.total_price_order = totla_price_orders_sale
        this_order.total_price_profit = total_price_profit
        this_order.total_tax = totla_tax
        this_order.totla_shipping = total_shipping
        this_order.amount_discount = totla_discount
        this_order.totla_all = totla_all
        this_order.create_at = if (this_order.create_at == null) Date() else this_order.create_at
        this_order.update_at = Date()
        return this_order
    }

    /**
     * Listener
     * */
    private fun resultScan(barcode:String, device:String){
        sound_scaner?.start()
        val p = App.database.getAppDao().selectProductByQR(App.branch(), barcode)

        if ( p != null){
            adapter?.addItem(p)
        }
        Handler().postDelayed({
            edt.requestFocus()
            edt.text.clear()
            if (device.equals("camera")) codeScanner?.startPreview()
        },400)
    }

    override fun onCategory(category: Category) {
    }

    override fun onProduct(product: Product) {
        sound_scaner?.start()
        adapter?.addItem(product)
    }

    override fun onPayedByMoneyCash(dialog: SetPaymentDialog, orders: Orders) {
        this_order = orders
        dialog.dismiss()
        readyToAddOrder()
    }

    override fun onPayedByCard(dialog: SetPaymentDialog, orders: Orders) {
        this_order = orders
        dialog.dismiss()
        readyToAddOrder()
    }

    override fun onPayedByMultiCash(dialog: SetPaymentDialog, orders: Orders) {
        this_order = orders
        dialog.dismiss()
        readyToAddOrder()
    }

    override fun onPayedByDebit(dialog: SetPaymentDialog, orders: Orders) {
        this_order = orders
        dialog.dismiss()
        readyToAddOrder()
    }

    private fun readyToAddOrder(){
        Log.e("qqqq",
            "customer id: ${this_order.customer} - ${this_order.customer_name} \n" +
                "cahs money: ${this_order.pay_cash} \n" +
                "card: ${this_order.pay_card} \n" +
                "card info: ${this_order.pay_card_info} \n" +
                "debit customer: ${this_order.customer_debtor} \n" )
        adapter?.populateOrderDetailToDatabase(this_order.customer)
    }

    /**
     * Insert Database
     * */
    private fun insertOrder(new_order_detail: ArrayList<OrderDetail>){
        if (EDIT) App.database.getAppDao().deleteOrdersDetailByOrderCode(App.branch(), ORDER_CODE)
        App.database.getAppDao().insertOrderDetail(new_order_detail)
        val order_id = App.database.getAppDao().insertOrder(this_order).toInt()
        submitted(order_id)
    }

    private fun submitted(idOrder: Int){
        val i = Intent()
        i.putExtra("order_id",idOrder)
        if (_POSITION != null) i.putExtra("order_position",_POSITION)
        setResult(RESULT_OK, i)
        finish()
    }

    override fun onEditPrice(dialog: EditPriceDialog, price: Double, type: String) {
        when(type){
            "total_shipping"-> total_shipping = price
        }
        if (!order_detail.isNullOrEmpty()) calculator(order_detail)
        dialog.dismiss()
    }

    override fun onEditOrderDetail(dialog: OrderDetailViewDialog, position: Int, orderDetail: OrderDetail) {
        adapter?.addItem(position,orderDetail)
    }

    override fun onRemoveOrderDetail(dialog: OrderDetailViewDialog, position: Int, orderDetail: OrderDetail) {
        adapter?.removeItem(position)
    }
}
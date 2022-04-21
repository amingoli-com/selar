package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.AddOrderAdapter
import amingoli.com.selar.dialog.SetPaymentDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import amingoli.com.selar.widget.select_product.SelectProduct
import amingoli.com.selar.widget.text_watcher.EditTextWatcher
import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
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

class AddOrderActivity : AppCompatActivity(), SetPaymentDialog.Listener, SelectProduct.Listener {

    private var this_order = Orders()
    private var adapter: AddOrderAdapter? = null
    private var codeScanner: CodeScanner? = null
    private var sound_scaner: MediaPlayer? = null

    private var total_price_profit = 0.0
    private var totla_count_orders = 0.0
    private var totla_price_orders_sale = 0.0
    private var totla_tax = 0.0
    private var totla_shipping = 0.0
    private var totla_discount = 0.0
    private var totla_all = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        initOtherView()
        initOnClick()
        initOrderList()
        initCameraScanner()

    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    @SuppressLint("StringFormatInvalid")
    private fun calculator(list:ArrayList<OrderDetail>){
        totla_count_orders = 0.0
        totla_price_orders_sale = 0.0
        total_price_profit = 0.0
        totla_tax = 0.0
        totla_shipping = Session.getInstance().shippingPrice
        totla_discount = 0.0
        totla_all = 0.0

        if (!list.isNullOrEmpty()){
            for (i in 0 until list.size){
                totla_price_orders_sale += list[i].price_sale!! * list[i].stock!!
                totla_discount += list[i].price_discount!! * list[i].stock!!
                total_price_profit += list[i].price_profit!! * list[i].stock!!
                if (list[i].tax_percent != null) totla_tax += (list[i].price_sale!! * list[i].stock!!) / 100 * list[i].tax_percent!!
                totla_count_orders += list[i].stock!!
            }
            totla_all = totla_price_orders_sale + totla_tax + totla_shipping
        }
        setTextBoxFactor()
        showViewFactor()
    }

    private fun setTextBoxFactor(){
        view_orders.setText(resources.getString(R.string.all_order_one),totla_price_orders_sale)
        view_tax.setText(resources.getString(R.string.tax),totla_tax)
        view_shipping.setText(resources.getString(R.string.shipping_price),totla_shipping)
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
        adapter = AddOrderAdapter(this, ArrayList(),object : AddOrderAdapter.Listener{
            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {
            }
            override fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>) {
                calculator(listOrderDetail)
            }

        })
        recyclerView.adapter = adapter
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
        submit_order.setOnClickListener {
            SetPaymentDialog(this,getOrder(),this).show()
        }
    }

    /**
     * get
     * */

    private fun getOrder(): Orders{
        this_order.orders_count = totla_count_orders
        this_order.total_price_order = totla_price_orders_sale
        this_order.total_price_profit = total_price_profit
        this_order.total_tax = totla_tax
        this_order.totla_shipping = totla_shipping
        this_order.amount_discount = totla_discount
        this_order.totla_all = totla_all

        return this_order
    }

    /**
     * Listener
     * */

    private fun resultScan(barcode:String, device:String){
        sound_scaner?.start()
        val p = App.database.getAppDao().selectProductByQR(barcode)

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

    override fun onPayedByMoneyCash(orders: Orders) {
        this_order = orders
    }

    override fun onPayedByCard(orders: Orders) {
        TODO("Not yet implemented")
    }

    override fun onPayedByMultiCash(orders: Orders) {
        TODO("Not yet implemented")
    }

    override fun onPayedByDebit(orders: Orders) {
        TODO("Not yet implemented")
    }
}
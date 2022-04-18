package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.AddOrderAdapter
import amingoli.com.selar.adapter.AddOrderCameraAdapter
import amingoli.com.selar.dialog.SetPaymentDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.OrderDetail
import amingoli.com.selar.model.Product
import amingoli.com.selar.widget.select_product.SelectProduct
import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_add_order.*
import kotlinx.android.synthetic.main.activity_add_order_camera.*
import kotlinx.android.synthetic.main.activity_add_order_camera.edt
import kotlinx.android.synthetic.main.activity_add_order_camera.recyclerView

class AddOrderActivity : AppCompatActivity(), SelectProduct.Listener {

    private var adapter: AddOrderAdapter? = null
    private var codeScanner: CodeScanner? = null
    private var TYPE_SCAN = Config.SCAN_BARCODE_SINGLE
    private var sound_scaner: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        if (intent?.extras != null){
            TYPE_SCAN = intent?.extras?.getInt(
                Config.KEY_EXTRA_TYPE_SCAN,
                Config.SCAN_BARCODE_SINGLE
            )!!
        }

        initListOrder()
        initScaner()

        selectProduct.setListener(this)

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
            SetPaymentDialog(this).show()
        }




        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    resultScan(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        sound_scaner = MediaPlayer.create(this,R.raw.scan)
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun initListOrder(){
        adapter = AddOrderAdapter(this, ArrayList(),object : AddOrderAdapter.Listener{
            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {
                App.toast(position.toString())
            }

            override fun onChangeListener(position: Int, listOrderDetail: ArrayList<OrderDetail>) {
                initAmountBox(listOrderDetail)
            }

        })
        recyclerView.adapter = adapter

    }

    private fun initScaner(){
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
//                resultScan(it.text)
                edt.setText(it.text)
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

    @SuppressLint("StringFormatInvalid")
    private fun initAmountBox(list:ArrayList<OrderDetail>){
        var all_order = 0.0
        var tax_price = 0.0
        var shipping_price = 15000.0
        var free = 0.0
        var all_price = 0.0
        var all_stock = 0.0

        if (list.isNullOrEmpty()){
            box_price.visibility = View.GONE
            view_orders.setText(resources.getString(R.string.all_order_one))
        }else{
            box_price.visibility = View.VISIBLE
            for (i in 0 until list.size){
                all_order += list[i].price_sale!! * list[i].stock!!
                free += list[i].price_discount!! * list[i].stock!!
                all_stock += list[i].stock!!
            }
            tax_price = (all_order/100) * 9
            all_price = all_order + tax_price + shipping_price
            view_orders.setText(resources.getString(R.string.all_order_one,list.size))
        }
        view_orders.setText(all_order)
        view_tax.setText(resources.getString(R.string.tax),tax_price)
        view_shipping.setText(resources.getString(R.string.shipping_price),shipping_price)
        view_discount.setText(resources.getString(R.string.price_discount),free,ContextCompat.getColor(this,R.color.green))
        view_total_price.setText(resources.getString(R.string.total_amount),all_price)
        initPaymentText(all_stock.toInt(),all_price)
    }

    private fun initPaymentText(size:Int,price_all:Double){
        if (size > 0){
            box_button.visibility = View.VISIBLE
            box_pay.visibility = View.VISIBLE
            tv_payment_product_size.setText(resources.getString(R.string.stock_count, size.toString()))
            tv_payment_amount.setText(App.priceFormat(price_all,true))
        }else{
            box_button.visibility = View.GONE
            box_pay.visibility = View.GONE
        }
    }

    private fun resultScan(barcode:String){
        sound_scaner?.start()
        val p = App.database.getAppDao().selectProductByQR(barcode)

        if ( p != null){
            adapter?.addItem(p)
        }
        Handler().postDelayed({
            edt.requestFocus()
            edt.text.clear()
//            codeScanner?.startPreview()
        },500)
    }

    private fun resultScan(product: Product?){
        sound_scaner?.start()

        if ( product != null){
            adapter?.addItem(product)
        }
        Handler().postDelayed({
            edt.requestFocus()
            edt.text.clear()
//            codeScanner?.startPreview()
        },500)
    }


    /**
     * Listener
     * */

    override fun onCategory(category: Category) {
    }

    override fun onProduct(product: Product) {
        resultScan(product)
    }
}
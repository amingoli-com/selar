package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.AddOrderAdapter
import amingoli.com.selar.adapter.AddOrderCameraAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.model.OrderDetail
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

class AddOrderActivity : AppCompatActivity() {

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

    private fun initAmountBox(list:ArrayList<OrderDetail>){
        var all_price = 0.0
        var all_order = 0.0
        var tax_price = 0.0
        var shipping_price = 15000.0
        var free = 0.0

        if (list.isNullOrEmpty()){
            box_price.visibility = View.GONE
            tv_all_order.setText(resources.getString(R.string.all_order_one))
        }else{
            box_price.visibility = View.VISIBLE
            for (i in 0 until list.size){
                all_order += list[i].price_sale!! * list[i].stock!!
                free += list[i].price_discount!!
            }
            tax_price = (all_order/100) * 9
            all_price = all_order + tax_price + shipping_price
            tv_all_order.setText(resources.getString(R.string.all_order,list.size))
        }
        tv_all_order_amount.setText(App.priceFormat(all_order))
        tv_tax.setText(App.priceFormat(tax_price))
        tv_sipping.setText(App.priceFormat(shipping_price))
        tv_discount.setText(App.priceFormat(free))
        tv_all_price.setText(App.priceFormat(all_price))
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
}
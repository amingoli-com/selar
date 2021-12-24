package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.adapter.SpinnerAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.App.Companion.context
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Branch
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.Spinner
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import java.io.File

class ProductActivity : AppCompatActivity()  {

    private var _DISCOUNT = 0.0
    private var _IMAGE_DEFULT_PATH = ""

    private val resultGetBarcodeCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            edt_barcode.setText(result?.data?.extras?.getString(Config.KEY_EXTRA_BARCODE))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initToolbar()
        initActionOnClick()
        initTextWatcherPrice()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                Glide.with(this).load(resultUri).into(image)
                _IMAGE_DEFULT_PATH = App.saveFile(App.getByte(resultUri))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun initToolbar(){
        toolbar.title.text = "ثبت محصول جدید"
    }

    private fun initActionOnClick() {
        btn_scanBarCodeByCamera.setOnClickListener {
            resultGetBarcodeCamera.launch(Intent(this, BarcodeScannerActivity::class.java))
        }

        image.setOnClickListener {
            if (checkPermission()){
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
            }
        }

        submit.btn.setOnClickListener {
            if (formIsValid()){
                submit.showLoader()
                App.database.getAppDao().insertProduct(insertValue())
                Handler().postDelayed({finish()},1000)
            }
        }
    }

    private fun initTextWatcherPrice(){
        edt_price_buy.addTextChangedListener(PriceTextWatcher(edt_price_buy) {initDiscount()})
        edt_price_sela_on_product.addTextChangedListener(PriceTextWatcher(edt_price_sela_on_product) {initDiscount()})
        edt_price_sela.addTextChangedListener(PriceTextWatcher(edt_price_sela) {initDiscount()})
    }

    @SuppressLint("SetTextI18n")
    private fun initDiscount(){
        val _buy = App.convertToDouble(edt_price_buy)
        val _sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        val _sale = App.convertToDouble(edt_price_sela)
        val _discount_price = _sale_on_product - _sale
        val _profit = _sale - _buy

        if (_discount_price > 0 && _sale > 0){
            if (tv_discount.visibility != View.VISIBLE) tv_discount.visibility = View.VISIBLE
            tv_discount.setText("این کالا شامل " + App.priceFormat(_discount_price,true) + " تخفیف است")
            _DISCOUNT = _discount_price
        }else{
            if (tv_discount.visibility != View.GONE) tv_discount.visibility = View.GONE
        }

        when {
            _profit > 0 && _buy > 0 && _sale > 0 -> {
                if (tv_profit.visibility != View.VISIBLE) tv_profit.visibility = View.VISIBLE
                tv_profit.setText("سود شما از این کالا " + App.priceFormat(_profit, true))
                tv_profit.setTextColor(resources.getColor(R.color.complementary))
            }
            _profit < 0 && _buy > 0 && _sale > 0 -> {
                if (tv_profit.visibility != View.VISIBLE) tv_profit.visibility = View.VISIBLE
                tv_profit.setText("ضرر شما از این کالا " + App.priceFormat(_profit, true))
                tv_profit.setTextColor(resources.getColor(R.color.red))
            }
            else -> {
                if (tv_profit.visibility != View.GONE) tv_profit.visibility = View.GONE
            }
        }
    }

    /**
     * Act
     * */

    private fun calculateDiscount(){

    }

    private fun formIsValid() : Boolean{
        val product = insertValue()

        if (product.name.isNullOrEmpty()){

        }

        if (product.image_defult.isNullOrEmpty()){

        }

        if (product.increase.isNullOrEmpty()){

        }

        if (product.branch == null){

        }

        if (product.status == null){

        }

        if (product.stock == null){

        }

        if (product.tax_percent == null){

        }

        if (product.user == null){

        }



        return true
    }

    private fun insertValue(): Product{
        val product = Product()

//        product.id = ""
        product.qrcode = App.getString(edt_barcode)
        product.name = App.getString(edt_name)
        product.image_defult = _IMAGE_DEFULT_PATH
//        product.image_code = ""
        product.descrption = App.getString(edt_desc)
        product.branch = Session.getInstance().branch
        product.status = 1
        product.stock = App.convertToDouble(edt_stock)
        product.price_buy = App.convertToDouble(edt_price_buy)
        product.price_sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        product.price_sale = App.convertToDouble(edt_price_sela)
        product.price_discount = _DISCOUNT
        product.min_selection = 1.0
        product.max_selection = 5.0
        product.increase = App.getString(atc_unit)
        product.tax_percent = 0
        product.user = Session.getInstance().user

//        product.created_at = ""
//        product.update_at = ""

        return product
    }

    /**
     * Permission Alert For get Permissions Camera
     * */
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)
    //    Permos
    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            val permissionsNotGranted = ArrayList<String>()
            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission)
                }
            }
            if (permissionsNotGranted.size > 0) {
                var shouldShowAlert = false
                for (permission in permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                }

                val arr = arrayOfNulls<String>(permissionsNotGranted.size)
                val permissions = permissionsNotGranted.toArray(arr)
                if (shouldShowAlert) {
                    showPermissionAlert(permissions)
                } else {
                    requestPermissions(permissions)
                }
                return false
            }
        }
        return true
    }

    private fun showPermissionAlert(permissions: Array<String?>) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.permission_required)
        alertBuilder.setMessage(R.string.permission_message)
        alertBuilder.setPositiveButton(R.string.positive_button) { _, _ -> requestPermissions(permissions) }
        alertBuilder.setNegativeButton(R.string.negative_button) { _, _ -> finish() }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun requestPermissions(permissions: Array<String?>) {
        ActivityCompat.requestPermissions(this, permissions, Config.REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Config.REQUEST_CODE -> {
                for (result in grantResults) {
                    Log.e("qqqRequestPelt", "" + result)
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Log.e("qqqRequestPelt", "PERMISSION_DENIED " + result)

                        // Not all permissions granted. Show some message and return.
                        return
                    }else{
                        Log.e("qqqRequestPelt", "PERMISSION " + result)
                    }
                }
                Log.e("qqqRequestPelt", "checkPermission.............. ")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
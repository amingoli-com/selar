package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Product
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_product.*

import kotlinx.android.synthetic.main.include_toolbar.view.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class ProductActivity : AppCompatActivity()  {

    private var _PRODUCT_OBJECT : Product? = null
    private var _ID_PRODUCT : Int? = null
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
        initObject()
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

    private fun initObject(){
        _PRODUCT_OBJECT = if (intent != null && intent?.extras != null){
            val extra = intent!!.extras!!.getString("product")
            Gson().fromJson(extra,Product::class.java)
        }else Product()
    }

    private fun initToolbar(){
        toolbar.title.text = "ثبت محصول جدید"
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
    }

    private fun initActionOnClick() {
        btn_scanBarCodeByCamera.setOnClickListener {
            resultGetBarcodeCamera.launch(Intent(this, BarcodeScannerActivity::class.java))
        }

        image.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
        }

        submit.btn.setOnClickListener {
            if (formIsValid()){
                submit.showLoader()
                App.database.getAppDao().insertProduct(getValue())
                Handler().postDelayed({finish()},500)
            }
        }
    }

    private fun initTextWatcherPrice(){
        edt_price_buy.addTextChangedListener(PriceTextWatcher(edt_price_buy) {initDiscount()})
        edt_price_sela_on_product?.addTextChangedListener(PriceTextWatcher(edt_price_sela_on_product) {initDiscount()})
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
        getValue()
        var value_is_true = "true"

        if (_PRODUCT_OBJECT?.name.isNullOrEmpty()){
            edt_name.setError(resources.getString(R.string.not_valid))
            value_is_true = "false"
        }

        if (_PRODUCT_OBJECT?.image_defult.isNullOrEmpty()){
            image.requestFocus()
            App.toast(resources.getString(R.string.image_not_valid))
            value_is_true = "false"
        }

        if (_PRODUCT_OBJECT?.increase.isNullOrEmpty()){
            atc_unit.setError(resources.getString(R.string.not_valid))
            value_is_true = "false"
        }

        if (_PRODUCT_OBJECT?.branch == null){
        }

        if (_PRODUCT_OBJECT?.status == null){
        }

        if (_PRODUCT_OBJECT?.stock == null){
            edt_stock.setError(resources.getString(R.string.not_valid))
            value_is_true = "false"
        }

        if (_PRODUCT_OBJECT?.tax_percent == null){
        }

        if (_PRODUCT_OBJECT?.user == null){
        }

        if (_PRODUCT_OBJECT?.price_buy!! <= 0 && _PRODUCT_OBJECT?.price_sale!! <= 0){
            edt_price_buy.setError(resources.getString(R.string.not_valid))
            value_is_true = "false"
        }else if (_PRODUCT_OBJECT?.price_buy!! > 0 && _PRODUCT_OBJECT?.price_sale!! <= 0){
            _PRODUCT_OBJECT?.price_sale = _PRODUCT_OBJECT?.price_buy
            edt_price_sela.setText(_PRODUCT_OBJECT?.price_sale.toString())
        }else if (_PRODUCT_OBJECT?.price_buy!! <= 0 && _PRODUCT_OBJECT?.price_sale!! > 0){
            _PRODUCT_OBJECT?.price_buy = _PRODUCT_OBJECT?.price_sale
            edt_price_buy.setText(_PRODUCT_OBJECT?.price_buy.toString())
        }
        return value_is_true == "true"
    }

    private fun getValue(): Product{

        _PRODUCT_OBJECT?.id = _ID_PRODUCT
        _PRODUCT_OBJECT?.image_defult = _IMAGE_DEFULT_PATH
        _PRODUCT_OBJECT?.price_discount = _DISCOUNT
        _PRODUCT_OBJECT?.branch = Session.getInstance().branch
        _PRODUCT_OBJECT?.user = Session.getInstance().user
        _PRODUCT_OBJECT?.qrcode = App.getString(edt_barcode)
        _PRODUCT_OBJECT?.name = App.getString(edt_name)
        _PRODUCT_OBJECT?.descrption = App.getString(edt_desc)
        _PRODUCT_OBJECT?.increase = App.getString(atc_unit)
        _PRODUCT_OBJECT?.stock = App.convertToDouble(edt_stock)
        _PRODUCT_OBJECT?.price_buy = App.convertToDouble(edt_price_buy)
        _PRODUCT_OBJECT?.price_sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        _PRODUCT_OBJECT?.price_sale = App.convertToDouble(edt_price_sela)
        _PRODUCT_OBJECT?.max_selection = App.convertToDouble(edt_max_selection)
        _PRODUCT_OBJECT?.min_selection = 1.0
        _PRODUCT_OBJECT?.tax_percent = 0
        _PRODUCT_OBJECT?.status = 1

//        _PRODUCT_OBJECT?.image_code = ""
//        _PRODUCT_OBJECT?.created_at = ""
//        _PRODUCT_OBJECT?.update_at = ""

        return _PRODUCT_OBJECT!!
    }
}
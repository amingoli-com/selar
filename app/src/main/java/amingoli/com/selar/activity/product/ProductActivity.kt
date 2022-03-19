package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.adapter.AutoCompleteAdapter
import amingoli.com.selar.adapter.TagAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.Spinner
import amingoli.com.selar.model.TagList
import amingoli.com.selar.model.UnitModel
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.item_toolbar.view.*
import java.util.*


class ProductActivity : AppCompatActivity()  {

    private var _PRODUCT_OBJECT : Product? = null
    private var _ID_PRODUCT : Int? = null
    private var _DISCOUNT = 0.0
    private var _IMAGE_DEFULT_PATH = ""
    private var _DATE_EXPIRED: Date? = null

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
        initDateExpire()
        initAutoCompleteUnitsList()
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

        tv_add_date_expire.setOnClickListener {
            tv_add_date_expire.visibility = View.GONE
            box_date_expire.visibility = View.VISIBLE
        }

        submit.btn.setOnClickListener {
            if (formIsValid()){
                submit.showLoader()
                insertUnitList()
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

    private fun initDateExpire(){
        btn_open_calender.setOnClickListener {
            val picker = PersianDatePickerDialog(this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(Typeface.createFromAsset(assets, "fonts/iran_sans_mobile.ttf"))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(object : PersianPickerListener {
                    override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                        edt_date.setText(App.getFormattedDate(persianPickerDate.timestamp))
                    }
                    override fun onDismissed() {}
                })
            picker.show()
        }


        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("بدون انقضا","0"))
        array_tag.add(TagList("۷ روز دیگر","7"))
        array_tag.add(TagList("۱۵ روز دیگر","15"))
        array_tag.add(TagList("یکماه دیگر","30"))
        array_tag.add(TagList("سه ماه دیگر","90"))
        array_tag.add(TagList("شش ماه دیگر","180"))
        array_tag.add(TagList("یکسال دیگر","365"))
        array_tag.add(TagList("دوسال دیگر","730"))

        val adapterTagList = TagAdapter(this,
            array_tag,
            object : TagAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
                    if (item.tag.equals("0")){
                        edt_date.text.clear()
                        tv_add_date_expire.visibility = View.VISIBLE
                        box_date_expire.visibility = View.GONE
                    }else{
                        val calendar = Calendar.getInstance()
                        calendar.time = Date()
                        calendar.add(Calendar.DAY_OF_YEAR, +item.tag!!.toInt())
                        edt_date.setText(App.getFormattedDate(calendar.timeInMillis))
                    }
                }
            })

        recyclerView_date.adapter = adapterTagList
    }

    private fun initAutoCompleteUnitsList(){
        val spinner = ArrayList<Spinner>()
        val list_unit = App.database.getAppDao().selectUnit()
        for (i in list_unit.indices) { spinner.add(Spinner(list_unit[i].id!!, list_unit[i].title )) }
        val adapter = AutoCompleteAdapter(this, spinner, false)
        atc_unit.setAdapter(adapter)
        atc_unit.setOnItemClickListener { parent, view, position, id ->
            val r: Spinner = parent.getItemAtPosition(position) as Spinner
            App.toast(r.name)
        }
        atc_unit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) atc_unit.showDropDown()
        }
        atc_unit.setOnClickListener { atc_unit.showDropDown() }
    }


    /**
     * Act
     * */

    private fun insertUnitList(){
        if (App.database.getAppDao().selectUnit(App.getString(atc_unit)) == null){
            App.database.getAppDao().insertUnit(UnitModel(App.getString(atc_unit)))
        }
    }

    private fun formIsValid() : Boolean{
        getValue()
        var value_is_true = "true"

        if (_PRODUCT_OBJECT?.name.isNullOrEmpty()){
            edt_name.setError(resources.getString(R.string.not_valid))
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
        _PRODUCT_OBJECT?.date_expired = _DATE_EXPIRED
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
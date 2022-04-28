package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.adapter.AutoCompleteAdapter
import amingoli.com.selar.adapter.TagAdapter
import amingoli.com.selar.dialog.SelectCategoryDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.*
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.edt_name
import kotlinx.android.synthetic.main.activity_product.ic_delete
import kotlinx.android.synthetic.main.activity_product.image
import kotlinx.android.synthetic.main.activity_product.submit
import kotlinx.android.synthetic.main.dialog_insert_category.*
import kotlinx.android.synthetic.main.item_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList


class ProductActivity : AppCompatActivity(), SelectCategoryDialog.Listener  {

    private var _PRODUCT_OBJECT : Product? = null
//    private var _ID_PRODUCT : Int? = null
    private var _IMAGE_DEFULT_PATH = ""
    private var _DATE_EXPIRED: Date? = null
    private var _CATEGORY: ArrayList<Category> = ArrayList()

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
        setValue()
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
                ic_delete.visibility = View.VISIBLE
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun initObject(){
        _PRODUCT_OBJECT = if (intent != null && intent?.extras != null){
            val extra = intent!!.extras!!.getInt("id_product", -1)
            App.database.getAppDao().selectProduct(extra)
        }else Product()
    }

    private fun initToolbar(){
        toolbar.title.text = if (_PRODUCT_OBJECT?.id != null) "ویرایش ${_PRODUCT_OBJECT?.name}" else "ثبت محصول جدید"
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

        ic_delete.setOnClickListener {
            _IMAGE_DEFULT_PATH = ""
            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_add_photo_alternate_black_24dp))
            ic_delete.visibility = View.GONE
        }

        checkbox_tax.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (isChecked) {
                    edt_tax_percent.isEnabled = true
                    edt_tax_percent.setText(Session.getInstance().taxPercent.toString())
                } else {
                    edt_tax_percent.isEnabled = false
                    edt_tax_percent.setText("0")
                }
            }
        }

        tv_add_category.setOnClickListener {
            SelectCategoryDialog(this, 0, _CATEGORY,this).show()
        }

        tv_add_date_expire.setOnClickListener {
            tv_add_date_expire.visibility = View.GONE
            box_date_expire.visibility = View.VISIBLE
        }

        submit.btn.setOnClickListener {
            if (formIsValid()){
                submit.showLoader()
                val idProduct = App.database.getAppDao().insertProduct(getValue()).toInt()
                insertUnitList()
                insertCategoryProductList(idProduct)
                Handler().postDelayed({finish()},500)
            }
        }
    }

    private fun initTextWatcherPrice(){
        edt_price_buy.addTextChangedListener(PriceTextWatcher(edt_price_buy) {initTextProfitAndDiscount()})
        edt_price_sela_on_product?.addTextChangedListener(PriceTextWatcher(edt_price_sela_on_product) {initTextProfitAndDiscount()})
        edt_price_sela.addTextChangedListener(PriceTextWatcher(edt_price_sela) {initTextProfitAndDiscount()})
    }

    @SuppressLint("SetTextI18n")
    private fun initTextProfitAndDiscount(){
        if (calculateDiscount() > 0){
            if (tv_discount.visibility != View.VISIBLE) tv_discount.visibility = View.VISIBLE
            tv_discount.setText(getString(R.string.this_product_has_free, App.priceFormat(calculateDiscount(),true)))
        }else{
            if (tv_discount.visibility != View.GONE) tv_discount.visibility = View.GONE
        }

        when {
            calculateProfit() > 0 -> {
                if (tv_profit.visibility != View.VISIBLE) tv_profit.visibility = View.VISIBLE
                tv_profit.setText(getString(R.string.this_product_has_profit, App.priceFormat(calculateProfit(), true)))
                tv_profit.setTextColor(resources.getColor(R.color.complementary))
            }
            calculateProfit() < 0 -> {
                if (tv_profit.visibility != View.VISIBLE) tv_profit.visibility = View.VISIBLE
                tv_profit.setText(getString(R.string.this_product_hast_free, App.priceFormat(calculateProfit(), true)))
                tv_profit.setTextColor(resources.getColor(R.color.red))
            }
            else -> {
                if (tv_profit.visibility != View.GONE) tv_profit.visibility = View.GONE
            }
        }
    }

    private fun calculateProfit() : Double{
        val _buy = App.convertToDouble(edt_price_buy)
        val _sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        val _sale = App.convertToDouble(edt_price_sela)

        return when {
            _sale > 0.0 -> {
                _sale - _buy
            }
            _sale_on_product > 0.0 -> {
                _sale_on_product - _buy
            }
            else -> 0.0
        }
    }

    private fun calculateDiscount() : Double{
        val _sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        val _sale = App.convertToDouble(edt_price_sela)

        return when {
            _sale_on_product > _sale -> {
                _sale_on_product - _sale
            }
            else -> 0.0
        }
    }

    private fun initCategoryProductList(){
        if (_PRODUCT_OBJECT?.id != null){
            val cat : ArrayList<CategoryProduct> = ArrayList(App.database.getAppDao().selectCategoryProduct(_PRODUCT_OBJECT!!.id!!))
            for (i in 0 until cat.size){
                _CATEGORY.add(App.database.getAppDao().selectCategory(cat[i].id_category!!))
            }
            initCategory()
        }
    }

    private fun initCategory(){
        if (!_CATEGORY.isNullOrEmpty()){
            tv_add_category.setText(resources.getString(R.string.this_product_has_n_category,_CATEGORY.size))
            box_category_list.visibility = View.VISIBLE
            val array_tag = ArrayList<TagList>()
            for (i in 0 until _CATEGORY.size){
                array_tag.add(i,TagList(_CATEGORY[i].name,_CATEGORY[i].id.toString()))
            }
            val adapterCategory = TagAdapter(this@ProductActivity, array_tag,null)
            recyclerView_category.adapter = adapterCategory
        }else {
            tv_add_category.setText(resources.getString(R.string.submit_category))
            box_category_list.visibility = View.GONE
        }
    }

    private fun initDateExpire(){
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
                        tv_add_date_expire.setText(resources.getString(R.string.add_date_expire))
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
                        adapterTagList.updateItemSelected(-1)
                    }
                    override fun onDismissed() {}
                })
            picker.show()
        }
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

    private fun insertCategoryProductList(idProduct: Int){
        App.database.getAppDao().deleteCategoryProduct(idProduct)

        val categoryProductList : ArrayList<CategoryProduct> = ArrayList()
        for (i in 0 until _CATEGORY.size){
            categoryProductList.add(CategoryProduct(idProduct, _CATEGORY[i].id))
        }
        App.database.getAppDao().insertCategoryProduct(categoryProductList)
    }

    private fun formIsValid() : Boolean{
        getValue()
        var value_is_true = "true"

        if (_PRODUCT_OBJECT?.name.isNullOrEmpty()){
            edt_name.setError(resources.getString(R.string.not_valid))
            value_is_true = "false"
        }
        if (_PRODUCT_OBJECT?.tax_percent == null){
            edt_tax_percent.setError(resources.getString(R.string.not_valid))
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

        return value_is_true == "true"
    }

    private fun getValue(): Product{
        _PRODUCT_OBJECT?.id = if (_PRODUCT_OBJECT?.id != null) _PRODUCT_OBJECT?.id!! else null
        _PRODUCT_OBJECT?.image_defult = _IMAGE_DEFULT_PATH
        _PRODUCT_OBJECT?.date_expired = App.getString(edt_date)
        _PRODUCT_OBJECT?.branch = Session.getInstance().branch
        _PRODUCT_OBJECT?.user = Session.getInstance().user
        _PRODUCT_OBJECT?.qrcode = App.getString(edt_barcode)
        _PRODUCT_OBJECT?.name = App.getString(edt_name)
        _PRODUCT_OBJECT?.descrption = null
        _PRODUCT_OBJECT?.increase = App.getString(atc_unit)
        _PRODUCT_OBJECT?.stock = App.convertToDouble(edt_stock)
        _PRODUCT_OBJECT?.price_buy = App.convertToDouble(edt_price_buy)
        _PRODUCT_OBJECT?.price_sale_on_product = App.convertToDouble(edt_price_sela_on_product)
        _PRODUCT_OBJECT?.price_sale =
            if (App.convertToDouble(edt_price_sela) == 0.0) _PRODUCT_OBJECT?.price_sale_on_product!!
            else App.convertToDouble(edt_price_sela)
        _PRODUCT_OBJECT?.price_discount = calculateDiscount()
        _PRODUCT_OBJECT?.price_profit = calculateProfit()
        _PRODUCT_OBJECT?.max_selection = App.convertToDouble(edt_max_selection)
        _PRODUCT_OBJECT?.min_selection = 1.0

        _PRODUCT_OBJECT?.tax_percent =
            if (App.convertToInt(edt_tax_percent) == Session.getInstance().taxPercent && checkbox_tax.isChecked){ null }
            else App.convertToInt(edt_tax_percent)

        _PRODUCT_OBJECT?.status = 1
        if (_PRODUCT_OBJECT?.id != null){
            _PRODUCT_OBJECT?.updated_at = Date()
        } else {
            _PRODUCT_OBJECT?.created_at = Date()
            _PRODUCT_OBJECT?.updated_at = Date()
        }
//        _PRODUCT_OBJECT?.image_code = ""
        return _PRODUCT_OBJECT!!
    }

    private fun setValue(){
        if (_PRODUCT_OBJECT?.id != null){
            if (!_PRODUCT_OBJECT?.image_defult.isNullOrEmpty()) {
                Glide.with(this).load(_PRODUCT_OBJECT!!.image_defult!!).into(image)
                _IMAGE_DEFULT_PATH = _PRODUCT_OBJECT!!.image_defult!!
                ic_delete.visibility = View.VISIBLE
            }

            edt_tax_percent.setText(
                if (_PRODUCT_OBJECT?.tax_percent == null) "${Session.getInstance().taxPercent}"
                else "${_PRODUCT_OBJECT?.tax_percent!!}"
            )

            if (_PRODUCT_OBJECT?.tax_percent == 0){
                checkbox_tax.isChecked = false
                edt_tax_percent.isEnabled = false
            }

            edt_barcode.setText(_PRODUCT_OBJECT?.qrcode)
            atc_unit.setText(_PRODUCT_OBJECT?.increase)
            edt_name.setText(_PRODUCT_OBJECT?.name)
            edt_max_selection.setText(_PRODUCT_OBJECT!!.max_selection!!.toInt().toString())
            initCategoryProductList()
            edt_price_buy.setText(App.priceFormat(_PRODUCT_OBJECT!!.price_buy!!))
            edt_price_sela_on_product.setText(App.priceFormat(_PRODUCT_OBJECT!!.price_sale_on_product!!))
            edt_price_sela.setText(App.priceFormat(_PRODUCT_OBJECT!!.price_sale!!))
            edt_stock.setText(App.stockFormat(_PRODUCT_OBJECT!!.stock!!))
            if (!_PRODUCT_OBJECT?.date_expired.isNullOrEmpty()){
                tv_add_date_expire.setText(resources.getString(R.string.expired_at,_PRODUCT_OBJECT?.date_expired))
                edt_date.setText(_PRODUCT_OBJECT?.date_expired)
            }
            initTextProfitAndDiscount()
        }else{
            edt_tax_percent.setText(Session.getInstance().taxPercent.toString())
        }
    }

    /**
     * Listener
     * */
//    Select Category Dialog
    override fun onSubmit(dialog: SelectCategoryDialog, list: ArrayList<Category>?) {
        initCategory()
        dialog.dismiss()
    }
}
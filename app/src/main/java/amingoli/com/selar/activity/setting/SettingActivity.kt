package amingoli.com.selar.activity.setting

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Config.MONEY_TYPE_DEFAULT
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Setting
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_finance.toolbar
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.submit
import kotlinx.android.synthetic.main.item_toolbar.view.*
import java.lang.Exception

class SettingActivity : AppCompatActivity() {

    val setting = App.database.getAppDao().selectSetting(Session.getInstance().branch)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initToolbar(getString(R.string.setting))
        initData()
        submit.btn.setOnClickListener {
            submit.showLoader()
            setData()
        }
    }

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
    }

    private fun initData(){
        initTextWatcherPrice()

        edt_min_order.setText("${setting.min_order?:0.0}")
        edt_tax.setText("${setting.tax?:0}")
        edt_money_type.setText(setting.currency?:MONEY_TYPE_DEFAULT)
        edt_price_shipping.setText("${setting.shipping_price?:0.0}")
        edt_free_shipping.setText("${setting.shipping_free_on_order?:0.0}")

        checkbox_discount.isChecked = setting.cash_discount?:false
        checkbox_money.isChecked = setting.cash_money?:false
        checkbox_card.isChecked = setting.cash_card?:false
        checkbox_debit.isChecked = setting.cash_debit?:false
    }

    private fun setData(){
        setting.min_order = App.convertToDouble(edt_min_order)
        setting.tax = App.convertToInt(edt_tax)
        setting.currency = App.getString(edt_money_type)
        setting.shipping_price = App.convertToDouble(edt_price_shipping)
        setting.shipping_free_on_order = App.convertToDouble(edt_free_shipping)
        setting.cash_discount = checkbox_discount.isChecked
        setting.cash_money = checkbox_money.isChecked
        setting.cash_card = checkbox_card.isChecked
        setting.cash_debit = checkbox_debit.isChecked

        saveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initTextWatcherPrice(){
        edt_price_shipping?.addTextChangedListener(PriceTextWatcher(edt_price_shipping) {})
    }

    private fun saveData(){
        App.database.getAppDao().insertSetting(setting)

        Session.getInstance().minOrder = setting.min_order?:0.0
        Session.getInstance().taxPercent = setting.tax?:0
        Session.getInstance().moneyType = setting.currency
        Session.getInstance().shippingPrice = setting.shipping_price?:0.0
        Session.getInstance().freeShippingPrice = setting.shipping_free_on_order?:0.0

        Session.getInstance().setCheckBox(setting.cash_money?:false, setting.cash_card?:false,
            setting.cash_debit?:false, setting.cash_discount?:false)

        Handler().postDelayed({finish()},700)
    }
}
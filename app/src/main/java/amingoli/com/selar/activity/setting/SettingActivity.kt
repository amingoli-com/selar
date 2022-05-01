package amingoli.com.selar.activity.setting

import amingoli.com.selar.R
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_finance.*
import kotlinx.android.synthetic.main.activity_finance.toolbar
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.submit
import kotlinx.android.synthetic.main.item_toolbar.view.*
import java.lang.Exception

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initToolbar(getString(R.string.setting))
        initData()
        submit.btn.setOnClickListener {
            submit.showLoader()
            saveData()
        }
    }

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }
    }

    private fun initData(){
        initTextWatcherPrice()

        val tax = Session.getInstance().taxPercent
        val moneyType = Session.getInstance().moneyType
        val shippingPrice = Session.getInstance().shippingPrice

        try {
            edt_tax.setText(tax.toString())
            edt_money_type.setText(moneyType.toString())
            edt_price_shipping.setText(shippingPrice.toString())
        }catch (e : Exception){
            Log.e("eee", "initData in setting activity: ",e )
        }


        checkbox_money.isChecked = Session.getInstance().checkBoxMoney
        checkbox_card.isChecked = Session.getInstance().checkBoxCard
        checkbox_debit.isChecked = Session.getInstance().checkBoxDebit
    }

    @SuppressLint("SetTextI18n")
    private fun initTextWatcherPrice(){
        edt_price_shipping?.addTextChangedListener(PriceTextWatcher(edt_price_shipping) {})
    }

    private fun saveData(){
        Session.getInstance().taxPercent = App.convertToInt(edt_tax)
        Session.getInstance().shippingPrice = App.convertToDouble(edt_price_shipping)
        Session.getInstance().moneyType = App.getString(edt_money_type)

        Session.getInstance().setCheckBox(checkbox_money.isChecked, checkbox_card.isChecked, checkbox_debit.isChecked)

        Handler().postDelayed({finish()},1500)
    }
}
package amingoli.com.selar.activity.first_open

import amingoli.com.selar.R
import amingoli.com.selar.activity.first_open.mvp.FirstOpenModel
import amingoli.com.selar.activity.first_open.mvp.FirstOpenView
import amingoli.com.selar.activity.main.MainActivity
import amingoli.com.selar.adapter.BusinessAdapter
import amingoli.com.selar.dialog.DownloadDataSampleDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
import amingoli.com.selar.model.ResponseBusinessSample
import amingoli.com.selar.model.Setting
import amingoli.com.selar.model.TagList
import amingoli.com.selar.widget.Statuser
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_open.*
import java.util.*
import kotlin.collections.ArrayList

class FirstOpenActivity : AppCompatActivity(), BusinessAdapter.Listener, FirstOpenView {

    private var adapter: BusinessAdapter? = null
    private var presenter: FirstOpenModel? = null
    private var URL_SAMPLE_DATA : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_open)

        presenter = FirstOpenModel(this)
        presenter?.getBusinessSample()

        initShowOnlyFirst()
        initOnClick()
        parent_view.animate().alpha(1f).duration = 500
        initRecyclerView()
        edt_name.requestFocus()
    }

    private fun initOnClick(){
        checkbox.setMovementMethod(LinkMovementMethod.getInstance())

        submit.btn.setOnClickListener {
            submit.showLoader()
            if (formIsValid()){
                val id_business : Long = App.database.getAppDao().insertBusiness(
                    Business(
                        App.getString(edt_name),
                        App.getString(edt_business_name),
                        "",
                        "1",
                        Date(),
                        Date()
                    )
                )
                App.database.getAppDao().insertSetting(Setting(id_business.toInt()))
                Session.getInstance().setBusiness(
                    App.getString(edt_name),
                    App.getString(edt_business_name),
                    id_business.toInt()
                )
                Handler().postDelayed({ getSampleData() },2500)
            }else submit.hideLoader()
        }

        exit.setOnClickListener { onBackPressed() }
    }

    private fun initShowOnlyFirst(){
        box_only_first.visibility = if (Session.getInstance().sessionKey.isNullOrEmpty()) View.VISIBLE else View.GONE
        exit.visibility = if (Session.getInstance().sessionKey.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun getSampleData(){
        if (URL_SAMPLE_DATA!= null){
            DownloadDataSampleDialog(this).show(supportFragmentManager,"ss")
        }else{
            next()
        }
    }

    private fun next(){
        if (Session.getInstance().sessionKey.isNullOrEmpty()){
            Session.getInstance().sessionKey = Date().toString()
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            App.toast(resources.getString(R.string.add_business_successfully))
        }
        finish()
    }

    private fun initRecyclerView(){
        adapter = BusinessAdapter(this, ArrayList(),this)
        recyclerView.adapter = adapter
    }

    private fun formIsValid() : Boolean{
        if (App.getString(edt_name).isNullOrEmpty()){
            edt_name.setError(resources.getString(R.string.not_valid))
            return false
        }
        if (App.getString(edt_business_name).isNullOrEmpty()){
            edt_business_name.setError(resources.getString(R.string.not_valid))
            return false
        }
        if (!checkbox.isChecked){
            checkbox.setError(resources.getString(R.string.not_valid))
            return false
        }
        return true
    }

    /**
     * Listener
    * */
    override fun onItemClicked(position: Int, item: ResponseBusinessSample.item) {
        URL_SAMPLE_DATA = item.data_url
    }

    override fun onResponse(arrayListBusinessSample: ArrayList<ResponseBusinessSample.item>?) {
        if (!arrayListBusinessSample.isNullOrEmpty()){
            adapter?.addItem(arrayListBusinessSample)
        }else{
            adapter?.addItemDefault()
        }
    }

    override fun onError(message: String) {
        adapter?.addItemDefault()
        statuser.onError(object : Statuser.Listener{
            override fun onTryAgain() {
                presenter?.getBusinessSample()
            }
        })
    }

    override fun stopResponse() {
        statuser.onFinish()
    }
}
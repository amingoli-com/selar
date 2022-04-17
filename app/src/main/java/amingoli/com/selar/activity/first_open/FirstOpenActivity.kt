package amingoli.com.selar.activity.first_open

import amingoli.com.selar.R
import amingoli.com.selar.activity.main.MainActivity
import amingoli.com.selar.adapter.BusinessAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.TagList
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_first_open.*

class FirstOpenActivity : AppCompatActivity(), BusinessAdapter.Listener {

    private var adapter: BusinessAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_open)

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
                Session.getInstance().setBusiness(App.getString(edt_name),App.getString(edt_business_name))
                Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },2500)
            }else submit.hideLoader()
        }
    }

    private fun initRecyclerView(){
        val arrayList = ArrayList<TagList>()
        arrayList.add(0,TagList("کسب‌وکار شخصی شما","ایجاد کسب‌وکار جدید با اطلاعات دلخواه شما"))
        arrayList.add(TagList("سوپر مارکت","شامل ۱,۸۰۰ محصول و ۲۹۰ دسته بندی"))
        arrayList.add(TagList("نانوایی","شامل ۱۹ نان با ۸ دسته بندی"))
        arrayList.add(TagList("کافی‌شاپ","شامل ۹۲ سرو با ۱۴ دسته‌بندی"))
        arrayList.add(TagList("مطب پزشک","شامل ۱۸ خدمت با ۴ دسته‌بندی"))
        arrayList.add(TagList("رستوران","شامل ۱۲۰ غذا و محصول با ۸۲ دسته‌بندی"))
        adapter = BusinessAdapter(this,arrayList,this)
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
    override fun onItemClicked(position: Int, item: TagList) {
    }
}
package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.activity.first_open.FirstOpenActivity
import amingoli.com.selar.adapter.BusinessAdapter
import amingoli.com.selar.adapter.BusinessListAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Business
import amingoli.com.selar.model.TagList
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_first_open.*
import kotlinx.android.synthetic.main.dialog_menu_business.*
import kotlinx.android.synthetic.main.dialog_menu_business.recyclerView

class BusinessMenuDialog(val _context: Context, val listener: Listener?) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_menu_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bind
        this.isCancelable = true

        initRecyclerView()

        tv_add_business.setOnClickListener {
            startActivity(Intent(context, FirstOpenActivity::class.java))
            dismiss()
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.TOP or Gravity.START)
    }

    interface Listener{
        fun onBusinessList(dialog: BusinessMenuDialog, business: Business)
    }


    private fun initRecyclerView(){
        val arrayList = ArrayList<Business>(App.database.getAppDao().selectBusinessExcept(Session.getInstance().businessID))
        recyclerView.adapter = BusinessListAdapter(_context,arrayList,object : BusinessListAdapter.Listener{
            override fun onItemClicked(position: Int, item: Business) {
                Session.getInstance().setBusiness(
                    item.owner_name,
                    item.business_name,
                    item.id!!
                )
                Session.getInstance().sessionKey = item.session_key
                listener?.onBusinessList(this@BusinessMenuDialog,item)
                dismiss()
            }
        })
    }
}
package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CustomerListHorizontalAdapter
import amingoli.com.selar.adapter.TagAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Customers
import amingoli.com.selar.model.Orders
import amingoli.com.selar.model.Product
import amingoli.com.selar.model.TagList
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_view_product.*

class ProductViewDialog(val _context: Context, val product_id:Int, val position: Int, val listener: Listener?) : DialogFragment() {

    private var this_product = App.database.getAppDao().selectProduct(App.branch(), product_id)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_view_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        initData()
        initOnClick()
        initAdapterTagList()
        initRecyclerView()
        chart_bar_price.barChartAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.CENTER)
    }

    interface Listener{
        fun onEditProduct(dialog: ProductViewDialog, product: Product?, position: Int)
    }

    private fun initData(){
        tv_id.setText("#${this_product.id}")

        if (!this_product.image_defult.isNullOrEmpty()){
            image.visibility = View.VISIBLE
            Glide.with(_context).load(this_product.image_defult).into(image)
        }

        tv_product_name.setText(this_product.name)

        tv_date.setText("در تاریخ ${App.getFormattedDate(this_product.updated_at)}ویرایش شده \n در تاریخ${App.getFormattedDate(this_product.created_at)} ثبت شده")
    }

    private fun initOnClick(){
        ic_close.setOnClickListener {
            dismiss()
        }

        tv_edit.setOnClickListener {
            listener?.onEditProduct(this,this_product, position)
        }
    }

    private fun initAdapterTagList(){
        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("۲۹۰ عدد موجودی"))
        array_tag.add(TagList("۹۲۰,۰۰۰ تومان سرمایه"))
        array_tag.add(TagList("۲۰۰ تومان سود"))
        array_tag.add(TagList("۵۰۰ تومان ارزانتر"))
        array_tag.add(TagList("۹۲ مرتبه سفارش داده شده"))
        array_tag.add(TagList("۹۸۰ عدد فروش رفته"))
        array_tag.add(TagList("#3881492193493"))
        array_tag.add(TagList("آخرین بار در ۱۴۰۱/۰۲/۰۶ فروخته شده"))

        recyclerView_tag.adapter = TagAdapter(_context, array_tag, null)
    }

    private fun initRecyclerView(){
        val arrayList = ArrayList<TagList>()
        arrayList.add(TagList("امین گلی","۲۸۰ عدد"))
        arrayList.add(TagList("حسین یوسفی","۱۸۲ عدد"))
        arrayList.add(TagList("اصغر حاجیان","۱۱۰ عدد"))
        arrayList.add(TagList("سارا عبدالکریمی","۹۸ عدد"))
        arrayList.add(TagList("حسن حاجی پور","۸۰ عدد"))
        recyclerView_customer.adapter = CustomerListHorizontalAdapter(_context,arrayList,
            object : CustomerListHorizontalAdapter.Listener{
                override fun onItemClicked(position: Int, item: TagList) {
                }

            })
    }
}
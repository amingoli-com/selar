package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.SelecCategoryAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_insert_category.*
import kotlinx.android.synthetic.main.dialog_insert_category.submit
import kotlinx.android.synthetic.main.dialog_select_category.*


class SelectCategoryDialog(val _context: Context, val id_mother_category: Int,
                           val _categoryList: ArrayList<Category>,
                           val _listener: Listener) : AlertDialog(_context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_category)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        val _categoryListDatabase : ArrayList<Category> = ArrayList(App.database.getAppDao().selectUnderCategory(id_mother_category))

        recyclerView.adapter = SelecCategoryAdapter(_context,_categoryListDatabase,_categoryList,
            object : SelecCategoryAdapter.Listener{
            override fun onItemClicked(position: Int, item: Category) {
                _listener.onUnderCategory(this@SelectCategoryDialog, item)
            }
            override fun onItemCheckBox(position: Int, item: Category, isChecked: Boolean) {
                if (isChecked) addItemCategoryListManager(item)
                else removeItemCategoryListManager(item)
            }
        })

        initOnClick()
    }

    interface Listener {
        fun onSubmit(dialog: SelectCategoryDialog, list: ArrayList<Category>?)
        fun onUnderCategory(dialog: SelectCategoryDialog, item: Category)
    }

    private fun initOnClick(){
        submit.btn.setOnClickListener {
            _listener.onSubmit(this, _categoryList)
        }
    }

    private fun addItemCategoryListManager(item: Category){
        for (i in 0 until _categoryList.size){
            if (_categoryList[i].id == item.id) return
        }
        _categoryList.add(item)
    }

    private fun removeItemCategoryListManager(item: Category){
        for (i in 0 until _categoryList.size){
            if (_categoryList[i].id == item.id){
                _categoryList.removeAt(i)
                return
            }
        }
    }
}
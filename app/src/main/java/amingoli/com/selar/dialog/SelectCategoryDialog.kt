package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.SelecCategoryAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Category
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_insert_category.*
import kotlinx.android.synthetic.main.dialog_insert_category.submit
import kotlinx.android.synthetic.main.dialog_select_category.*


class SelectCategoryDialog(val _context: Context, var id_mother_category: Int,
                           val _categoryList: ArrayList<Category>,
                           val _listener: Listener) : AlertDialog(_context), SelecCategoryAdapter.Listener {

    private var adapter : SelecCategoryAdapter? = null
    private var _categoryListDatabase : ArrayList<Category> = ArrayList()

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_category)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        adapter = SelecCategoryAdapter(_context,_categoryListDatabase,_categoryList,this)
        recyclerView.adapter = adapter
        showNewList(id_mother_category)
        initOnClick()
    }

    interface Listener {
        fun onSubmit(dialog: SelectCategoryDialog, list: ArrayList<Category>?)
        fun onUnderCategory(dialog: SelectCategoryDialog, item: Category)
    }

    private fun initOnClick(){
        ic_add_category.setOnClickListener {
            addCategory()
        }

        edt_category_name.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_GO,
                EditorInfo.IME_ACTION_SEND->{
                    addCategory()
                }
            }
            return@setOnEditorActionListener false
        }


        submit.btn.setOnClickListener {
            _listener.onSubmit(this, _categoryList)
        }
    }

    private fun initTitle(title:String?){
        tv_title.setText("$title:")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showNewList(id_category: Int){
        id_mother_category = id_category
        recyclerView.removeAllViews()
        _categoryListDatabase.clear()
        _categoryListDatabase.addAll(ArrayList(App.database.getAppDao().selectUnderCategory(id_category)))
        adapter?.notifyDataSetChanged()
    }

    private fun addCategory(){
        if (!App.getString(edt_category_name).isNullOrEmpty()){
            adapter?.addItem(getCategory(App.database.getAppDao().insertCategory(getCategory(null))))
            edt_category_name.text.clear()
            recyclerView.smoothScrollToPosition(_categoryListDatabase.size)
        }
    }

    private fun getCategory(id:Long?): Category{
        val category = Category()
        if (id != null) category.id = id.toInt()
        category.id_mother = id_mother_category
        category.name = App.getString(edt_category_name)
        category.content = ""
        category.image = ""
        category.branch = Session.getInstance().branch
        category.status = 1
        return category
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

    /**
     * Listener
     * */
    override fun onItemClicked(position: Int, item: Category) {
//        _listener.onUnderCategory(this@SelectCategoryDialog, item)
        initTitle(item.name)
        showNewList(item.id!!)    }

    override fun onItemCheckBox(position: Int, item: Category, isChecked: Boolean) {
        Log.e("qqqamin-1", "category: ${item.name} id: ${item.id} - isChecked: ${isChecked}" )
        if (isChecked) addItemCategoryListManager(item)
        else removeItemCategoryListManager(item)    }
}
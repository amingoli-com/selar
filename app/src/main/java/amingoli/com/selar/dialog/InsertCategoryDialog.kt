package amingoli.com.selar.dialog

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListManagerAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.Category
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_insert_category.*


class InsertCategoryDialog(context: Context,val _category: Category?, val _position: Int,
                           val listener: Listener) : AlertDialog(context) {

    private var _ID = -1
    private var _ID_MOTHER = 0
    private var _IMAGE_PATH: String? = null
    private var _POS = -1


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_insert_category)
        this.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        Log.e("qqq", "InsertCategoryDialog onCreate: id: $_ID - id_mother: $_ID_MOTHER - image: $_IMAGE_PATH - pos: $_POS ")
        initOnClick()
        if (_category != null) setValue(_category, _position)
    }

    fun initImage(resultUri: Uri){
        Glide.with(context).load(resultUri).into(image)
        _IMAGE_PATH = App.saveFile(App.getByte(resultUri))
    }

    interface Listener {
        fun chooseImage(dialog: AlertDialog)
        fun insert(dialog: AlertDialog, category: Category, position: Int)
    }

    private fun initOnClick(){
        submit.btn.setOnClickListener {
            if (formIsValid()) listener.insert(this, getValue(), _POS)
        }
        image.setOnClickListener {
            listener.chooseImage(this)
        }
        ic_delete.setOnClickListener {
            _IMAGE_PATH = null
            image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_add_photo_alternate_black_24dp))
        }
    }

    private fun formIsValid() :Boolean{
        if(App.getString(edt_name).isNullOrEmpty()){
            edt_name.setError(context.resources.getString(R.string.not_valid))
            return false
        }
        return true
    }

    private fun setValue(category: Category, position: Int){
        _POS = position
        if (category.id != null) _ID = category.id!!
        if (category.id_mother != null) _ID_MOTHER = category.id_mother!!
        if (!category.name.isNullOrEmpty()) edt_name.setText(category.name)
        if (!category.content.isNullOrEmpty()) edt_content.setText(category.content)
        checkbox.isChecked = category.status != null && category.status == 1
        if (!category.image.isNullOrEmpty()) {
            Glide.with(context).load(category.image).into(image)
            _IMAGE_PATH = category.image
            ic_delete.visibility = View.VISIBLE
        }
    }

    private fun getValue(): Category{
        val category = Category()
        if (_ID != -1) category.id = _ID
        category.id_mother = _ID_MOTHER
        category.name = App.getString(edt_name)
        category.content = App.getString(edt_content)
        category.image = _IMAGE_PATH
        category.branch = Session.getInstance().branch
        category.status = if(checkbox.isChecked) 1 else 0
        return category
    }
}
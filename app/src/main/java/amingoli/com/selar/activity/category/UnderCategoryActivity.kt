package amingoli.com.selar.activity.category

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListManagerAdapter
import amingoli.com.selar.dialog.InsertCategoryDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class UnderCategoryActivity : AppCompatActivity(), InsertCategoryDialog.Listener {

    private var _ID_MOTHRE = -1
    private var _CATEGORY : Category? = null
    private var array = ArrayList<Category>()
    private var adapter: CategoryListManagerAdapter? = null
    private var dialog_category: InsertCategoryDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_under_category)

        initStart()
        initToolbar("دسته ${_CATEGORY?.name!!}")
        initAdapterCategory()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                dialog_category?.initImage(resultUri)
//                _IMAGE_DEFULT_PATH = App.saveFile(App.getByte(resultUri))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun initStart(){
        if (intent?.extras?.getInt("id" , -1) != null){
            _ID_MOTHRE = intent!!.extras!!.getInt("id")
            _CATEGORY = App.database.getAppDao().selectCategory(App.branch(), _ID_MOTHRE)
        }
        array = ArrayList(App.database.getAppDao().selectUnderCategory(App.branch(),_ID_MOTHRE))
    }

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            dialog_category = InsertCategoryDialog(this,
                null,-1,_ID_MOTHRE,this)
            dialog_category?.show()
        }
    }

    private fun initAdapterCategory(){
        adapter = CategoryListManagerAdapter(this,
            array,
            object : CategoryListManagerAdapter.Listener {
                override fun onItemClicked(position: Int, category: Category) {
                    Log.e("qqq", "onItemClicked status is : ${category.status}" )
                    val i = Intent(this@UnderCategoryActivity, UnderCategoryActivity::class.java)
                    i.putExtra("id", category.id)
                    startActivity(i)
                }

                override fun onLongItemClicked(position: Int, category: Category) {
                    dialog_category = InsertCategoryDialog(applicationContext,
                        category,position,category.id_mother!!,this@UnderCategoryActivity)
                    dialog_category?.show()
                }
            })
        recyclerView.adapter = adapter
    }

    /**
     * Listener
     * */

    override fun chooseImage(dialog: AlertDialog) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
    }

    override fun insert(dialog: AlertDialog, category: Category, position: Int) {
        category.id = App.database.getAppDao().insertCategory(category).toInt()
        adapter?.addItem(category, position)
        dialog.dismiss()
    }
}
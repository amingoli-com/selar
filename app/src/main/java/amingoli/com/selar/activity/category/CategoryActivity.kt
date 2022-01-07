package amingoli.com.selar.activity.category

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListManagerAdapter
import amingoli.com.selar.adapter.ProductListManagerAdapter
import amingoli.com.selar.dialog.InsertCategoryDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_category.toolbar
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.item_toolbar.view.*

class CategoryActivity : AppCompatActivity(), InsertCategoryDialog.Listener {

    private var array = ArrayList<Category>()
    private var adapter: CategoryListManagerAdapter? = null
    private var dialog_category: InsertCategoryDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initStart()
        initToolbar("دسته بندی ها")
        initAdapter()
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

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            dialog_category = InsertCategoryDialog(this@CategoryActivity,
                null,-1,this)
            dialog_category?.show()
        }
    }

    private fun initAdapter(){
        adapter = CategoryListManagerAdapter(this,
            array,
            object : CategoryListManagerAdapter.Listener {
                override fun onItemClicked(position: Int, category: Category) {
                    Log.e("qqq", "onItemClicked status is : ${category.status}" )
                    dialog_category = InsertCategoryDialog(this@CategoryActivity,
                        category,position,this@CategoryActivity)
                    dialog_category?.show()
                }
            })
        recyclerView.adapter = adapter
    }

    private fun initStart(){
        if (intent?.extras?.getBoolean("add") != null){
            Handler().postDelayed({
                dialog_category = InsertCategoryDialog(this@CategoryActivity,
                    null,-1,this@CategoryActivity)
                dialog_category?.show()
            }, 500)
        }

        if (intent?.extras?.getInt("status", -1) != null){
            if (intent!!.extras!!.getInt("status",-1) != -1){
                array = ArrayList(App.database.getAppDao()
                    .selectCategory(intent!!.extras!!.getInt("status")))
                return
            }
        }
        array = ArrayList(App.database.getAppDao().selectCategory())
    }

    private fun insertCategory(category: Category){
        if (category.id != null){
            App.database.getAppDao().UpdateCategory(category)
        }else{
            App.database.getAppDao().insertCategory(category)
        }
    }

    /**
     * Listener
     * */

    override fun chooseImage(dialog: AlertDialog) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this@CategoryActivity)
    }

    override fun insert(dialog: AlertDialog, category: Category, position: Int) {
        adapter?.addItem(category, position)
        insertCategory(category)
        dialog.dismiss()
    }

}
package amingoli.com.selar.activity.category

import amingoli.com.selar.R
import amingoli.com.selar.adapter.CategoryListManagerAdapter
import amingoli.com.selar.adapter.TagInfoAdapter
import amingoli.com.selar.dialog.InsertCategoryDialog
import amingoli.com.selar.helper.App
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.TagList
import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_category.toolbar
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
        initAdapterTagList()
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

    private fun initToolbar(title: String) {
        toolbar.title.text = title
        toolbar.ic_back.visibility = View.VISIBLE
        toolbar.ic_back.setOnClickListener { onBackPressed() }

        toolbar.ic_add.visibility = View.VISIBLE
        toolbar.ic_add.setOnClickListener {
            dialog_category = InsertCategoryDialog(this@CategoryActivity,
                null,-1,0,this)
            dialog_category?.show()
        }
    }

    private fun initAdapterTagList(){

        val array_tag = ArrayList<TagList>()
        array_tag.add(TagList("همه", R.drawable.ic_baseline_category_24,"all"))
        array_tag.add(TagList("فعال", R.drawable.ic_baseline_extension_24,"all"))
        array_tag.add(TagList("منتخب", R.drawable.ic_baseline_category_24,"all"))

        val adapterTagList = TagInfoAdapter(this,
            array_tag,
            object : TagInfoAdapter.Listener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClicked(position: Int, item: TagList) {
                }
            })

        recyclerView_tag.adapter = adapterTagList
    }

    private fun initAdapterCategory(){
        adapter = CategoryListManagerAdapter(this,
            array,
            object : CategoryListManagerAdapter.Listener {
                override fun onItemClicked(position: Int, category: Category) {
                    Log.e("qqq", "onItemClicked status is : ${category.status}" )
                    val i = Intent(this@CategoryActivity, UnderCategoryActivity::class.java)
                    i.putExtra("id", category.id)
                    startActivity(i)
                }

                override fun onLongItemClicked(position: Int, category: Category) {
                    dialog_category = InsertCategoryDialog(this@CategoryActivity,
                        category,position,category.id_mother!!,this@CategoryActivity)
                    dialog_category?.show()
                }
            })
        recyclerView.adapter = adapter
    }

    private fun initStart(){
        if (intent?.extras?.getBoolean("add") != null){
            Handler().postDelayed({
                dialog_category = InsertCategoryDialog(this@CategoryActivity,
                    null,-1,0,this@CategoryActivity)
                dialog_category?.show()
            }, 100)
        }

        if (intent?.extras?.getInt("status", -1) != null){
            if (intent!!.extras!!.getInt("status",-1) != -1){
                array = ArrayList(App.database.getAppDao()
                    .selectCategoryByStatus(intent!!.extras!!.getInt("status")))
                return
            }
        }
        array = ArrayList(App.database.getAppDao().selectUnderCategory(0))
    }

    /**
     * Listener
     * */

    override fun chooseImage(dialog: AlertDialog) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this@CategoryActivity)
    }

    override fun insert(dialog: AlertDialog, category: Category, position: Int) {
        category.id = App.database.getAppDao().insertCategory(category).toInt()
        adapter?.addItem(category, position)
        dialog.dismiss()
    }

}
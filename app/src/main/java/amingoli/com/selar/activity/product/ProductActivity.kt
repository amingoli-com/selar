package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.adapter.SpinnerAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.App.Companion.context
import amingoli.com.selar.helper.Config
import amingoli.com.selar.model.Branch
import amingoli.com.selar.model.Spinner
import amingoli.com.selar.widget.text_watcher.PriceTextWatcher
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import java.io.File

class ProductActivity : AppCompatActivity()  {

    private val resultGetBarcodeCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            edt_barcode.setText(result?.data?.extras?.getString(Config.KEY_EXTRA_BARCODE))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initToolbar()
        initActionOnClick()
        initTextWatcherPrice()
        initSpinnerBranch()
        initSpinnerStatus()


        val file = File("/storage/emulated/0/Sealerimage_1640256121119.jpg")
        if (file.exists()) {
            val myBitmap: Bitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
            image.setImageBitmap(myBitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                Glide.with(this).load(resultUri).into(image)
                App.saveFile(App.getByte(resultUri                                                                     ))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun initToolbar(){
        toolbar.title.text = "ثبت محصول جدید"
    }

    private fun initActionOnClick() {
        btn_scanBarCodeByCamera.setOnClickListener {
            resultGetBarcodeCamera.launch(Intent(this, BarcodeScannerActivity::class.java))
        }

        image.setOnClickListener {
            if (checkPermission()){
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)
            }
        }
    }

    private fun initTextWatcherPrice(){
        edt_price_buy.addTextChangedListener(PriceTextWatcher(edt_price_buy) {})

        edt_price_sela_on_product.addTextChangedListener(PriceTextWatcher(edt_price_sela_on_product) {})

        edt_price_sela.addTextChangedListener(PriceTextWatcher(edt_price_sela) {})
    }

    private fun initSpinnerBranch() {
        val branchList = ArrayList<Branch>(App.database.getAppDao().selectBranch())

        val spinner = ArrayList<Spinner>()
        spinner.add(0,Spinner(-1,"شعبه را انتخاب کنید"))
        for (i in 0 until branchList.size) {
            spinner.add(Spinner(branchList[i].id!!,branchList[i].name))
        }

        val adapter = SpinnerAdapter(context,spinner)
        spinner_branch.adapter = adapter

    }

    private fun initSpinnerStatus() {
        val statusList = resources.getStringArray(R.array.status_string)

        val spinner = ArrayList<Spinner>()
        for (i in 0 until statusList.size) {
            spinner.add(Spinner(i,statusList[i]))
        }

        val adapter = SpinnerAdapter(context,spinner)
        spinner_status.adapter = adapter
    }

    /**
     * Permission Alert For get Permissions Camera
     * */
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)
    //    Permos
    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            val permissionsNotGranted = ArrayList<String>()
            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission)
                }
            }
            if (permissionsNotGranted.size > 0) {
                var shouldShowAlert = false
                for (permission in permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                }

                val arr = arrayOfNulls<String>(permissionsNotGranted.size)
                val permissions = permissionsNotGranted.toArray(arr)
                if (shouldShowAlert) {
                    showPermissionAlert(permissions)
                } else {
                    requestPermissions(permissions)
                }
                return false
            }
        }
        return true
    }

    private fun showPermissionAlert(permissions: Array<String?>) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.permission_required)
        alertBuilder.setMessage(R.string.permission_message)
        alertBuilder.setPositiveButton(R.string.positive_button) { _, _ -> requestPermissions(permissions) }
        alertBuilder.setNegativeButton(R.string.negative_button) { _, _ -> finish() }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun requestPermissions(permissions: Array<String?>) {
        ActivityCompat.requestPermissions(this, permissions, Config.REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Config.REQUEST_CODE -> {
                for (result in grantResults) {
                    Log.e("qqqRequestPelt", "" + result)
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Log.e("qqqRequestPelt", "PERMISSION_DENIED " + result)

                        // Not all permissions granted. Show some message and return.
                        return
                    }else{
                        Log.e("qqqRequestPelt", "PERMISSION " + result)
                    }
                }
                Log.e("qqqRequestPelt", "checkPermission.............. ")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
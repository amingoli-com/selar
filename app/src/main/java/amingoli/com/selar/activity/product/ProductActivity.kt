package amingoli.com.selar.activity.product

import amingoli.com.selar.R
import amingoli.com.selar.activity.BarcodeScannerActivity
import amingoli.com.selar.activity.ChooseCameraActivity
import amingoli.com.selar.dialog.PikeImageDialog
import amingoli.com.selar.helper.Config
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import java.io.File

class ProductActivity : AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initToolbar()
        initActionOnClick()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                Glide.with(this).load(resultUri).into(image)
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
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }
    }

    /**
     * Result Activity Value
     * */
    private val resultGetBarcodeCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            edt_barcode.setText(result?.data?.extras?.getString(Config.KEY_EXTRA_BARCODE))
        }
    }
}
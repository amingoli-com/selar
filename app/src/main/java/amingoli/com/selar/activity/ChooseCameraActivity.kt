package amingoli.com.selar.activity

import amingoli.com.selar.R
import amingoli.com.selar.helper.Config
import amingoli.com.selar.helper.Config.KEY_EXTRA_BITMAP
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_choose_camera.*
import kotlinx.android.synthetic.main.activity_product.*
import java.io.File

class ChooseCameraActivity : AppCompatActivity() {

    private val TYPE = "CHOOSE_IMAGE_FROM_CAMERA"
    private lateinit var uri: Uri
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
        if (isSaved){
            submit.visibility = View.VISIBLE
            cropImageView.setImageUriAsync(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_camera)

        if (TYPE == "CHOOSE_IMAGE_FROM_CAMERA"){
            if (checkPermission()) initCamera()
        }

        submit.setOnClickListener {
            if (cropImageView.croppedImage != null){
                val intent = Intent()
                intent.putExtra(KEY_EXTRA_BITMAP,cropImageView.croppedImage)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun initCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        uri = FileProvider.getUriForFile(applicationContext, "${applicationContext.packageName}.provider", photoFile)
        takePicture.launch(uri)
    }

    /**
     * Permission Alert For get Permissions Camera
     * */
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA)
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
                initCamera()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
package amingoli.com.selar.activity.add_order

import amingoli.com.selar.R
import amingoli.com.selar.adapter.AddOrderCameraAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config
import amingoli.com.selar.model.OrderDetail
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_add_order_camera.*
import kotlinx.android.synthetic.main.activity_product.*


class AddOrderCameraActivity : AppCompatActivity() {

    private var adapter: AddOrderCameraAdapter? = null
    private var codeScanner: CodeScanner? = null
    private var TYPE_SCAN = Config.SCAN_BARCODE_SINGLE
    private var sound_scaner: MediaPlayer? = null
    private var cameraIsOn = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_camera)

        if (intent?.extras != null){
            TYPE_SCAN = intent?.extras?.getInt(
                Config.KEY_EXTRA_TYPE_SCAN,
                Config.SCAN_BARCODE_SINGLE
            )!!
        }
        if (checkPermission()) initScaner()

        edt.setInputType(InputType.TYPE_NULL)
        edt.requestFocus()
        camera_power.setOnClickListener {
            if (cameraIsOn){
                cameraIsOn = false
                camera_power.imageTintList = ContextCompat.getColorStateList(this,R.color.red)
                codeScanner?.stopPreview()
                codeScanner?.releaseResources()
            }else{
                cameraIsOn = true
                camera_power.imageTintList = ContextCompat.getColorStateList(this,R.color.green)
                codeScanner?.startPreview()
            }
        }

        edt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    resultScan(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        sound_scaner = MediaPlayer.create(this,R.raw.scan)
        initListOrder()
    }

    override fun onResume() {
        super.onResume()
//        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun initScaner(){
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner?.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner?.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner?.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner?.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner?.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner?.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner?.decodeCallback = DecodeCallback {
            runOnUiThread {
//                resultScan(it.text)
                edt.setText(it.text)
            }
        }
        codeScanner?.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                App.toast("Camera initialization error: ${it.message}")
            }
        }

        scannerView.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun initListOrder(){
        adapter = AddOrderCameraAdapter(this, ArrayList(),object : AddOrderCameraAdapter.Listener{
            override fun onItemClicked(position: Int, orderDetail: OrderDetail) {
                App.toast(position.toString())
            }

        })
        recyclerView.adapter = adapter

    }

    private fun resultScan(barcode:String){
//        when(TYPE_SCAN){
//            Config.SCAN_BARCODE_SINGLE,
//            Config.SCAN_BARCODE_ARRAY ->{
//                sound_scaner?.start()
//                val p = App.database.getAppDao().selectProductByQR(barcode)
//                if ( p != null){
//                    adapter?.addItem(p)
//                }
//            }
//        }
        sound_scaner?.start()
        val p = App.database.getAppDao().selectProductByQR(barcode)
        if ( p != null){
            adapter?.addItem(p)
        }
        Handler().postDelayed({
            edt.requestFocus()
            edt.text.clear()
//            codeScanner?.startPreview()
                              },500)
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
                initScaner()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
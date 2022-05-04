package amingoli.com.selar.activity

import amingoli.com.selar.R
import amingoli.com.selar.adapter.SingalItemAdapter
import amingoli.com.selar.helper.App
import amingoli.com.selar.helper.Config.KEY_EXTRA_BARCODE
import amingoli.com.selar.helper.Config.KEY_EXTRA_TYPE_SCAN
import amingoli.com.selar.helper.Config.REQUEST_CODE
import amingoli.com.selar.helper.Config.SCAN_BARCODE_ARRAY
import amingoli.com.selar.helper.Config.SCAN_BARCODE_SINGLE
import amingoli.com.selar.helper.Session
import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_barcode_scanner.*

class BarcodeScannerActivity : AppCompatActivity() {

    private var adapter: SingalItemAdapter? = null
    private var codeScanner: CodeScanner? = null
    private var TYPE_SCAN = SCAN_BARCODE_SINGLE
    private var sound_scaner: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
        sound_scaner = MediaPlayer.create(this,R.raw.scan)
        if (intent?.extras != null){
            TYPE_SCAN = intent?.extras?.getInt(KEY_EXTRA_TYPE_SCAN, SCAN_BARCODE_SINGLE)!!
        }
        if (checkPermission()) initScaner()
        initListOrder()
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun play_beeb(){
        if (Session.getInstance().checkBoxSoundScanner){
            sound_scaner?.start()
        }
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
                resultScan(it.text)
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
        adapter = SingalItemAdapter(this, ArrayList(),object : SingalItemAdapter.Listener{
            override fun onItemClicked(position: Int, string: String) {

            }

        })
        recyclerView.adapter = adapter

    }

    private fun resultScan(barcode:String){
        play_beeb()
        when(TYPE_SCAN){
            SCAN_BARCODE_SINGLE ->{
                val intent = Intent()
                intent.putExtra(KEY_EXTRA_BARCODE, barcode)
                setResult(RESULT_OK, intent)
                finish()
            }
            SCAN_BARCODE_ARRAY ->{
                adapter?.addItem(barcode)
            }
        }
    }


    /**
     * Permission Alert For get Permissions Camera
     * */
    private val neededPermissions = arrayOf(CAMERA)
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
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
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
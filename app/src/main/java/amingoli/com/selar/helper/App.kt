package amingoli.com.selar.helper

import amingoli.com.selar.database.AppDatabase
import amingoli.com.selar.helper.Config.JPG
import amingoli.com.selar.helper.Config.PATH_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.io.*
import java.text.DecimalFormat
import java.util.*


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        database = AppDatabase.getInstance(this)
        MultiDex.install(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var database: AppDatabase

        fun toast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getFormattedDate(dateTime: Long?): String? {
            return PersianDateFormat("Y/m/d").format(PersianDate(dateTime))
        }

        fun getFormattedDate(date: Date?): String? {
            return PersianDateFormat("Y/m/d").format(PersianDate(date))
        }

        fun priceFormat(double: Double):String{
            val decimalFormat = DecimalFormat("###,###,###")
            return decimalFormat.format(double)
        }

        fun stockFormat(double: Double):String{
            val decimalFormat = DecimalFormat("###.##")
            return decimalFormat.format(double)
        }


        fun priceFormat(double: Double, showMoneyType: Boolean):String{
            return if (showMoneyType) "${priceFormat(double)} ${Session.getInstance().moneyType}"
            else priceFormat(double)
        }

        fun convertToDouble(editText: EditText): Double{
            return if (replaceForPrice(getString(editText)).isNullOrEmpty()) 0.0
            else replaceForPrice(getString(editText)).toDouble()
        }

        fun convertToInt(editText: EditText): Int{
            return if (replaceForPrice(getString(editText)).isNullOrEmpty()) 0
            else replaceForPrice(getString(editText)).toInt()
        }

        fun replaceForPrice(string: String) : String {
            return string
                .replace(" ","")
                .replace(",","")
                .replace("،","")
        }

        fun getString(editText: EditText): String{
            return editText.text.toString().trim()
        }

        fun getDrawable(context: Context, id:Int) : Drawable{
            return ContextCompat.getDrawable(context, id)!!
        }

        fun getByte(uri: Uri): ByteArray{
            val baos = ByteArrayOutputStream()
            val fis: FileInputStream
            try {
                fis = FileInputStream(File(uri.getPath()))
                val buf = ByteArray(1024)
                var n: Int
                while (-1 != fis.read(buf).also { n = it }) baos.write(buf, 0, n)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return baos.toByteArray()
        }

        fun saveFile(byteArray: ByteArray) : String{
            val outStream: FileOutputStream
            try {
                val path = File(Environment.getExternalStorageDirectory(), PATH_IMAGES)
                path.mkdirs()
                val fileName = "image_${System.currentTimeMillis()}$JPG"
                val file = File(path,fileName)
                Log.e("qqqq", "saveFile: ${file.path}" )
                outStream = FileOutputStream(file)
                outStream.write(byteArray)
                outStream.close()
                return file.path
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        fun closeKeyboard(activity: Activity){
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
package amingoli.com.selar.helper

import amingoli.com.selar.R
import amingoli.com.selar.database.AppDatabase
import amingoli.com.selar.helper.Config.JPG
import amingoli.com.selar.helper.Config.MONEY
import amingoli.com.selar.helper.Config.PATH
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.io.*
import java.lang.Exception
import java.net.URI
import java.text.DecimalFormat

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

        fun priceFormat(double: Double):String{
            val decimalFormat = DecimalFormat("###,###,###")
            return decimalFormat.format(double)
        }

        fun priceFormat(double: Double, showMoneyType: Boolean):String{
            return if (showMoneyType) "${priceFormat(double)} $MONEY"
            else priceFormat(double)
        }

        fun convertToDouble(editText: EditText): Double{
            return if (replaceForPrice(getString(editText)).isNullOrEmpty()) 0.0
            else replaceForPrice(getString(editText)).toDouble()
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
                val path = File(Environment.getExternalStorageDirectory(),"/AAAA/image")
                path.mkdirs()
                val fileName = "trano_keeper_" + System.currentTimeMillis() + ".jpg"
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
    }
}
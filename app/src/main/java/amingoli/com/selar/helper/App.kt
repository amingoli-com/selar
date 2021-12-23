package amingoli.com.selar.helper

import amingoli.com.selar.R
import amingoli.com.selar.database.AppDatabase
import amingoli.com.selar.helper.Config.JPG
import amingoli.com.selar.helper.Config.PATH
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.io.*
import java.lang.Exception
import java.net.URI

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

        fun saveFile(byteArray: ByteArray) {
            val outStream: FileOutputStream
            try {
                val path = File(Environment.getExternalStorageDirectory(),"/AAAA/image")
                path.mkdirs()
                val fileName = "trano_keeper_" + System.currentTimeMillis() + ".jpg"
                val file = File(path,fileName)
                Log.e("qqqq", "saveFile: $path $fileName" )
                outStream = FileOutputStream(file)
                outStream.write(byteArray)
                outStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
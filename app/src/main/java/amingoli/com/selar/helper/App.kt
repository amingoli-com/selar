package amingoli.com.selar.helper

import amingoli.com.selar.R
import amingoli.com.selar.database.AppDatabase
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

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
    }
}
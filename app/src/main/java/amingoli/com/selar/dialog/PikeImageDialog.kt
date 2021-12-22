package amingoli.com.selar.dialog

import amingoli.com.selar.R
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_pike_image.*

class PikeImageDialog (context: Context, private val listener: Listener) : AlertDialog(context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pike_image)

        choose_from_camera.setOnClickListener { listener.chooseImageFromCamera(this) }
        choose_from_gallery.setOnClickListener { listener.chooseImageFromGallery(this) }
    }

    interface Listener {
        fun chooseImageFromCamera(dialog: AlertDialog)
        fun chooseImageFromGallery(dialog: AlertDialog)
    }
}
package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Images {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var image_code :String? = null
    var path:String? = null
    var branch:Int? = null
    var possition:Int? = null
}
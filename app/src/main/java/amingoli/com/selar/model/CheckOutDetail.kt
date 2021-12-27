package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CheckOut {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var content :String? = null
    var check_out_number :String? = null

}
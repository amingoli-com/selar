package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Messages {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var title :String? = null
    var content :String? = null
    var description :String? = null
    var tag :String? = null
    var created_at : Date? = null
    var updated_at : Date? = null
}
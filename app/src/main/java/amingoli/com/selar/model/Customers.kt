package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Customers {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var name :String? = null
    var family :String? = null
    var phone :String? = null
    var agent_code :Int? = null
    var status :Int? = null
    var branch :Int? = null
    var created_at : Date? = null
    var updated_at : Date? = null
}
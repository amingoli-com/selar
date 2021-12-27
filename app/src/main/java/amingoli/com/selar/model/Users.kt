package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Users {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var name :String? = null
    var family :String? = null
    var phone :String? = null
    var password :String? = null
    var permission_type :String? = null
    var status :Int? = null
    var branch :Int? = null
}
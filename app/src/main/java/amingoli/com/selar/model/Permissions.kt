package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Permissions {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var permission_type :String? = null
    var value_array :String? = null
    var user_id :Int? = null
}
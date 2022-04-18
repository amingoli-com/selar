package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Business {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var owner_name :String? = null
    var business_name :String? = null
    var business_type :String? = null
    var session_key :String? = null
}
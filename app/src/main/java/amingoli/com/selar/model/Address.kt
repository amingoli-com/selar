package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var address :String? = null
    var city :String? = null
    var state :String? = null
    var customer_id :Int? = null
}
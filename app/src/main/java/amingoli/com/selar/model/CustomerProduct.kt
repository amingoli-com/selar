package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class CustomerProduct {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var customer_name :String? = null
    var customer_id :Int? = null
    var product_id :Int? = null
    var product_count :Int? = null
    var first : Date? = null
    var last : Date? = null
}
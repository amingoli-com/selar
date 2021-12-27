package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class OrderDetail {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var order_code :String? = null
    var product_id :Int? = null
    var name :String? = null
    var stock :Double? = null
    var increase_id :Int? = null
    var increase_name :String? = null
    var price_buy :Double? = null
    var price_sale :Double? = null
    var price_discount :Double? = null
    var price_profit :Double? = null
    var price_tax :Double? = null
}
package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
@TypeConverters(DateConverter::class)
class Orders {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var order_code :String? = null
    var order_type :Int? = null
    var status :Int? = null
    var discount_code :String? = null
    var branch :Int? = null
    var customer :Int? = null
    var total_price_order :Double? = null
    var total_price_profit :Double? = null
    var pay_discount :Double? = null
    var pay_cash :Double? = null
    var pay_card :Double? = null
    var pay_card_info :String? = null
    var pay_creadit :Double? = null
    var pay_tax :Double? = null
    var address :String? = null
    var location :String? = null
    var create_at : Date? = null
    var update_at :Date? = null
    var delivery_at :Date? = null
    var description_public :String? = null
    var description_private :String? = null

}
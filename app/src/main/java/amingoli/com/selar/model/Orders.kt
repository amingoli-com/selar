package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
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
    var customer_name :String? = null
    var customer_phone :String? = null
    var total_price_order :Double? = null
    var total_price_profit :Double? = null
    var pay_discount :Double? = null
    var pay_cash :Double? = null
    var pay_card :Double? = null
    var pay_card_info :String? = null
    var pay_tax :Double? = null
    var address :String? = null
    var location :String? = null
    var create_at : Date? = null
    var update_at :Date? = null
    var delivery_at :Date? = null
    var description_public :String? = null
    var description_private :String? = null



    @Ignore
    constructor(
        status: Int?,
        branch: Int?,
        total_price_order: Double?,
        total_price_profit: Double?,
        customer_name: String?,
        customer_phone: String?,
        pay_discount: Double?,
        pay_cash: Double?,
        pay_card: Double?,
        pay_card_info: String?,
        pay_tax: Double?,
        create_at: Date?
    ) {
        this.status = status
        this.branch = branch
        this.total_price_order = total_price_order
        this.total_price_profit = total_price_profit
        this.customer_name = customer_name
        this.customer_phone = customer_phone
        this.pay_discount = pay_discount
        this.pay_cash = pay_cash
        this.pay_card = pay_card
        this.pay_card_info = pay_card_info
        this.pay_tax = pay_tax
        this.create_at = create_at
    }

    constructor()
}
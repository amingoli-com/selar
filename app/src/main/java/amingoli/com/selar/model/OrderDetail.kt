package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class OrderDetail {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var order_code :String? = null
    var product_id :Int? = null
    var product_code :String? = null
    var product_image :String? = null
    var name :String? = null
    var stock :Double? = null
    var increase_id :Int? = null
    var increase_name :String? = null
    var price_buy :Double? = null
    var price_sale :Double? = null
    var price_discount :Double? = null
    var price_profit :Double? = null
    var tax_percent :Int? = null


    constructor()

    @Ignore
    constructor(
        id: Int?,
        order_code: String?,
        product_id: Int?,
        product_code: String?,
        product_image: String?,
        name: String?,
        stock: Double?,
        increase_id: Int?,
        increase_name: String?,
        price_buy: Double?,
        price_sale: Double?,
        price_discount: Double?,
        price_profit: Double?,
        tax_percent: Int?
    ) {
        this.id = id
        this.order_code = order_code
        this.product_id = product_id
        this.product_code = product_code
        this.product_image = product_image
        this.name = name
        this.stock = stock
        this.increase_id = increase_id
        this.increase_name = increase_name
        this.price_buy = price_buy
        this.price_sale = price_sale
        this.price_discount = price_discount
        this.price_profit = price_profit
        this.tax_percent = tax_percent
    }
}
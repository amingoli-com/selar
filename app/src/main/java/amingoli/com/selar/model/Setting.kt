package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Setting {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var branch: Int? = null
    var currency: String? = null
    var tax: Int? = null
    var min_order: Double? = null
    var shipping_price: Double? = null
    var shipping_free_on_order: Double? = null
    var pay_cash: Boolean? = null
    var pay_card: Boolean? = null
    var discount_code: Boolean? = null
    var store_is_enable: Boolean? = null
    var show_product_no_stock: Boolean? = null
    var show_product_by_status_id: Int? = null // inner table status.class
}
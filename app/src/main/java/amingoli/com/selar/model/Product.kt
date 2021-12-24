package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
class Product {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var qrcode :String? = null
    var name :String? = null
    var image_defult :String? = null
    var image_code :String? = null
    var descrption :String? = null
    var branch :Int? = null
    var status :Int? = null
    var stock :Double? = null
    var price_buy :Double? = 0.0
    var price_sale_on_product :Double? =  0.0
    var price_sale :Double? =  0.0
    var price_discount :Double? =  0.0
    var min_selection :Double? = null
    var max_selection :Double? = null
    var increase :String? = null
//    val created_at :Date? = null
//    val update_at :Date? = null
    var tax_percent :Int? = null
    var user :Int? = null
}
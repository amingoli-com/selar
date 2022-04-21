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
    var branch :Int? = null

    var orders_count :Double = 0.0            // تعداد اقلام سفارش داده شده
    var total_price_order :Double = 0.0       // مبلغ کل کالاهای سفارش داده شده
    var total_price_profit :Double = 0.0      // مبلغ سود این سفارش
    var total_tax :Double = 0.0               // مبلغ مالیات
    var totla_shipping :Double = 0.0          // هزینه ارسال
    var totla_all :Double = 0.0               // جمع کل سفارش با احتساب هزینه ارسال و مالیات و کسر تخفیف
    var amount_discount :Double = 0.0         // مبلغ کل تخفیف


    var customer :Int? = null
    var customer_name :String? = null
    var customer_phone :String? = null
    var customer_debtor :Double = 0.0         // مبلغ بدهی یا مانده مشتری از این سفارش

    var pay_cash :Double = 0.0             // مبلغ نقدی پرداخت شده
    var pay_card :Double = 0.0             // مبلغ پرداخت شده با کارتخوان
    var pay_card_info :String? = null             // اطلاعات کارت پرداخت شده

    var address :String? = null
    var location :String? = null

    var create_at : Date? = null
    var update_at :Date? = null
    var delivery_at :Date? = null

    var description_public :String? = null
    var description_private :String? = null

    var pay_discount_code :Double = 0.0
    var discount_code :String? = null

    constructor()
}
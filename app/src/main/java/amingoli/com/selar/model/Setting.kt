package amingoli.com.selar.model

import amingoli.com.selar.helper.Config.MONEY_TYPE_DEFAULT
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
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
    var cash_money: Boolean? = null
    var cash_card: Boolean? = null
    var cash_debit: Boolean? = null
    var cash_discount: Boolean? = null

    constructor()

    @Ignore
    constructor(branch : Int) {
        this.branch = branch
        this.currency = MONEY_TYPE_DEFAULT
        this.tax = 0
        this.min_order = 0.0
        this.shipping_price = 0.0
        this.shipping_free_on_order = 0.0
        this.cash_money = true
        this.cash_card = true
        this.cash_debit = true
        this.cash_discount = true
    }

    @Ignore
    constructor(
        id: Int?,
        branch: Int?,
        currency: String?,
        tax: Int?,
        min_order: Double?,
        shipping_price: Double?,
        shipping_free_on_order: Double?,
        cash_money: Boolean?,
        cash_card: Boolean?,
        cash_debit: Boolean?,
        cash_discount: Boolean?
    ) {
        this.id = id
        this.branch = branch
        this.currency = currency
        this.tax = tax
        this.min_order = min_order
        this.shipping_price = shipping_price
        this.shipping_free_on_order = shipping_free_on_order
        this.cash_money = cash_money
        this.cash_card = cash_card
        this.cash_debit = cash_debit
        this.cash_discount = cash_discount
    }

}
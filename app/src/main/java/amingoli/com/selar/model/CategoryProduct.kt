package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class CategoryProduct {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var id_product :Int? = null
    var id_category :Int? = null

    constructor()

    @Ignore
    constructor(id_product: Int?, id_category: Int?) {
        this.id_product = id_product
        this.id_category = id_category
    }


}
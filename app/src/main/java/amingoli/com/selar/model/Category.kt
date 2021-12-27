package amingoli.com.selar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var id_mother :Int? = null
    var name :String? = null
    var content :String? = null
    var image :String? = null
    var branch :Int? = null
    var status :Int? = null
}
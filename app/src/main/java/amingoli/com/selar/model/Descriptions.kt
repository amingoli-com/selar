package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Descriptions {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var content :String? = null
    var text :Double? = null
}
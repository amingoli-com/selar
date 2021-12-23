package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Branch {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var name :String? = null
    var type :String? = null
}
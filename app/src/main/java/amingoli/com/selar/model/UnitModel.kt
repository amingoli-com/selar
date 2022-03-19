package amingoli.com.selar.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class UnitModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var title: String? = null

    @Ignore
    constructor(title: String?) {
        this.title = title
    }

    constructor()


}
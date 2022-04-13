package amingoli.com.selar.model

import androidx.room.Entity
import androidx.room.Ignore
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

    constructor()

    @Ignore
    constructor(
        id: Int?,
        id_mother: Int?,
        name: String?,
        content: String?,
        image: String?,
        branch: Int?,
        status: Int?
    ) {
        this.id = id
        this.id_mother = id_mother
        this.name = name
        this.content = content
        this.image = image
        this.branch = branch
        this.status = status
    }
}
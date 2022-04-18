package amingoli.com.selar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Business {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null
    var owner_name :String? = null
    var business_name :String? = null
    var business_type :String? = null
    var session_key :String? = null
    var created_at : Date? = null
    var updated_at :Date? = null

    constructor()

    @Ignore
    constructor(
        owner_name: String?,
        business_name: String?,
        business_type: String?,
        session_key: String?,
        created_at: Date?,
        updated_at: Date?
    ) {
        this.owner_name = owner_name
        this.business_name = business_name
        this.business_type = business_type
        this.session_key = session_key
        this.created_at = created_at
        this.updated_at = updated_at
    }

}
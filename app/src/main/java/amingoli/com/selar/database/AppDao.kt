package amingoli.com.selar.database

import amingoli.com.selar.model.Product
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {

    @Insert
    fun insertProduct(product: Product)

    @Query("select * from product")
    fun selectProduct(): List<Product>
}
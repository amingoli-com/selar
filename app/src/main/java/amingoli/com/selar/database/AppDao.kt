package amingoli.com.selar.database

import amingoli.com.selar.model.Branch
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

    @Query("select count(id) from product")
    fun getAllProductCount(): Int

    @Query("select count(id) from product where stock <= 1 ")
    fun getOutOfStockProductCount(): Int


    @Query("select * from branch")
    fun selectBranch(): List<Branch>

    @Insert
    fun insertBranch(branch: Branch)

}
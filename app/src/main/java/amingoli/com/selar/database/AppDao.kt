package amingoli.com.selar.database

import amingoli.com.selar.model.Branch
import amingoli.com.selar.model.Category
import amingoli.com.selar.model.Product
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {

//    Product
    @Insert
    fun insertProduct(product: Product)

    @Query("select * from product")
    fun selectProduct(): List<Product>

    @Query("select count(id) from product")
    fun getAllProductCount(): Int

    @Query("select count(id) from product where stock <= 1 ")
    fun getOutOfStockProductCount(): Int

//    Category
    @Insert
    fun insertCategory(category: Category)

    @Update
    fun UpdateCategory(category: Category)

    @Query("select * from category")
    fun selectCategory(): List<Category>

    @Query("select * from category where status = :status")
    fun selectCategory(status: Int): List<Category>

//    Branch
    @Query("select * from branch")
    fun selectBranch(): List<Branch>

    @Insert
    fun insertBranch(branch: Branch)

}
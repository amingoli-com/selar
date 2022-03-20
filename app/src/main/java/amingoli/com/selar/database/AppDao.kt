package amingoli.com.selar.database

import amingoli.com.selar.model.*
import androidx.room.*

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category): Long

    @Query("select * from category")
    fun selectCategory(): List<Category>

    @Query("select * from category where id = :id")
    fun selectCategory(id: Int): Category

    @Query("select * from category where status = :status")
    fun selectCategoryByStatus(status: Int): List<Category>

    @Query("select * from category where id_mother = :id_mother")
    fun selectUnderCategory(id_mother: Int): List<Category>

//    Branch
    @Query("select * from branch")
    fun selectBranch(): List<Branch>

    @Insert
    fun insertBranch(branch: Branch)

//    Unit
    @Query("select * from unitmodel")
    fun selectUnit(): List<UnitModel>

    @Query("select * from unitmodel where title = :unit_title")
    fun selectUnit(unit_title: String): UnitModel

    @Insert
    fun insertUnit(unit: UnitModel)



}
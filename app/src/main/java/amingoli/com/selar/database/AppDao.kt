package amingoli.com.selar.database

import amingoli.com.selar.model.*
import androidx.room.*

@Dao
interface AppDao {

//    Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product) : Long

    @Query("select * from product")
    fun selectProduct(): List<Product>

    @Query("select * from product where id = :id_product")
    fun selectProduct(id_product: Int): Product

    @Query("select * from product where qrcode = :barcode")
    fun selectProductByQR(barcode: String): Product?

    @Query("SELECT * FROM product " +
            "INNER JOIN categoryproduct on product.id = categoryproduct.id_product " +
            "WHERE categoryproduct.id_category = :id_category ")
    fun selectProductByCategory(id_category: Int): List<Product>

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

//    Category Product
    @Query("DELETE FROM categoryproduct WHERE id_product = :id_product;")
    fun deleteCategoryProduct(id_product:Int)

    @Insert
    fun insertCategoryProduct(categoryProductList: ArrayList<CategoryProduct>)

    @Query("select * from categoryproduct where id_product = :id_product")
    fun selectCategoryProduct(id_product: Int): List<CategoryProduct>

//    Branch
    @Query("select * from branch")
    fun selectBranch(): List<Branch>

    @Insert
    fun insertBranch(branch: Branch)

//    Unit
    @Query("select * from unitmodel")
    fun selectUnit(): List<UnitModel>

    @Query("select * from unitmodel where title = :unit_title")
    fun selectUnit(unit_title: String): UnitModel?

    @Insert
    fun insertUnit(unit: UnitModel)



}
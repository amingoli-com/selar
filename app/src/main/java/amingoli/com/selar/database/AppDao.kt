package amingoli.com.selar.database

import amingoli.com.selar.helper.Session
import amingoli.com.selar.model.*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    //    Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: ArrayList<Product>) : List<Long>

    @Query("select * from product where branch=:branch")
    fun selectProduct(branch: Int): List<Product>

    @Query("select id from product where branch=:branch and id_code=:id_code limit 1")
    fun selectProductByIdCode(branch: Int, id_code:String): Product

    @Query("select id,image_defult,name,price_sale from product where branch=:branch")
    fun selectSmallSizeProduct(branch: Int): List<Product>

    @Query("select id,image_defult,name,price_sale from product where branch=:branch and id = :id")
    fun selectSmallSizeProduct(branch: Int, id: Int): List<Product>

    @Query("select id,image_defult,name,price_sale from product " +
            "where branch=:branch and name LIKE '%' || :search || '%' " +
            "or qrcode LIKE '%' || :search || '%' " +
            "or price_sale LIKE '%' || :search || '%'")
    fun searchSmallSizeProduct(branch: Int, search: String): List<Product>

    @Query("SELECT id,image_defult,name,price_sale FROM product " +
            "INNER JOIN categoryproduct on product.id = categoryproduct.id_product " +
            "WHERE product.branch=:branch and categoryproduct.id_category = :id_category ")
    fun selectSmallSizeProductByCategory(branch: Int, id_category: Int): List<Product>

    @Query("select * from product where branch=:branch and id = :id_product")
    fun selectProduct(branch: Int, id_product: Int): Product

    @Query("select * from product where branch=:branch and qrcode = :barcode")
    fun selectProductByQR(branch: Int,barcode: String): Product?

    @Query("SELECT * FROM product " +
            "INNER JOIN categoryproduct on product.id = categoryproduct.id_product " +
            "WHERE product.branch=:branch and categoryproduct.id_category = :id_category ")
    fun selectProductByCategory(branch: Int, id_category: Int): List<Product>

    @Query("select stock,price_buy from product where branch=:branch")
    fun productCount(branch: Int): List<Product>

    @Query("select count(id) from product where branch=:branch")
    fun getAllProductCount(branch: Int): Int

    @Query("select count(id) from product where branch=:branch and stock <= 1 ")
    fun getOutOfStockProductCount(branch: Int): Int

//    Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: ArrayList<Category>): List<Long>

    @Query("select * from category where branch=:branch")
    fun selectCategory(branch: Int): List<Category>

    @Query("select id from category where branch=:branch and id_code=:id_code limit 1")
    fun selectCategoryByIdCode(branch: Int, id_code:String): Category

    @Query("select * from category where branch=:branch and id = :id")
    fun selectCategory(branch: Int, id: Int): Category

    @Query("select * from category where branch=:branch and status = :status")
    fun selectCategoryByStatus(branch: Int, status: Int): List<Category>

    @Query("select * from category where branch=:branch and id_mother = :id_mother")
    fun selectUnderCategory(branch: Int, id_mother: Int): List<Category>

    @Query("select count(id) from category where branch=:branch and id_mother = :id_mother")
    fun sizeCategory(branch: Int,id_mother: Int): Int

    @Query("select count(id) from category where branch=:branch")
    fun categoryCount(branch: Int): Int

//    Category Product
    @Query("DELETE FROM categoryproduct WHERE id_product = :id_product")
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
    @Query("select * from unitmodel where branch=:branch")
    fun selectUnit(branch: Int): List<UnitModel>

    @Query("select * from unitmodel where branch=:branch and title = :unit_title")
    fun selectUnit(branch: Int, unit_title: String): UnitModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnit(unit: UnitModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnit(unit: ArrayList<UnitModel>)


//    Business
    @Query("select * from business")
    fun selectBusiness(): List<Business>

    @Query("select * from business where id != :id")
    fun selectBusinessExcept(id: Int): List<Business>

    @Query("select * from business where id = :id")
    fun selectBusiness(id: Int): Business?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusiness(business: Business) : Long

//    Setting
    @Query("select * from setting")
    fun selectSetting(): List<Setting>

    @Query("select * from setting where branch=:branch limit 1")
    fun selectSetting(branch: Int): Setting

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetting(setting: Setting) : Long

//    customer
    @Query("select * from customers where branch=:branch")
    fun selectCustomer(branch: Int): List<Customers>

    @Query("select * from customers where branch=:branch and status = 1")
    fun selectCustomerActive(branch: Int): List<Customers>

    @Query("select * from customers " +
            "where branch=:branch and name LIKE '%' || :search || '%' " +
            "or phone LIKE '%' || :search || '%' ")
    fun searchCustomer(branch: Int, search: String): List<Customers>

    @Query("select * from customers where branch=:branch and status = :status")
    fun selectCustomerByStatus(branch: Int, status: Int): List<Customers>

    @Query("select * from customers where branch=:branch and status = :id ")
    fun selectCustomerByOrderCount(branch: Int, id: Int): List<Customers>

    @Query("select * from customers where id = :id")
    fun selectCustomerById(id: Int): Customers?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(customer: Customers) : Long

    @Query("select count(id) from customers where branch=:branch")
    fun customerCount(branch: Int): Int

//    order
    @Query("select * from orders where branch=:branch ORDER BY ID DESC")
    fun selectOrders(branch: Int): List<Orders>

    @Query("select * from orders where id=:id LIMIT 1")
    fun selectOrdersById(id: Int): Orders

    @Query("select * from orders where branch=:branch and status = :status")
    fun selectOrders(branch: Int, status: Int): List<Orders>

    @Query("select * from orders where branch=:branch ORDER BY total_price_profit DESC")
    fun selectOrdersByMostProfit(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch ORDER BY total_price_profit ASC")
    fun selectOrdersByLeastProfit(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch and customer_debtor <= 0 ")
    fun selectOrdersByPied(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch and customer_debtor >= 1 ")
    fun selectOrdersByUnPied(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch and pay_card <=0 and pay_cash >= 1 ")
    fun selectOrdersByMoney(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch and pay_card >=1 and pay_cash <= 0 ")
    fun selectOrdersByCard(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch and pay_card >=1 and pay_cash >=1")
    fun selectOrdersMultiPay(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch ORDER BY orders_count DESC  ")
    fun selectOrdersMostCount(branch: Int): List<Orders>

    @Query("select * from orders where branch=:branch ORDER BY orders_count ASC  ")
    fun selectOrdersLeastCount(branch: Int): List<Orders>

    @Query("select * from orders " +
            "where branch=:branch and customer_name LIKE '%' || :search || '%' " +
            "or customer_phone LIKE '%' || :search || '%' "+
            "or total_price_order LIKE '%' || :search || '%' "+
            "or create_at LIKE '%' || :search || '%' ")
    fun searchOrders(branch: Int, search: String): List<Orders>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(orders: Orders) : Long

    @Query("select count(id) from orders where branch=:branch")
    fun orderCount(branch: Int): Int

//    order detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderDetail(orderDetail: ArrayList<OrderDetail>) : List<Long>

    @Query("select * from orderdetail where branch=:branch and order_code = :order_code")
    fun selectOrdersDetailByOrderCode(branch: Int, order_code: String): List<OrderDetail>

    @Query("DELETE FROM orderdetail WHERE branch=:branch and order_code = :order_code")
    fun deleteOrdersDetailByOrderCode(branch: Int, order_code: String)
}
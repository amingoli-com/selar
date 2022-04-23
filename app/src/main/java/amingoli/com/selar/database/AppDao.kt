package amingoli.com.selar.database

import amingoli.com.selar.helper.Config.ORDER_STATUS_WAITING
import amingoli.com.selar.model.*
import androidx.room.*

@Dao
interface AppDao {

//    Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product) : Long

    @Query("select * from product")
    fun selectProduct(): List<Product>

    @Query("select id,image_defult,name,price_sale from product")
    fun selectSmallSizeProduct(): List<Product>

    @Query("select id,image_defult,name,price_sale from product where id = :id")
    fun selectSmallSizeProduct(id: Int): List<Product>

    @Query("select id,image_defult,name,price_sale from product " +
            "where name LIKE '%' || :search || '%' " +
            "or qrcode LIKE '%' || :search || '%' " +
            "or price_sale LIKE '%' || :search || '%'")
    fun searchSmallSizeProduct(search: String): List<Product>

    @Query("SELECT id,image_defult,name,price_sale FROM product " +
            "INNER JOIN categoryproduct on product.id = categoryproduct.id_product " +
            "WHERE categoryproduct.id_category = :id_category ")
    fun selectSmallSizeProductByCategory(id_category: Int): List<Product>

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

    @Query("select count(id) from category where id_mother = :id_mother")
    fun sizeCategory(id_mother: Int): Int

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


//    Business
    @Query("select * from business")
    fun selectBusiness(): List<Business>

    @Query("select * from business where id != :id")
    fun selectBusinessExcept(id: Int): List<Business>

    @Query("select * from business where id = :id")
    fun selectBusiness(id: Int): Business?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusiness(business: Business) : Long

//    customer
    @Query("select * from customers")
    fun selectCustomer(): List<Customers>

    @Query("select * from customers where status = 1")
    fun selectCustomerActive(): List<Customers>

    @Query("select * from customers " +
            "where name LIKE '%' || :search || '%' " +
            "or phone LIKE '%' || :search || '%' ")
    fun searchCustomer(search: String): List<Customers>

    @Query("select * from customers where status = :status")
    fun selectCustomerByStatus(status: Int): List<Customers>

    @Query("select * from customers where status = :id ")
    fun selectCustomerByOrderCount(id: Int): List<Customers>

    @Query("select * from customers where id = :id")
    fun selectCustomer(id: Int): Customers?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(customer: Customers) : Long

//    order
    @Query("select * from orders ORDER BY ID DESC")
    fun selectOrders(): List<Orders>

    @Query("select * from orders where id = :id LIMIT 1")
    fun selectOrdersById(id: Int): Orders

    @Query("select * from orders where status = :status")
    fun selectOrders(status: Int): List<Orders>

    @Query("select * from orders ORDER BY total_price_profit DESC")
    fun selectOrdersByMostProfit(): List<Orders>

    @Query("select * from orders ORDER BY total_price_profit ASC")
    fun selectOrdersByLeastProfit(): List<Orders>

    @Query("select * from orders where customer_debtor <= 0 ")
    fun selectOrdersByPied(): List<Orders>

    @Query("select * from orders where customer_debtor >= 1 ")
    fun selectOrdersByUnPied(): List<Orders>

    @Query("select * from orders where pay_card <=0 and pay_cash >= 1 ")
    fun selectOrdersByMoney(): List<Orders>

    @Query("select * from orders where pay_card >=1 and pay_cash <= 0 ")
    fun selectOrdersByCard(): List<Orders>

    @Query("select * from orders where pay_card >=1 and pay_cash >=1")
    fun selectOrdersMultiPay(): List<Orders>

    @Query("select * from orders ORDER BY orders_count DESC  ")
    fun selectOrdersMostCount(): List<Orders>

    @Query("select * from orders ORDER BY orders_count ASC  ")
    fun selectOrdersLeastCount(): List<Orders>



    @Query("select * from orders " +
            "where customer_name LIKE '%' || :search || '%' " +
            "or customer_phone LIKE '%' || :search || '%' "+
            "or total_price_order LIKE '%' || :search || '%' "+
            "or create_at LIKE '%' || :search || '%' ")
    fun searchOrders(search: String): List<Orders>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(orders: Orders) : Long

//    order detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderDetail(orderDetail: ArrayList<OrderDetail>) : List<Long>


    @Query("select * from orderdetail where order_code = :order_code")
    fun selectOrdersDetailByOrderCode(order_code: String): List<OrderDetail>
}
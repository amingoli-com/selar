package amingoli.com.selar.database

import amingoli.com.selar.BuildConfig
import amingoli.com.selar.model.*
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Database(entities = [Product::class,Branch::class,Category::class,Status::class,Increase::class,
        Descriptions::class,CategoryProduct::class,Users::class,Customers::class,Images::class,
        CheckOut::class,CheckOutDetail::class,Orders::class,OrderDetail::class,Permissions::class,
        Setting::class, UnitModel::class, Business::class,
        CustomerProduct::class, ProductSaleRender::class], version = AppDatabase.VERSION)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns the DAO for this application.
     * @return The DAO for this application.
     */
    abstract fun getAppDao(): AppDao

    companion object {

        private const val TAG = "AppDatabase"

        const val VERSION = 1
        private const val DATABASE_NAME = "inventory_database.db"

        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Gets and returns the database instance if exists; otherwise, builds a new database.
         * @param context The context to access the application context.
         * @return The database instance.
         */
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        /**
         * Creates and returns the callback object to execute when the database is first created.
         * @return The callback object to execute when the database is first created.
         */
        private fun appDatabaseCallback(): Callback = object : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d(TAG, "Database has been created.")

                // Throws exception
                CoroutineScope(Dispatchers.IO).launch {
                    instance?.getAppDao()?.let { populateDbAsync(it) }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "Database has been opened.")
            }
        }

        /**
         * Builds and returns the database.
         * @param appContext The application context to reference.
         * @return The built database.
         */
        private fun buildDatabase(appContext: Context): AppDatabase {
            val builder =
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()

            // Execute the callback only in DEBUG mode.
            if (BuildConfig.DEBUG) {
                builder.addCallback(appDatabaseCallback())
            }
            return builder.build()
        }

        /**
         * Populates the database when it is first created, as a suspended operation.
         * @param appDao The application DAO to execute queries.
         */
        private suspend fun populateDbAsync(appDao: AppDao) {

            withContext(Dispatchers.IO) {
                // Populate your database here...
            }

        }
    }

    /**
     * Alert Table
     * */
    private var MIGRATION_1_2s: Migration? = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'orderdetail' ADD COLUMN 'image_defult' STRING DEFAULT ''")
            Log.d("VROM", "Migration")
        }
    }

    val MIGRATION_1_2 = object : Migration(1, 2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `User` (`id` INTEGER, PRIMARY KEY(`id`))")
        }
    }
}
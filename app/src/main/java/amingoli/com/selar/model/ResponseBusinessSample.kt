package amingoli.com.selar.model

class ResponseBusinessSample {
    val business : ArrayList<item>?= null

    val sample_data: SampleData? = null

    class item{
        var name:String?=null
        var content:String?=null
        var type:String?=null
        var data_url:String?=null

        constructor(custom_business : Boolean){
            if (custom_business){
                this.name = "کسب‌وکار شخصی شما"
                this.content = "ایجاد کسب‌وکار جدید با اطلاعات دلخواه شما"
                this.type = "custom"
            }
        }

        constructor()
    }

    class SampleData{
        val category : ArrayList<Category>?= null
        val product : ArrayList<Product>?= null
        val product_category : ArrayList<ProductCategory>?= null
        val unit : ArrayList<String>?= null
    }

    class Category{
        var id_code:String?=null
        var name:String?=null
        var image:String?=null
    }

    class Product{
        var id_code:String?=null
        var category:String?=null
        var title:String?=null
        var price:Double? = 0.0
        var unit:String?=null
        var image:String?=null
    }

    class ProductCategory{
        var id_code_product:String?=null
        var id_code_category:String?=null
    }
}
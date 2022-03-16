package amingoli.com.selar.model

class TagList {
    var title:String? = null
    var icon:Int? = null
    var tag:String? = null



    constructor(title: String?, icon: Int?, tag: String?) {
        this.title = title
        this.icon = icon
        this.tag = tag
    }

    constructor()
}
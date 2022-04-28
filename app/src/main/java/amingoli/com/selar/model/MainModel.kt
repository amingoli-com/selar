package amingoli.com.selar.model

import android.content.Intent

class MainModel {
    var image: Int? = null
    var title: String? = null
    var cover: String? = null
    var content: String? = null
    var action: Class<*>? = null

    constructor(image: Int?, title: String?, cover: String?, content: String?, action: Class<*>?) {
        this.image = image
        this.title = title
        this.cover = cover
        this.content = content
        this.action = action
    }
}
package domain

import java.util.ArrayList

class Family  (override var id: String) : Entity {
    var husband: String = ""
    var wife: String = ""
    val children = ArrayList<String>()
    val notes = ArrayList<String>()
    val sources = ArrayList<SourceRef>()
    var marriage: Event? = null

    override fun toString(): String {
        return "Family(id='$id', husband='$husband', wife='$wife', children=$children, notes=$notes, sources=$sources, marriage=$marriage)"
    }


}
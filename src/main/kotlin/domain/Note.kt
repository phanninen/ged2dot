package domain

class Note  (override var id: String) : Entity  {
    var teksti = StringBuffer("")
    constructor(id: String, text:String) : this(id) {
        teksti.append(text)
    }

    override fun toString(): String {
        return "Note(id='$id', teksti='$teksti')"
    }


}
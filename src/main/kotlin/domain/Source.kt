package domain

class Source  (override var id: String) : Entity  {
    var title: String? = null
    var publisher: String? = null
    var author: String? = null

    override fun toString(): String {
        return "Source(id='$id', title=$title, publisher=$publisher, author=$author)"
    }


}
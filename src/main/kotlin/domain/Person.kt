package domain

import java.util.ArrayList



class Person constructor (override var id: String) : Entity{
    var surname = ""
    var firstname = ""
    var sex = ""
    val familyId = ArrayList<String>()
    var parentsId: String? = null
    var birth: Event? = null
    var death: Event? = null
    val notes = ArrayList<String>()
    val sources = ArrayList<SourceRef>()

    override fun toString(): String {
        return "Person(id='$id', surname='$surname', firstname='$firstname', sex='$sex', familyId=$familyId, parentsId=$parentsId, birth=$birth, death=$death, notes=$notes, sources=$sources)"
    }

    fun getBirthYear() = birth?.getYear()
}
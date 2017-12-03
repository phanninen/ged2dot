package domain

import java.util.ArrayList



open class Event {
    var time = ""
    var place = ""
    val notes = ArrayList<String>()
    val sources = ArrayList<SourceRef>()

    fun getYear(): String {
        return if (this.time.length > 4)
            this.time.substring(this.time.length - 4)
        else
            this.time
    }

    override fun toString(): String {
        return "Event(time='$time', place='$place', notes=$notes, sources=$sources)"
    }

}
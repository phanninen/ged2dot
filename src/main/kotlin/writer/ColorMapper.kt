package writer

import domain.Person
import java.io.File
import java.io.InputStream
import java.util.*

class ColorMapper {
    internal var colors = Properties()

    fun load(filename : String) {
        colors.load( File(filename).inputStream())
    }

    fun getColorFor(name: String): String? {
        for (key in colors.keys) {
            if (name.startsWith(key as String))
                return colors.getProperty(key)
        }
        return null
    }
    fun getLineColor(person:Person) : String {
        val colorcheme = colors.getProperty("colorscheme")
        val color = getColorFor(person.surname)
        return if (color != null)
            "colorscheme=$colorcheme color=$color penwidth=4.0 "
        else
            " color=black "
    }

    fun getColor(person: Person) : String {
        val colorcheme = colors.getProperty("colorscheme")
        val color = getColorFor(person.surname)
        return if (color != null)
            " colorscheme=$colorcheme fillcolor=$color"
        else
            " color=black fillcolor=white "
    }
}
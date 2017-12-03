package writer

import domain.Family
import domain.Person
import pedigree.Pedigree
import java.io.PrintWriter

class PedigreeWriter(private val filename : String) {
    private val writer = PrintWriter(filename)
    private val selectedPersons = mutableSetOf<String>()
    private val selectedFamilies = mutableSetOf<String>()

    val colorMapper=ColorMapper()
    var pedigree : Pedigree? = null
    var startperson : String = "@I10@"

    fun createPedigree() {
        writer.println("digraph G {rankdir=LR;")
        writeConnections( startperson)
 //       printMap(pedigree, filter)
        // printFamilyMap(pedigree, filter)
        //   printGroups(pedigree, filter);
        printSelectedPersons()
        printSelectedFamilies()


        writer.println("}")
        writer.close()

    }

    private fun printSelectedFamilies(ranked:Boolean=true) {
        if (!ranked)
            selectedFamilies.forEach({f -> printFamily(pedigree!!.getFamily(f))})
        else {
            var currentRank = ""
            selectedFamilies.map({ f -> pedigree!!.getFamily(f) }).sortedBy({ it.marriage?.getYear() }).forEach {
                val rank = if ((it.marriage?.getYear() ?: "").isNotEmpty()) it.marriage!!.getYear().substring(0, 3) else ""
                if (currentRank.isNotEmpty() && !rank.equals(currentRank))
                    writer.println("}")
                if (!rank.equals(currentRank) && rank.isNotEmpty()) {
                    writer.println("{rank = same;")
                }
                printFamily(it)
                currentRank = rank

            }
            if (currentRank.isNotEmpty())
                writer.println("}")
        }
    }

    private fun printSelectedPersons() {
        selectedPersons.forEach({ p -> printPerson(pedigree!!.getPerson(p)) })
    }

    fun writeConnections(startPerson: String) {
        selectedPersons.add(startPerson)
        val person=pedigree!!.getPerson(startPerson)
        if (person.parentsId != null) {
            val parents = pedigree!!.getFamily(person.parentsId!!)
            outputConnection(person, parents)

            writeParent(parents, parents.husband)
            writeParent(parents, parents.wife)

        }

    }

    private fun writeParent(parents: Family, parent: String) {
        selectedFamilies.add(parents.id)
        if (parent.isNotEmpty()) {
            outputConnection(parents, pedigree!!.getPerson(parent))
            writeConnections(parent)
        }
    }


    private fun outputConnection(family: Family, person: Person?) {
        if (person != null) {
            writer.println("""
                "${family.id}" -> "${person.id}" [weight=100 ${colorMapper.getLineColor(person)}];
                """)
        }

    }


    private fun outputConnection(person: Person, family: Family) {
        writer.println("""
            "${person.id}" -> "${family.id}" [${colorMapper.getLineColor(person)} weight=10 constraint=true];
            """)

    }

    private fun printPerson(person: Person) {
        val color = colorMapper.getColor(person)
        writer.println("""
                "${person.id}" [shape=box style=filled fontname=helvetica $color
                 label="${person.firstname}\n${person.surname}\n(${person.birth?.getYear() ?: "    "} - ${person.death?.getYear() ?: "    "})"];
                """)

    }

    private fun printFamily(family : Family) {
        println(family.toString())
        val mies = if (family.husband.isNotEmpty()) pedigree!!.getPerson(family.husband) else Person("")
        writer.println("""
            "${family.id}" [shape=circle style=filled ${colorMapper.getColor(mies)} label="${family.marriage?.getYear() ?: ""}"];
            """)

    }
}
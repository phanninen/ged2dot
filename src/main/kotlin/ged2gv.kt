import gedcom.GedcomParser
import pedigree.InMemoryPedigree
import writer.PedigreeWriter

fun main(args: Array<String>) {
    val pedigree  = InMemoryPedigree()
    pedigree.load(args[0])

    with(PedigreeWriter(args[1])) {
        this.pedigree = pedigree
        colorMapper.load("../ged2gv/colors.properties")
        createPedigree()
    }


    val parser= GedcomParser(args[0])
    parser.parse().forEach{e->println(e)}
}
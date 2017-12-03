package pedigree

import domain.Family
import domain.Note
import domain.Person
import domain.Source
import gedcom.GedcomParser
import java.io.FileNotFoundException
import java.io.IOException
import java.io.UnsupportedEncodingException

interface Pedigree {
    abstract fun addPerson(person: Person)

    abstract fun addFamily(family: Family)

    abstract fun addSource(family: Source)

    abstract fun addNote(note: Note)

    abstract fun getPerson(id: String): Person

    abstract fun getFamily(id: String): Family

    abstract fun getSource(id: String): Source

    abstract fun getNote(id: String): Note

    @Throws(FileNotFoundException::class, UnsupportedEncodingException::class, IOException::class)
    fun load(gedcomFile: String) {

        val parser = GedcomParser(gedcomFile)

        for (entity in parser.parse()) {
            if (entity is Person)
                addPerson(entity as Person)
            if (entity is Family)
                addFamily(entity as Family)
            if (entity is Source)
                addSource(entity as Source)
            if (entity is Note)
                addNote(entity as Note)

        }


    }
}
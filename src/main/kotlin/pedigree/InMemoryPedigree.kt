package pedigree

import domain.Family
import domain.Note
import domain.Person
import domain.Source
import java.util.HashMap

class InMemoryPedigree : Pedigree {
    private val persons = mutableMapOf<String, Person>()
    private val families = mutableMapOf<String, Family>()
    private val sources = mutableMapOf<String, Source>()
    private val notes = mutableMapOf<String, Note>()


    override fun addPerson(person: Person) { persons[person.id] = person }

    override fun addFamily(family: Family) { families[family.id] = family }

    override fun addSource(source: Source) { sources[source.id] = source }

    override fun addNote(note: Note) { notes[note.id] = note }

    override fun getPerson(id: String): Person = persons[id]!!

    override fun getFamily(id: String): Family = families[id]!!

    override fun getSource(id: String): Source =sources[id]!!

    override fun getNote(id: String): Note = notes[id]!!
}
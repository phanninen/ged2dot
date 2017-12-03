package gedcom

import domain.*
import java.io.File
import java.io.InputStream


public class GedcomParser (private val filename : String) {
    private var currentEntity : Entity = Person("")
    private var currentEvent: Event? = null

    fun parse() : List<Entity>{
        val resultList = mutableListOf<Entity>()

        val inputStream: InputStream = File(filename).inputStream()
        inputStream.bufferedReader().useLines { lines -> lines.forEach {
            println(it)
           val tokens=it.split(" ",limit = 4)
           if (tokens[0]=="0" && tokens.size>2) {
                when (tokens[2]) {
                "INDI" -> {
                    if (currentEntity.id.isNotEmpty())
                        resultList.add(currentEntity)

                    currentEntity = Person(tokens[1])
                }
                "FAM" -> {
                        resultList.add(currentEntity)
                        currentEntity = Family(tokens[1])
                    }
                "SOUR" -> {
                        resultList.add(currentEntity)
                        currentEntity = Source(tokens[1])
                    }
                "NOTE" -> {
                        resultList.add(currentEntity)
                        currentEntity = Note(tokens[1], tokens[3])
                    }

                }

            }
            else {
               when (currentEntity) {
                   is Person -> addPersonLine(currentEntity as Person, it.split(" ",limit = 3))
                   is Family -> addFamilyLine(currentEntity as Family, it.split(" ",limit = 3))
                   is Source -> addSourceLine(currentEntity as Source,  it.split(" ",limit = 3))
                   is Note ->  addNoteLine(currentEntity as Note, it.split(" ",limit = 3))
               }
           }


        }
        }
        return resultList
    }

    private fun addPersonLine(person: Person, tokens: List<String>) {
        when (tokens[0]) {
            "1" -> {
                when (tokens[1]) {
                    "SEX" ->  person.sex = tokens[2]
                    "FAMC" -> person.parentsId = tokens[2]
                    "FAMS" -> person.familyId.add(tokens[2])
                    "NOTE" -> person.notes.add(tokens[2])
                    "BIRT" -> {
                        person.birth=Event()
                        currentEvent = person.birth
                    }
                    "DEAT" -> {
                        person.death=Event()
                        currentEvent = person.death
                    }
                    "SOUR" -> {
                        currentEvent = SourceRef(tokens[2])
                        person.sources.add(currentEvent as SourceRef)
                    }

                    else -> {
                        currentEvent = null
                    }

                }
            }
            "2" -> {
                when (tokens[1]) {
                    "SURN" -> if (person.surname.isEmpty()) person.surname = tokens[2]
                    "GIVN" -> person.firstname = tokens[2]
                    "DATE" -> currentEvent?.time = tokens[2]
                    "PLAC" -> if (tokens.size > 2) currentEvent?.place = tokens[2]
                    "SOUR" -> currentEvent?.sources?.add(SourceRef(tokens[2]))
                    "NOTE" -> {//
                        if (currentEvent != null)
                            currentEvent!!.notes.add(tokens[2])
                        else
                            person.notes.add(tokens[2])
                    }
                    "PAGE" ->  (currentEvent as SourceRef).page = tokens[2]


                }
            }
            "3" -> processLevel3Tags(tokens)



        }
    }

    private fun addFamilyLine(family: Family, tokens: List<String>) {
        if (tokens[0] == "1")
            currentEvent = null

        when (tokens[0]) {
        "1" -> {
            when (tokens[1]) {
                "HUSB" -> family.husband = tokens[2]
                "WIFE" -> family.wife = tokens[2]
                "CHIL" -> family.children.add(tokens[2])
                "NOTE" -> family.notes.add(tokens[2])
                "MARR" -> {
                    family.marriage = Event()
                    currentEvent = family.marriage
                }
                "SOUR" -> {
                    currentEvent = SourceRef(tokens[2])
                    family.sources.add(currentEvent as SourceRef)
                }
            }
        }
        "2" -> {
            when (tokens[1]) {
                "DATE" -> currentEvent?.time = tokens[2]
                "PLAC" -> currentEvent?.place = tokens[2]
                "SOUR" -> currentEvent?.sources?.add(SourceRef(tokens[2]))
                "NOTE" -> {//
                    if (currentEvent != null)
                        currentEvent?.notes?.add(tokens[2])
                    else
                        family.notes.add(tokens[2])
                }
                "PAGE" ->  (currentEvent as SourceRef).page = tokens[2]
           }
        }
        "3" ->  processLevel3Tags(tokens)



        }


    }

    private fun addSourceLine(source: Source, tokens: List<String>) {
        if (tokens[0] == "1") {
            when (tokens[1]) {
                "TITL" -> source.title = tokens[2]
                "PUBL" -> source.publisher = tokens[2]
                "AUTH" -> source.author = tokens[2]
            }
        }
    }

    private fun addNoteLine(note : Note, tokens: List<String>) {
        if (tokens[0] == "1") {
            when (tokens[1]) {
                "CONC" -> note.teksti.append(tokens[2])
                "CONT" -> note.teksti.append("\n").append(tokens[2])
            }
        }
    }


    private fun processLevel3Tags(tokens: List<String>) {
        when (tokens[1]) {

            "PAGE" ->  if (currentEvent != null)
                    currentEvent!!.sources.last().page =tokens[2]

            "NOTE" ->
                if (currentEvent?.sources!!.isEmpty())
                    currentEvent?.notes?.add(tokens[2])
                else
                    currentEvent?.sources!!.last().notes.add(tokens[2])
        }
    }

}
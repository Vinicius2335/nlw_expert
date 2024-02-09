import { ChangeEvent, useState } from "react"
import logo from "./assets/logo-nlw-expert.svg"
import { NewNoteCard } from "./components/new-note-card"
import { NoteCard } from "./components/note-card"
import { toast } from "sonner"

interface Note {
  id: string,
  date: Date,
  description: string
}

export function App() {
  const [search, setSearch] = useState('')

  const [notes, setNotes] = useState<Note[]>(() => {
    const notesOnStorage = localStorage.getItem('notes')

    if(notesOnStorage) {
      return JSON.parse(notesOnStorage)
    }

    return []
  })

  function onNoteCreated(description: string){
    const newNote = {
      id: crypto.randomUUID(),
      date: new Date(),
      description: description
    }

    const notesList = [newNote, ...notes]

    // cria uma nova lista, copia a lista de notes, adiciona a nova nota
    //setNotes([...notes, newNote])

    // ordem crescente
    // cria uma nova lista, adiciona a nova nota, depois copia a lista de notes
    setNotes([newNote, ...notes])

    // armazenando a lista de notas no storage do navegador para não perder as informações ao apertar f5
    localStorage.setItem('notes', JSON.stringify(notesList))
  }

  function onNoteDeleted(id: String){
    const noteList = notes.filter(note => {
      return note.id != id
    })

    setNotes(noteList)
    localStorage.setItem('notes', JSON.stringify(noteList))
    toast.success("Nota Removida com sucesso!")
  }

  function handleSearch(event: ChangeEvent<HTMLInputElement>){
    const query = event.target.value

    setSearch(query)
  }

  // Busca Notas
  const filterdNotes = search != '' ? notes.filter(
    note => note.description.toLowerCase().includes(search.toLowerCase())
    ) : notes

  return (
    <div className="mx-auto max-w-6xl my-12 space-y-6 px-5">
      <img src={logo} className="h-6 w-32" alt="Logo Nlw Expert" />

      <form className="w-full">
        <input
          type="text"
          placeholder="Busque em suas notas..."
          className="w-full bg-transparent text-3xl font-semibold tracking-tight placeholder:text-slade-500 outline-none text-slate-200"
          onChange={handleSearch}
        />
      </form>

      <div className="h-px bg-slate-700" />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 auto-rows-[250px] gap-6">
        <NewNoteCard onNoteCreated={onNoteCreated} />

        {
          filterdNotes.map(note => {
            return <NoteCard date={note.date} id={note.id} description={note.description} key={note.id} onNoteDeleted={onNoteDeleted}/>
          })
        }
      </div>
    </div>
  )
}

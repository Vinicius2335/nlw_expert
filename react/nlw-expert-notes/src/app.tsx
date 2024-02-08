import logo from "./assets/logo-nlw-expert.svg"
import { NewNoteCard } from "./components/new-note-card"
import { NoteCard } from "./components/note-card"

export function App() {
  return (
    <div className="mx-auto max-w-6xl my-12 space-y-6">
      <img src={logo} alt="Logo Nlw Expert" />

      <form className="w-full">
        <input
          type="text"
          placeholder="Busque em suas notas..."
          className="w-full bg-transparent text-3xl font-semibold tracking-tight placeholder:text-slade-500 outline-none text-slate-200"
        />
      </form>

      <div className="h-px bg-slate-700" />

      <div className="grid grid-cols-3 auto-rows-[250px] gap-6">
        
        <NewNoteCard />

        <NoteCard
          date={new Date()}
          description="O Drizzle possui um plugin do ESLint para evitar que realizemos updates ou deletes sem
          where... Para configurar o plugin, é preciso instalar como abaixo: Lorem ipsum dolor sit
          amet consectetur, adipisicing elit. Deleniti dolores perspiciatis suscipit unde fuga
          reiciendis debitis adipisci repudiandae quisquam enim neque possimus maxime corporis
          amet blanditiis error, nesciunt expedita vero! Lorem ipsum, dolor sit amet consectetur
          adipisicing elit. Et fuga nisi aliquam nobis recusandae, ut ducimus, excepturi ab
          reprehenderit voluptatum quidem nostrum architecto voluptatibus sed accusantium aperiam
          hic temporibus ea."
        />

        <NoteCard
          date={new Date()}
          description="No app do NLW vamos criar um layout incrível, assim podemos entregar a melhor
            experiência para a comunidade. Na aplicação React vamos criar um projeto que permite o
            usuário salvar notas em texto ou áudio. xqdele"
        />
      </div>
    </div>
  )
}

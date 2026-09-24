import React, {useState} from 'react'
import http from '../api/http'

export default function Login({onLogin}){
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const submit = async (e)=>{
    e.preventDefault()
    try{
      const r = await http.post('/auth/login', {email, password})
      localStorage.setItem('token', r.data.token)
      onLogin(r.data.usuario)
    }catch(e){
      alert('Credenciales inválidas')
    }
  }

  return (
    <div className="p-8 min-h-screen flex items-center justify-center">
      <div className="glass p-6 w-full max-w-md">
        <h2 className="text-xl font-semibold mb-4">Iniciar sesión</h2>
        <form onSubmit={submit} className="space-y-3">
          <input className="w-full p-2 border rounded" placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} required />
          <input type="password" className="w-full p-2 border rounded" placeholder="Password" value={password} onChange={e=>setPassword(e.target.value)} required />
          <div className="text-right">
            <button className="px-4 py-2 bg-blue-600 text-white rounded">Entrar</button>
          </div>
        </form>
      </div>
    </div>
  )
}

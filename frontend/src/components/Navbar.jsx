import React from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function Navbar({user, onLogout}){
  const nav = useNavigate()
  const logout = ()=>{
    localStorage.removeItem('token')
    onLogout()
    nav('/login')
  }

  return (
    <div className="glass p-4 flex items-center justify-between">
      <div className="font-bold">Abarrotes Lima</div>
      <div className="flex gap-4 items-center">
        <Link to="/" className="hover:underline">Dashboard</Link>
        <Link to="/productos" className="hover:underline">Productos</Link>
        <Link to="/ventas" className="hover:underline">Ventas</Link>
        {user ? (
          <>
            <span className="ml-4 text-sm">{user.nombre}</span>
            <button className="ml-2 px-3 py-1 bg-red-600 text-white rounded" onClick={logout}>Salir</button>
          </>
        ) : (
          <Link to="/login" className="ml-4 px-3 py-1 bg-blue-600 text-white rounded">Entrar</Link>
        )}
      </div>
    </div>
  )
}

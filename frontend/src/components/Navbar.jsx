import React from 'react'
import { Link } from 'react-router-dom'

export default function Navbar(){
  return (
    <div className="glass p-4 flex items-center justify-between">
      <div className="font-bold">Abarrotes Lima</div>
      <div className="flex gap-4">
        <Link to="/" className="hover:underline">Dashboard</Link>
        <Link to="/productos" className="hover:underline">Productos</Link>
        <Link to="/ventas" className="hover:underline">Ventas</Link>
      </div>
    </div>
  )
}

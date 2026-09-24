import React from 'react'

export default function Dashboard(){
  return (
    <div className="p-8 min-h-screen flex items-center justify-center">
      <div className="glass p-8 w-full max-w-4xl">
        <h1 className="text-2xl font-bold mb-4">Abarrotes Lima - Dashboard</h1>
        <div className="grid grid-cols-3 gap-4">
          <div className="p-4 glass">Ventas hoy: 0</div>
          <div className="p-4 glass">Productos: 0</div>
          <div className="p-4 glass">Proveedores: 0</div>
        </div>
      </div>
    </div>
  )
}

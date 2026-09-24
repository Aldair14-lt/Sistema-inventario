import React, {useEffect, useState} from 'react'
import http from '../api/http'

function ProductoRow({p}){
  return (
    <tr>
      <td className="px-4 py-2">{p.id}</td>
      <td className="px-4 py-2">{p.nombre}</td>
      <td className="px-4 py-2">{p.stockActual}</td>
      <td className="px-4 py-2">{p.precioVenta}</td>
    </tr>
  )
}

export default function Productos(){
  const [productos, setProductos] = useState([])

  useEffect(()=>{
    http.get('/productos').then(r=>setProductos(r.data)).catch(()=>{})
  },[])

  return (
    <div className="p-8 min-h-screen">
      <div className="glass p-6 max-w-5xl mx-auto">
        <h2 className="text-xl font-semibold mb-4">Productos</h2>
        <table className="w-full table-auto">
          <thead>
            <tr className="text-left">
              <th className="px-4 py-2">ID</th>
              <th className="px-4 py-2">Nombre</th>
              <th className="px-4 py-2">Stock</th>
              <th className="px-4 py-2">Precio</th>
            </tr>
          </thead>
          <tbody>
            {productos.map(p=> <ProductoRow key={p.id} p={p} />)}
          </tbody>
        </table>
      </div>
    </div>
  )
}

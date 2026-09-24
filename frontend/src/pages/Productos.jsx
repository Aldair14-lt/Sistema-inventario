import React, {useEffect, useState} from 'react'
import http from '../api/http'
import Modal from '../components/ui/Modal'

function ProductoRow({p, onEdit, onDelete}){
  return (
    <tr>
      <td className="px-4 py-2">{p.id}</td>
      <td className="px-4 py-2">{p.nombre}</td>
      <td className="px-4 py-2">{p.stockActual}</td>
      <td className="px-4 py-2">{p.precioVenta}</td>
      <td className="px-4 py-2">
        <button className="mr-2 px-3 py-1 bg-blue-600 text-white rounded" onClick={()=>onEdit(p)}>Editar</button>
        <button className="px-3 py-1 bg-red-600 text-white rounded" onClick={()=>onDelete(p.id)}>Eliminar</button>
      </td>
    </tr>
  )
}

const empty = {nombre: '', descripcion: '', codigoBarras: '', stockActual: 0, precioVenta: 0}

export default function Productos(){
  const [productos, setProductos] = useState([])
  const [open, setOpen] = useState(false)
  const [form, setForm] = useState(empty)
  const [editingId, setEditingId] = useState(null)

  const load = ()=>{
    http.get('/productos').then(r=>setProductos(r.data)).catch(()=>{})
  }

  useEffect(()=>{ load() },[])

  const onEdit = (p)=>{ setForm(p); setEditingId(p.id); setOpen(true) }
  const onNew = ()=>{ setForm(empty); setEditingId(null); setOpen(true) }

  const onDelete = (id)=>{
    if(!confirm('Eliminar producto?')) return
    http.delete(`/productos/${id}`).then(()=> load())
  }

  const submit = (e)=>{
    e.preventDefault()
    const payload = {...form}
    const action = editingId ? http.put(`/productos/${editingId}`, payload) : http.post('/productos', payload)
    action.then(()=>{ setOpen(false); load() }).catch(err=> alert('Error'))
  }

  return (
    <div className="p-8 min-h-screen">
      <div className="glass p-6 max-w-5xl mx-auto">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold">Productos</h2>
          <div>
            <button className="px-4 py-2 bg-green-600 text-white rounded" onClick={onNew}>Nuevo</button>
          </div>
        </div>
        <table className="w-full table-auto">
          <thead>
            <tr className="text-left">
              <th className="px-4 py-2">ID</th>
              <th className="px-4 py-2">Nombre</th>
              <th className="px-4 py-2">Stock</th>
              <th className="px-4 py-2">Precio</th>
              <th className="px-4 py-2">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productos.map(p=> <ProductoRow key={p.id} p={p} onEdit={onEdit} onDelete={onDelete} />)}
          </tbody>
        </table>
      </div>

      <Modal open={open} onClose={()=>setOpen(false)}>
        <form onSubmit={submit} className="space-y-3">
          <div>
            <label className="block text-sm font-medium">Nombre</label>
            <input className="w-full mt-1 p-2 border rounded" value={form.nombre} onChange={e=>setForm({...form, nombre: e.target.value})} required />
          </div>
          <div>
            <label className="block text-sm font-medium">Código</label>
            <input className="w-full mt-1 p-2 border rounded" value={form.codigoBarras} onChange={e=>setForm({...form, codigoBarras: e.target.value})} />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium">Stock</label>
              <input type="number" className="w-full mt-1 p-2 border rounded" value={form.stockActual} onChange={e=>setForm({...form, stockActual: Number(e.target.value)})} required />
            </div>
            <div>
              <label className="block text-sm font-medium">Precio venta</label>
              <input type="number" step="0.01" className="w-full mt-1 p-2 border rounded" value={form.precioVenta} onChange={e=>setForm({...form, precioVenta: Number(e.target.value)})} required />
            </div>
          </div>
          <div className="text-right">
            <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">Guardar</button>
          </div>
        </form>
      </Modal>
    </div>
  )
}

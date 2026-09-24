import React, {useState} from 'react'
import http from '../api/http'

export default function Ventas(){
  const [items, setItems] = useState([])
  const [codigo, setCodigo] = useState('')

  const addByCodigo = async ()=>{
    if(!codigo) return
    try{
      // Backend may not support search by query param; fetch all and try matching
      const r = await http.get('/productos')
      const productos = r.data || []
      const producto = productos.find(p => p.codigoBarras === codigo || String(p.id) === codigo || p.nombre.toLowerCase().includes(codigo.toLowerCase()))
      if(!producto) { alert('Producto no encontrado'); return }
      const exists = items.find(i=>i.producto.id===producto.id)
      if(exists){
        setItems(items.map(i=> i.producto.id===producto.id?{...i, cantidad:i.cantidad+1}:i))
      } else {
        setItems([...items, {producto, cantidad:1}])
      }
      setCodigo('')
    }catch(e){console.error(e)}
  }

  const total = items.reduce((s,i)=> s + (i.producto.precioVenta * i.cantidad), 0)
  const igv = total * 0.18
  const subtotal = total - igv

  const confirmVenta = async ()=>{
    const detalle = items.map(i=>({producto: {id: i.producto.id}, cantidad: i.cantidad, precioUnitario: i.producto.precioVenta, subtotal: i.producto.precioVenta * i.cantidad}))
    const payload = {serie:'F001', numero: String(Date.now()), subtotal, igv, total, usuario: {id:1}, detalleVentas: detalle}
    try{
      const r = await http.post('/ventas', payload)
      alert('Venta registrada: '+r.data.id)
      setItems([])
    }catch(e){console.error(e); alert('Error al registrar venta')}
  }

  return (
    <div className="p-8 min-h-screen">
      <div className="glass p-6 max-w-5xl mx-auto">
        <h2 className="text-xl font-semibold mb-4">Punto de Venta</h2>
        <div className="flex gap-2 mb-4">
          <input className="p-2 rounded flex-1" value={codigo} onChange={e=>setCodigo(e.target.value)} placeholder="Código de barras o nombre" />
          <button className="bg-blue-600 text-white px-4 py-2 rounded" onClick={addByCodigo}>Agregar</button>
        </div>

        <table className="w-full table-auto mb-4">
          <thead>
            <tr className="text-left"><th className="px-4 py-2">Producto</th><th className="px-4 py-2">Cantidad</th><th className="px-4 py-2">Precio</th></tr>
          </thead>
          <tbody>
            {items.map((it,idx)=> (
              <tr key={idx}><td className="px-4 py-2">{it.producto.nombre}</td><td className="px-4 py-2">{it.cantidad}</td><td className="px-4 py-2">{(it.producto.precioVenta * it.cantidad).toFixed(2)}</td></tr>
            ))}
          </tbody>
        </table>

        <div className="text-right mb-4">
          <div>Subtotal: S/. {subtotal.toFixed(2)}</div>
          <div>IGV (18%): S/. {igv.toFixed(2)}</div>
          <div className="text-2xl font-bold">Total: S/. {total.toFixed(2)}</div>
        </div>

        <div className="flex justify-end">
          <button className="bg-green-600 text-white px-4 py-2 rounded" onClick={confirmVenta} disabled={items.length===0}>Confirmar Venta</button>
        </div>
      </div>
    </div>
  )
}

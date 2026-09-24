import React from 'react'

export default function Modal({open, onClose, children}){
  if(!open) return null
  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
      <div className="glass p-6 max-w-md w-full">
        {children}
        <div className="text-right mt-4">
          <button className="px-4 py-2 bg-gray-700 text-white rounded" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  )
}

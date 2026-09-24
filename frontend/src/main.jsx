import React, {useState} from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import './index.css'
import Dashboard from './pages/Dashboard'
import Productos from './pages/Productos'
import Ventas from './pages/Ventas'
import Navbar from './components/Navbar'
import Login from './pages/Login'

function App(){
  const [user, setUser] = useState(null)

  const onLogin = (u)=> setUser(u)
  const onLogout = ()=> setUser(null)

  const Private = ({children}) => {
    const token = localStorage.getItem('token')
    return token ? children : <Navigate to="/login" />
  }

  return (
    <BrowserRouter>
      <div className="min-h-screen">
        <Navbar user={user} onLogout={onLogout} />
        <Routes>
          <Route path="/login" element={<Login onLogin={onLogin} />} />
          <Route path="/" element={<Private><Dashboard/></Private>} />
          <Route path="/productos" element={<Private><Productos/></Private>} />
          <Route path="/ventas" element={<Private><Ventas/></Private>} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

createRoot(document.getElementById('root')).render(<App />)

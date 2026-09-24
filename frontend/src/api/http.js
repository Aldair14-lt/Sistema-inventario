import axios from 'axios'

const http = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' }
})

// attach token automatically if present
http.interceptors.request.use(req=>{
  const token = localStorage.getItem('token')
  if(token) req.headers.Authorization = `Bearer ${token}`
  return req
})

export default http

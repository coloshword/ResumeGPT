import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import ChatInterface from './pages/ChatInterface/ChatInterface';

function App() {

  return (
    <>
      <Router>
        <Routes>
            <Route path="/chat" element={<ChatInterface/>}/>
        </Routes>
      </Router>
    </>
  )
}

export default App

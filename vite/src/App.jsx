import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home.jsx";
import OAuthSuccess from "./pages/OAuthSuccess.jsx";


function App() {


  return (
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/oauth-success" element={<OAuthSuccess />} />
          </Routes>
      </BrowserRouter>
  )
}

export default App

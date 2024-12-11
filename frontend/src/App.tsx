import {LoginForm} from "./components/Login";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RelatorioAluno from "./components/RelatorioAluno";
import GoogleStyleForm from "./components/Forms";
import HomeProfessor from "./components/HomeProfessor";
import HomeAluno from "./components/HomeAluno";
import HomeCCP from "./components/HomeCCP";
import { PrivateRoute } from "@/components/PrivateRoute";
import ReenvioRelatorio from "./components/ReenvioRelatorio";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route
          path="/homeAluno"
          element={<PrivateRoute role="ROLE_ALUNO"><HomeAluno /></PrivateRoute>}
        />
        <Route
          path="/homeCCP"
          element={<PrivateRoute role="ROLE_CCP"><HomeCCP /></PrivateRoute>}
        />
        <Route
          path="/homeProfessor"
          element={<PrivateRoute role="ROLE_ORIENTADOR"><HomeProfessor /></PrivateRoute>}
        />
        <Route path="/:nusp/:id" element={<RelatorioAluno/>} />
        <Route path="/form" element={<GoogleStyleForm />} />
        <Route path="/reenvio/:nusp/:idRelatorio" element={<ReenvioRelatorio/>} />
      </Routes>
    </Router>
  )
}

export default App

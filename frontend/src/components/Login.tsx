import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";
import axios from "axios";

export function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // Função para lidar com o login
  const handleLogin = async () => {
    console.log("Iniciando login...");
    console.log("Username:", username);
    console.log("Password:", password);

    try {
      // Fazer a requisição POST para o backend
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        {
          username,
          senha: password, // Note que o campo é "senha" e não "password"
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Resposta da API:", response);

      const token = response.data.token;
      console.log("Token recebido:", token);

      // Verificar se o token foi retornado
      if (!token) {
        setError("Token não recebido do backend.");
        console.error("Token não encontrado na resposta.");
        return;
      }

      // Armazenar o token e nusp no localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("nusp", username);

      // Decodificar o token para obter a role
      const decodedToken: any = jwtDecode(token);
      console.log("Token decodificado:", decodedToken);

      const userRoles = decodedToken.roles;
      console.log("Roles do usuário:", userRoles);

      if (userRoles.includes("ROLE_ALUNO")) {
        console.log("Redirecionando para /homeAluno");
        window.location.href = "/homeAluno";
      } else if (userRoles.includes("ROLE_CCP")) {
        console.log("Redirecionando para /homeCCP");
        window.location.href = "/homeCCP";
      } else if (userRoles.includes("ROLE_ORIENTADOR")) {
        console.log("Redirecionando para /homeProfessor");
        window.location.href = "/homeProfessor";
      } else {
        setError("Role não definida.");
        console.error("Role não definida:", userRoles);
      }
      
    } catch (err: any) {
      console.error("Erro ao fazer login:", err);
      console.error("Detalhes do erro:", err.response?.data || err.message);
      setError("Erro ao fazer login. Verifique suas credenciais.");
      setUsername("");
      setPassword("");
    }
  };

  return (
    <div className="bg-[url('./img/each.jpg')] bg-cover bg-center flex items-center justify-center min-h-screen">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle className="text-2xl">Login</CardTitle>
        </CardHeader>
        <CardContent className="grid gap-4">
          <div className="grid gap-2">
            <Label htmlFor="usuario">Usuário</Label>
            <Input
              id="usuario"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="password">Senha</Label>
            <Input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="text-center text-red-600">{error}</p>}
        </CardContent>
        <CardFooter>
          <Button
            onClick={handleLogin}
            className="w-full bg-blue-600 hover:bg-blue-800"
          >
            Entrar
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

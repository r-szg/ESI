import { Navigate } from "react-router-dom";
import {jwtDecode} from "jwt-decode";

interface PrivateRouteProps {
  children: JSX.Element;
  role: string;
}

export function PrivateRoute({ children, role }: PrivateRouteProps) {
  const token = localStorage.getItem("token");

  if (!token) {
    console.log("Token não encontrado, redirecionando para login");
    return <Navigate to="/" />;
  }

  try {
    const decodedToken: any = jwtDecode(token);
    const userRoles = decodedToken.roles;
    console.log("Token decodificado:", decodedToken);
    console.log("Roles do usuário:", userRoles);

    if (!userRoles.includes(role)) {
      console.log("Role não permitida, redirecionando para login");
      return <Navigate to="/" />;
    }

    return children;
  } catch (error) {
    console.error("Erro ao decodificar token:", error);
    return <Navigate to="/" />;
  }
}

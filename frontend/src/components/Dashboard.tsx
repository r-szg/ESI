"use client"
import { useEffect, useState } from 'react';
import { Badge } from "@/components/ui/badge"
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { useNavigate } from "react-router-dom"
import axios from 'axios';

interface Relatorio {
  id: number;
  nomeDiscente: string;
  numeroUsp: number;
  tipoCurso: string;
  prazo: string;
  nomeDocente: string;
}

export default function Component({ showDocenteColumn = false }) {
  const [relatoriosDocente, setRelatoriosDocente] = useState<Relatorio[]>([]);
  const navigate = useNavigate();

  //Função para direcionar para página do aluno especifico
  const handleRowClick = (relatorio: Relatorio) => {
    navigate(`/${relatorio.numeroUsp}/${relatorio.id}`);
  }

  useEffect(() => {
    if(showDocenteColumn){
      fetchDataCCP();
    }
    else{
      fetchData();
    }
  }, [])
  
  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const nusp = localStorage.getItem("nusp");
  
      if (!token) {
        throw new Error('Token not found. Please log in again.');
      }
      if (!nusp) {
        throw new Error('User not found. Please log in again.');
      }

      const response = await axios.get(`http://localhost:8080/api/relatorios/orientador/${nusp}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      response.data.map((data: any) => {
        const newData: Relatorio = {
          id: data.id,
          nomeDiscente: data.aluno.nomeCompleto,
          numeroUsp: data.aluno.numeroUsp,
          tipoCurso: data.aluno.tipoCurso,
          prazo: data.aluno.dataLimiteDepositoTrabalhoFinal,
          nomeDocente: data.docente.nomeCompleto,
        }
        setRelatoriosDocente((prevRelatorios) => [...prevRelatorios, newData]);
      })
    } catch (error) {
      console.error('Error na requisição GET:', error);
      throw error;
    }
  };

  const fetchDataCCP = async () => {
    try {
      const token = localStorage.getItem('token');
      const nusp = localStorage.getItem("nusp");
  
      if (!token) {
        throw new Error('Token not found. Please log in again.');
      }
      if (!nusp) {
        throw new Error('User not found. Please log in again.');
      }

      const response = await axios.get(`http://localhost:8080/api/relatorios`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      response.data.map((data: any) => {
        const newData: Relatorio = {
          id: data.id,
          nomeDiscente: data.aluno.nomeCompleto,
          numeroUsp: data.aluno.numeroUsp,
          tipoCurso: data.aluno.tipoCurso,
          prazo: data.aluno.dataLimiteDepositoTrabalhoFinal,
          nomeDocente: data.docente.nomeCompleto,
        }
        setRelatoriosDocente((prevRelatorios) => [...prevRelatorios, newData]);
      })
    } catch (error) {
      console.error('Error na requisição GET:', error);
      throw error;
    }
  };

  return (
    <Card className="h-screen w-screen flex flex-col">
    <CardHeader>
        <CardTitle className="flex justify-between items-center">
          <div className='text-4xl'>
            Projetos
          </div>
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="hidden w-[100px] sm:table-cell">
                <span className="sr-only">Image</span>
              </TableHead>
              {showDocenteColumn && <TableHead>Nome Docente</TableHead>}
              <TableHead>Nome Discente</TableHead>
              <TableHead className="hidden md:table-cell">Número USP</TableHead>
              <TableHead className="hidden md:table-cell">Prazo de Entrega</TableHead>
              <TableHead className="hidden md:table-cell">Curso</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {relatoriosDocente.map((relatorio:Relatorio) => (
              <TableRow className="cursor-pointer" onClick={() => handleRowClick(relatorio)}>
                <TableCell className="hidden sm:table-cell">
                </TableCell>
                {showDocenteColumn && <TableCell>{relatorio.nomeDocente || "N/A"}</TableCell>}
                <TableCell className="font-medium">
                  {relatorio.nomeDiscente}
                </TableCell>
                <TableCell className="hidden md:table-cell">{relatorio.numeroUsp}</TableCell>
                <TableCell className="hidden md:table-cell">{new Date(relatorio.prazo).toLocaleDateString('pt-br')}</TableCell>
                <TableCell>
                  <Badge variant="outline">{relatorio.tipoCurso}</Badge>
                </TableCell>
                <TableCell>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
      <CardFooter>
        <div className="text-xs text-muted-foreground">
          Mostrando <strong>{relatoriosDocente.length}</strong> alunos
        </div>
      </CardFooter>
    </Card>
  )
}

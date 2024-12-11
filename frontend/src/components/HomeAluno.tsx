import { useNavigate } from 'react-router-dom';
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"

import { Button } from "@/components/ui/button"
import { useEffect, useState } from 'react';
import axios from 'axios';

interface Parecer {
    id: number;
    parecer: string;
    desempenhoRelatorio: string;
    idRelatorio: number;
    numeroUspParecista: number;
    nomeParecista: string;
}

interface Entrega {
    nomeDiscente: string;
    prazo: string;
    numeroEntrega: string;
    statusRelatorio: string;
}

export default function HomeAluno(){
    const [dadosEntrega, setDadosEntrega] = useState<Entrega>({
        nomeDiscente: '',
        prazo: '',
        numeroEntrega: '',
        statusRelatorio: '',
    });    
    const [dadosPareceres, setDadosPareceres] = useState<Parecer[]>([])
    // setDadosPareceres([]);
    const navigate = useNavigate();

    const navigateForms = () => {
        navigate("/form");
    }

    const getNumeroEntrega = (numeroEntrega: number) =>{
        if (numeroEntrega === 1) return "Primeira"
        if (numeroEntrega === 2) return "Segunda"
        if (numeroEntrega === 3) return "Terceira"
        if (numeroEntrega > 3) return numeroEntrega.toString()
        
        return "Indefinido"
    }

    const handleStatusEntrega = (status: string) => {
        if(status === "ENTREGUE") return

        return (
            <Button onClick={navigateForms} className="bg-blue-600 hover:bg-blue-800">Preencher Relatório</Button>
        )
    }

    const handleReenvio = (nota: string, idRelatorio: number) => {
        if(nota === "INADEQUADO") {
            return (
                <Button onClick={() => navigateReenvio(idRelatorio)} className="bg-black hover:bg-blue-800 mt-2">Reenviar Relatório {idRelatorio}</Button>
            )
        }
    }

    const navigateReenvio  = (idRelatorio: number) => {
        navigate(`/reenvio/${localStorage.getItem("nusp")}/${idRelatorio}`);
      }

    useEffect(() => {
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
                        
              const response = await axios.get(`http://localhost:8080/api/status_relatorios/${nusp}`, {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              });
              
              setDadosEntrega((prevDadosEntrega) => ({
                  ...prevDadosEntrega,
                  nomeDiscente: response.data.aluno.nomeCompleto,
                  prazo: response.data.prazoEntregaRelatorio,
                  numeroEntrega: getNumeroEntrega(response.data.numeroDaEntrega),
                  statusRelatorio: response.data.situacaoEntregaRelatorio,
                }));
              
            } catch (error) {
              console.error('Error na requisição GET:', error);
              throw error;
            }
        };

        fetchData();
        fetchRelatorio();
    }, [])

    const fetchRelatorio = async () => {
        try {
          const token = localStorage.getItem('token');
          const nusp = localStorage.getItem("nusp");
      
          if (!token) {
            throw new Error('Token not found. Please log in again.');
          }
          if (!nusp) {
            throw new Error('User not found. Please log in again.');
          }
                    
          const response = await axios.get(`http://localhost:8080/api/relatorios/aluno/${nusp}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          response.data.map((data: any) => {
            data.pareceres?.map((parecer: any) => {
                const newData: Parecer = {
                    id: parecer.id,
                    parecer: parecer.parecer,
                    desempenhoRelatorio: parecer.desempenhoRelatorio,
                    idRelatorio: data.id,
                    numeroUspParecista: parecer.parecista?.numeroUsp,
                    nomeParecista: parecer.parecista?.nomeCompleto,
                }                
                setDadosPareceres((prevPareceres) => [...prevPareceres, newData]);
            })
          });
        } catch (error) {
          console.error('Error na requisição GET:', error);
          throw error;
        }
    };

    return(
        <div className="flex flex-col items-center justify-center min-h-screen gap-4 py-8">
            <Card className="w-full max-w-screen-sm p-4">
                <CardHeader className="text-center">
                    <CardTitle>Relatório de Pós Graduação</CardTitle>
                </CardHeader>
                <CardContent className="flex justify-center flex-col">
                    <div className="flex justify-around flex-row p-5 wrap">
                        <div>
                            <label className='text-md font-medium text-gray-700 pb-1'>Nome do Discente: </label>
                            <p className='text-center'>{dadosEntrega.nomeDiscente}</p>
                        </div>
                        <div>
                            <label className='text-md font-medium text-gray-700 pb-1'>Prazo de Entrega: </label>
                            <p className='text-center'>{new Date(dadosEntrega.prazo).toLocaleDateString("pt-br")}</p>
                        </div>
                    </div>

                    <div>
                        <p className="pt-1"><strong>Número da Entrega: </strong>{dadosEntrega.numeroEntrega}</p>
                        <p className="pt-1"><strong>Status do Relatório: </strong>{dadosEntrega.statusRelatorio}</p>
                    </div>
                </CardContent>
                <CardFooter className="flex justify-center">
                    {handleStatusEntrega(dadosEntrega.statusRelatorio)}
                </CardFooter>
            </Card>
            <Card className="w-full max-w-screen-sm p-4">
                <CardHeader className="text-center">
                    <CardTitle>Pareceres</CardTitle>
                </CardHeader>
                <CardContent className="flex justify-center flex-col">
                    <div>
                        {dadosPareceres?.map((parecer) => (
                            <>
                                <p className='pt-6 font-bold'>Parecer {parecer.id}</p>
                                <div className='pl-4'>
                                    <p className='font-medium'>ID Relatório: {parecer.idRelatorio}</p>
                                    <p>Avaliador: {parecer.nomeParecista}</p>
                                    <p>NUSP Avaliador: {parecer.numeroUspParecista}</p>
                                    <p>Nota: {parecer.desempenhoRelatorio}</p>
                                    <p>Observações: {parecer.parecer}</p>
                                    {handleReenvio(parecer.desempenhoRelatorio, parecer.idRelatorio)}
                                </div>
                            </>
                        ))}
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
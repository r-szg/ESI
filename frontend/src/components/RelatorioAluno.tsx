import { useNavigate, useNavigation, useParams } from "react-router-dom"
import { useEffect, useState } from "react";
import axios from "axios";
import TextInput from "./formsComponents/TextInput";
import SelectInput from "./formsComponents/SelectInput";
import { Card } from "./ui/card";

interface Parecer {
    parecer: string;
    desempenhoRelatorio: string;
}

interface Aluno {
    dataAprovacaoExameProficienciaLinguas: string;
    dataAprovacaoExameQualificacao: string;
    dataLimiteDepositoTrabalhoFinal: string;
    email: string;
    linkLattes: string;
    nomeCompleto: string;
    numeroUsp: string;
    tipoCurso: string;
}

interface Relatorio {
    id: number;
    aluno: Aluno;
    artigosAceitosOuPublicados: string;
    artigosEmFaseEscrita: string;
    artigosSubmetidosEEmPeriodoAvaliacao: string;
    declaracaoAdicionalParaCCP: string;
    necessidadeApoioCoordenacao: boolean;
    pareceres: Parecer[];
    participacaoAtividadesAcademicas2024S1: string;
    prazoMaximoDepositoDissertacao: string;
    prazoMaximoInscricaoExameQualificacao: string;
    quantidadeAprovacoesDesdeInicioCurso: string;
    quantidadeReprovacoes2023S2: string;
    quantidadeReprovacoesDesdeInicioCurso: string;
    realizouExameProficienciaLinguas: string;
    realizouExameQualificacao: string;
    resultadoUltimoRelatorio: string;
    resumoAtividadesPesquisa: string;    
}

interface FormData {
    id: number;
    parecer: string;
    desempenhoRelatorio: string;
}

export default function RelatorioAluno(){
    const navigate = useNavigate();
    const {nusp} = useParams <{ nusp:string }>();
    const {id} = useParams <{ id:string }>();
    
    const nuspNum = Number(nusp);
    const idNum = Number(id);

    const [relatorio, setRelatorio] = useState<Relatorio>();

    const [formData, setFormData] = useState<FormData>({
        id: idNum,
        parecer: '',
        desempenhoRelatorio: '',
    });

    const [wasEvaluated, setWasEvaluated] = useState(false);

    const showAvaliacao = () => {
        console.log(wasEvaluated);
        
        if(wasEvaluated) return (
            <h1 className="text-center mb-8 text-lg font-semibold">Esse relatório já foi avaliado</h1>
        )

        return (
            <form onSubmit={createParecer} className="space-y-6 px-24 my-16">
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Avaliação</h2>
                    <TextInput
                        label="Parecer *"
                        name="parecer"
                        value={formData.parecer}
                        onChange={handleChange}
                        required
                        type="text"
                    />
                    <SelectInput
                        label="Avalie o Desempenho *"
                        name="desempenhoRelatorio"
                        value={formData.desempenhoRelatorio}
                        onChange={handleChange}
                        options={[
                            { value: 'ADEQUADO', label: 'ADEQUADO' },
                            { value: 'ADEQUADO_COM_RESSALVAS', label: 'ADEQUADO COM RESSALVAS' },
                            { value: 'INADEQUADO', label: 'INADEQUADO' },
                        ]}
                        required
                    />
                </div>
                <div className="flex items-center justify-center">
                    <button
                    type="submit"
                    className="w-1/4 bg-blue-600 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-700"
                    >
                    Enviar
                    </button>
                </div>
            </form>
        );
    }

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
              const token = localStorage.getItem('token');
              const nusp = nuspNum;
          
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
                if(data.id === idNum) {
                    setRelatorio((prevRelatorio) => ({
                        ...prevRelatorio,
                        aluno: data.aluno,
                        id: data.id,
                        artigosAceitosOuPublicados: data.artigosAceitosOuPublicados,
                        artigosEmFaseEscrita: data.artigosEmFaseEscrita,
                        artigosSubmetidosEEmPeriodoAvaliacao: data.artigosSubmetidosEEmPeriodoAvaliacao,
                        declaracaoAdicionalParaCCP: data.declaracaoAdicionalParaCCP,
                        necessidadeApoioCoordenacao: data.necessidadeApoioCoordenacao,
                        pareceres: data.pareceres,
                        participacaoAtividadesAcademicas2024S1: data.participacaoAtividadesAcademicas2024S1,
                        prazoMaximoDepositoDissertacao: data.prazoMaximoDepositoDissertacao,
                        prazoMaximoInscricaoExameQualificacao: data.prazoMaximoInscricaoExameQualificacao,
                        quantidadeAprovacoesDesdeInicioCurso: data.quantidadeAprovacoesDesdeInicioCurso,
                        quantidadeReprovacoes2023S2: data.quantidadeReprovacoes2023S2,
                        quantidadeReprovacoesDesdeInicioCurso: data.quantidadeReprovacoesDesdeInicioCurso,
                        realizouExameProficienciaLinguas: data.realizouExameProficienciaLinguas,
                        realizouExameQualificacao: data.realizouExameQualificacao,
                        resultadoUltimoRelatorio: data.resultadoUltimoRelatorio,
                        resumoAtividadesPesquisa: data.resumoAtividadesPesquisa,
                    }));   
                    
                    data?.pareceres.map((parecer: any) => {
                        if(parecer.parecista?.numeroUsp.toString() === localStorage.getItem("nusp") && data.resultadoUltimoRelatorio !== "INADEQUADO"){
                            setWasEvaluated(true);
                        }
                    })
                    console.log(data);   
                }
              })
              
              
            } catch (error) {
              console.error('Error na requisição GET:', error);
              throw error;
            }
        };

        fetchData();
    }, [])

    const createParecer = async () => {
        try {
            const token = localStorage.getItem("token");

            if (!token) {
                throw new Error('Token não encontrado.');
            }

            const response = await axios.post(
                "http://localhost:8080/api/pareceres",
                {
                    relatorioId: idNum,
                    parecer: formData.parecer,
                    desempenhoRelatorio: formData.desempenhoRelatorio,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
        } catch (error) {
            console.error("Erro: ", error);
        } 
        alert("Parecer Enviado com Sucesso!");
        navigate("/homeProfessor")
    };

    return(
        <>
            <Card className="m-8 flex justify-around py-8">
                <div>
                    <h1 className="text-lg font-semibold">ID Relatório - {relatorio?.id}</h1>
                    <h1 className="text-lg font-semibold pt-2">Nome do Aluno</h1>
                    <h1>{relatorio?.aluno.nomeCompleto}</h1>
                    <h1 className="text-lg font-semibold pt-2">Número USP</h1>
                    <h1>{relatorio?.aluno.numeroUsp}</h1>
                    <h1 className="text-lg font-semibold pt-2">Curso do Aluno</h1>
                    <h1>{relatorio?.aluno.tipoCurso}</h1>
                    <h1 className="text-lg font-semibold pt-2">e-Mail do Aluno</h1>
                    <h1>{relatorio?.aluno.email}</h1>
                    <h1 className="text-lg font-semibold pt-2">Link Lattes</h1>
                    <h1>{relatorio?.aluno.linkLattes}</h1>
                    <h1 className="text-lg font-semibold pt-2">Data de Aprovação do Exame de Linguas</h1>
                    <h1>{new Date(relatorio?.aluno.dataAprovacaoExameProficienciaLinguas).toLocaleDateString('pt-br')}</h1>
                    <h1 className="text-lg font-semibold pt-2">Data de Aprovação do Exame de Qualificação</h1>
                    <h1>{new Date(relatorio?.aluno.dataAprovacaoExameQualificacao).toLocaleDateString('pt-br')}</h1>
                    <h1 className="text-lg font-semibold pt-2">Prazo de Entrega do Trabalho Final</h1>
                    <h1>{new Date(relatorio?.aluno.dataLimiteDepositoTrabalhoFinal).toLocaleDateString('pt-br')}</h1>
                    <h1 className="text-lg font-semibold pt-2">Quantidade de Artigos em Fase de Escrita</h1>
                    <h1>{relatorio?.artigosEmFaseEscrita.toLowerCase()}</h1>
                    <h1 className="text-lg font-semibold pt-2">Quantidade de Artigos Submetidos e em Avaliação</h1>
                    <h1>{relatorio?.artigosSubmetidosEEmPeriodoAvaliacao.toLowerCase()}</h1>
                    <h1 className="text-lg font-semibold pt-2">Quantidade de Artigos Aceitos ou Publicados</h1>
                    <h1>{relatorio?.artigosAceitosOuPublicados.toLowerCase()}</h1>
                    <h1 className="text-lg font-semibold pt-2">Informação Adicional para a CCP</h1>
                    <h1>{relatorio?.declaracaoAdicionalParaCCP}</h1>
                    <h1 className="text-lg font-semibold pt-2">Necessita de Apoio da Coordenação? </h1>
                    <h1>{relatorio?.necessidadeApoioCoordenacao ? 'Sim' : 'Não'}</h1>
                </div>
                <div>
                    <h1 className="text-lg font-semibold pt-2">Participação em Atividades Acadêmcias</h1>
                    <h1>{relatorio?.participacaoAtividadesAcademicas2024S1}</h1>
                    <h1 className="text-lg font-semibold pt-2">Prazo Máximo para Dissertação</h1>
                    <h1>{new Date(relatorio?.prazoMaximoDepositoDissertacao).toLocaleDateString('pt-br')}</h1>
                    <h1 className="text-lg font-semibold pt-2">Exame de Qualificação</h1>
                    <h1>{relatorio?.realizouExameQualificacao}</h1>
                    <h1 className="text-lg font-semibold pt-2">Prazo Máximo para Inscrição do Exame de Qualificação</h1>
                    <h1>{new Date(relatorio?.prazoMaximoInscricaoExameQualificacao).toLocaleDateString('pt-br')}</h1>
                    <h1 className="text-lg font-semibold pt-2">Reprovações 2023</h1>
                    <h1>{relatorio?.quantidadeReprovacoes2023S2.toLowerCase()}</h1>
                    <h1 className="text-lg font-semibold pt-2">Reprovações Totais</h1>
                    <h1>{relatorio?.quantidadeAprovacoesDesdeInicioCurso.toLowerCase()}</h1>
                    <h1 className="text-lg font-semibold pt-2">Exame de Proficiência de Línguas</h1>
                    <h1>{relatorio?.realizouExameProficienciaLinguas}</h1>
                    <h1 className="text-lg font-semibold pt-2">Resultado do Último Relatório</h1>
                    <h1>{relatorio?.resultadoUltimoRelatorio}</h1>                    
                    <h1 className="text-lg font-semibold pt-2">Resumo de Atividades de Pesquisa</h1>
                    <h1>{relatorio?.resumoAtividadesPesquisa}</h1>
                </div>
            </Card>
            {showAvaliacao()}
            {/* <form onSubmit={createParecer} className="space-y-6 px-24 my-16">
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Avaliação</h2>
                    <TextInput
                        label="Parecer *"
                        name="parecer"
                        value={formData.parecer}
                        onChange={handleChange}
                        required
                        type="text"
                    />
                    <SelectInput
                        label="Avalie o Desempenho *"
                        name="desempenhoRelatorio"
                        value={formData.desempenhoRelatorio}
                        onChange={handleChange}
                        options={[
                            { value: 'ADEQUADO', label: 'ADEQUADO' },
                            { value: 'ADEQUADO_COM_RESSALVAS', label: 'ADEQUADO COM RESSALVAS' },
                            { value: 'INADEQUADO', label: 'INADEQUADO' },
                        ]}
                        required
                    />
                </div>
                <div className="flex items-center justify-center">
                    <button
                    type="submit"
                    className="w-1/4 bg-blue-600 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-700"
                    >
                    Enviar
                    </button>
                </div>
            </form> */}
        </>
    )
}
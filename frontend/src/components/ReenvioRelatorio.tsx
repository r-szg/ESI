import { useNavigate, useParams } from "react-router-dom"
import { useEffect, useState } from "react";
import axios from "axios";
import TextInput from "./formsComponents/TextInput";
import SelectInput from "./formsComponents/SelectInput";
import TextAreaInput from "./formsComponents/TextAreaInput";
import NumberInput from "./formsComponents/NumberInput";
import DateInput from "./formsComponents/DateInput";

interface Relatorio {
    numeroUspParecista: string;
    dataUltimaAtualizacaoLattes: string;
    tipoCurso: string;
    mesAnoIngressoComoAlunoRegular: string;
    resultadoUltimoRelatorio: string;
    quantidadeAprovacoesDesdeInicioCurso: string;
    quantidadeReprovacoes2023S2: string;
    quantidadeReprovacoesDesdeInicioCurso: string;
    realizouExameProficienciaLinguas: string;
    realizouExameQualificacao: string;
    prazoMaximoInscricaoExameQualificacao: string;
    prazoMaximoDepositoDissertacao: string;
    artigosEmFaseEscrita: string;
    artigosSubmetidosEEmPeriodoAvaliacao: string;
    artigosAceitosOuPublicados: string;
    participacaoAtividadesAcademicas2024S1: string;
    resumoAtividadesPesquisa: string;
    declaracaoAdicionalParaCCP: string;
    necessidadeApoioCoordenacao: string;
}


export default function ReenvioRelatorio(){
    const navigate = useNavigate();

    const {nusp} = useParams <{ nusp:string }>();
    const nuspNum = Number(nusp);
    const {idRelatorio} = useParams <{ idRelatorio:string }>();
    const idRelatorioNum = Number(idRelatorio);

    const [relatorio, setRelatorio] = useState<Relatorio>({
        numeroUspParecista: '',
        dataUltimaAtualizacaoLattes: '',
        tipoCurso: '',
        mesAnoIngressoComoAlunoRegular: '',
        resultadoUltimoRelatorio: '',
        quantidadeAprovacoesDesdeInicioCurso: '',
        quantidadeReprovacoes2023S2: '',
        quantidadeReprovacoesDesdeInicioCurso: '',
        realizouExameProficienciaLinguas: '',
        realizouExameQualificacao: '',
        prazoMaximoInscricaoExameQualificacao: '',
        prazoMaximoDepositoDissertacao: '',
        artigosEmFaseEscrita: '',
        artigosSubmetidosEEmPeriodoAvaliacao: '',
        artigosAceitosOuPublicados: '',
        participacaoAtividadesAcademicas2024S1: '',
        resumoAtividadesPesquisa: '',
        declaracaoAdicionalParaCCP: '',
        necessidadeApoioCoordenacao: '',
    });

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;
        setRelatorio({ ...relatorio, [name]: value });        
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
                if(data.id === idRelatorioNum) {
                    console.log(data);
                    setRelatorio((prevRelatorio) => ({
                        ...prevRelatorio,
                        numeroUspParecista: data.docente.numeroUsp,
                        dataUltimaAtualizacaoLattes: data.dataUltimaAtualizacaoLattes,
                        tipoCurso: data.tipoCurso,
                        mesAnoIngressoComoAlunoRegular: data.mesAnoIngressoComoAlunoRegular,
                        resultadoUltimoRelatorio: data.resultadoUltimoRelatorio,
                        quantidadeAprovacoesDesdeInicioCurso: data.quantidadeAprovacoesDesdeInicioCurso,
                        quantidadeReprovacoes2023S2: data.quantidadeReprovacoes2023S2,
                        quantidadeReprovacoesDesdeInicioCurso: data.quantidadeReprovacoesDesdeInicioCurso,
                        realizouExameProficienciaLinguas: data.realizouExameProficienciaLinguas,
                        realizouExameQualificacao: data.realizouExameQualificacao,
                        prazoMaximoInscricaoExameQualificacao: data.prazoMaximoInscricaoExameQualificacao,
                        prazoMaximoDepositoDissertacao: data.prazoMaximoDepositoDissertacao,
                        artigosEmFaseEscrita: data.artigosEmFaseEscrita,
                        artigosSubmetidosEEmPeriodoAvaliacao: data.artigosSubmetidosEEmPeriodoAvaliacao,
                        artigosAceitosOuPublicados: data.artigosAceitosOuPublicados,
                        participacaoAtividadesAcademicas2024S1: data.participacaoAtividadesAcademicas2024S1,
                        resumoAtividadesPesquisa: data.resumoAtividadesPesquisa,
                        declaracaoAdicionalParaCCP: data.declaracaoAdicionalParaCCP,
                        necessidadeApoioCoordenacao: data.necessidadeApoioCoordenacao,
                    }));                    
                }
              })
              
              
            } catch (error) {
              console.error('Error na requisição GET:', error);
              throw error;
            }
        };

        fetchData();
    }, [])




    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const nusp = localStorage.getItem("nusp");
            
            if(!token) {
                throw new Error('Token não encontrado.');
          }
          if(!nusp) {
              throw new Error('Número USP não encontrado.');
            }
            
            
            const response = await axios.put(
            `http://localhost:8080/api/relatorios/${nusp}/${idRelatorioNum}`,
            {
                numeroUspParecista: relatorio.numeroUspParecista,
                dataUltimaAtualizacaoLattes: relatorio.dataUltimaAtualizacaoLattes,
                tipoCurso: relatorio.tipoCurso,
                mesAnoIngressoComoAlunoRegular: relatorio.mesAnoIngressoComoAlunoRegular,
                resultadoUltimoRelatorio: relatorio.resultadoUltimoRelatorio,
                quantidadeAprovacoesDesdeInicioCurso: relatorio.quantidadeAprovacoesDesdeInicioCurso,
                quantidadeReprovacoes2023S2: relatorio.quantidadeReprovacoes2023S2,
                quantidadeReprovacoesDesdeInicioCurso: relatorio.quantidadeReprovacoesDesdeInicioCurso,
                realizouExameProficienciaLinguas: relatorio.realizouExameProficienciaLinguas,
                realizouExameQualificacao: relatorio.realizouExameQualificacao,
                prazoMaximoInscricaoExameQualificacao: relatorio.prazoMaximoInscricaoExameQualificacao,
                prazoMaximoDepositoDissertacao: relatorio.prazoMaximoDepositoDissertacao,
                artigosEmFaseEscrita: relatorio.artigosEmFaseEscrita,
                artigosSubmetidosEEmPeriodoAvaliacao: relatorio.artigosSubmetidosEEmPeriodoAvaliacao,
                artigosAceitosOuPublicados: relatorio.artigosAceitosOuPublicados,
                participacaoAtividadesAcademicas2024S1: relatorio.participacaoAtividadesAcademicas2024S1,
                resumoAtividadesPesquisa: relatorio.resumoAtividadesPesquisa,
                declaracaoAdicionalParaCCP: relatorio.declaracaoAdicionalParaCCP,
                necessidadeApoioCoordenacao: relatorio.necessidadeApoioCoordenacao,
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
      
        alert("Reenvio Efetuado com Sucesso");
        console.log('Form data submitted:', relatorio);
        navigate("/homeAluno");
      };

    return(
        <>
            <form onSubmit={handleSubmit} className="space-y-6 p-16">
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Dados Pessoais</h2>
                    <NumberInput
                        label="Número USP do(a) Orientador(a)"
                        name="numeroUspParecista"
                        value={relatorio.numeroUspParecista}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Informações Acadêmicas</h2>
                    <DateInput
                        label="Data da última atualização do Lattes *"
                        name="dataUltimaAtualizacaoLattes"
                        value={relatorio.dataUltimaAtualizacaoLattes}
                        onChange={handleChange}
                        required
                    />
                    <SelectInput
                        label="Seu Curso *"
                        name="tipoCurso"
                        value={relatorio.tipoCurso}
                        onChange={handleChange}
                        options={[
                            { value: 'MESTRADO', label: 'MESTRADO' },
                            { value: 'DOUTORADO', label: 'DOUTORADO' },
                        ]}
                        required
                    />
                    <TextInput
                        label="Informe o mês e o ano de seu ingresso como aluno(a) regular (exemplo: março/2023) *"
                        name="mesAnoIngressoComoAlunoRegular"
                        value={relatorio.mesAnoIngressoComoAlunoRegular}
                        onChange={handleChange}
                        required
                        placeholder="ex: março/2023"
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Avaliação e Disciplinas</h2>
                    <SelectInput
                        label="Qual foi o resultado da avaliação do seu último relatório? *"
                        name="resultadoUltimoRelatorio"
                        value={relatorio.resultadoUltimoRelatorio}
                        onChange={handleChange}
                        options={[
                            { value: 'ADEQUADO', label: 'ADEQUADO' },
                            { value: 'ADEQUADO_COM_RESSALVAS', label: 'ADEQUADO COM RESSALVAS' },
                            { value: 'INADEQUADO', label: 'INADEQUADO' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Aprovações em disciplinas desde o início do curso"
                        name="quantidadeAprovacoesDesdeInicioCurso"
                        value={relatorio.quantidadeAprovacoesDesdeInicioCurso}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3' },
                            { value: '4', label: '4' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Reprovações no 2º semestre de 2023"
                        name="quantidadeReprovacoes2023S2"
                        value={relatorio.quantidadeReprovacoes2023S2}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3' },
                            { value: '4', label: '4' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Reprovações em disciplinas desde o início do curso"
                        name="quantidadeReprovacoesDesdeInicioCurso"
                        value={relatorio.quantidadeReprovacoesDesdeInicioCurso}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3' },
                            { value: '4', label: '4' },
                        ]}
                        required
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Referente aos Exames do Programa *</h2>
                    <SelectInput
                        label="Já realizou exame de proficiência em idiomas?"
                        name="realizouExameProficienciaLinguas"
                        value={relatorio.realizouExameProficienciaLinguas}
                        onChange={handleChange}
                        options={[
                            { value: 'APROVADO', label: 'Sim, fui aprovado.' },
                            { value: 'REPROVADO', label: 'Sim, fui reprovado.' },
                            { value: 'NÃO', label: 'Não.' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Já realizou exame de qualificação?"
                        name="realizouExameQualificacao"
                        value={relatorio.realizouExameQualificacao}
                        onChange={handleChange}
                        options={[
                            { value: 'APROVADO', label: 'Sim, fui aprovado.' },
                            { value: 'REPROVADO', label: 'Sim, fui reprovado.' },
                            { value: 'NÃO', label: 'Não.' },
                        ]}
                        required
                    />
                    <TextInput
                        label="Se você ainda não qualificou, qual é o seu prazo máximo para inscrição no exame de qualificação? (exemplo: novembro/2024)"
                        name="prazoMaximoInscricaoExameQualificacao"
                        value={relatorio.prazoMaximoInscricaoExameQualificacao}
                        onChange={handleChange}
                        placeholder="ex: novembro/2024"
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Referente à Produção de Artigos Durante o Curso *</h2>
                    <SelectInput
                        label="Artigos em fase de escrita"
                        name="artigosEmFaseEscrita"
                        value={relatorio.artigosEmFaseEscrita}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3 ou mais' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Artigos submetidos e em período de avaliação"
                        name="artigosSubmetidosEEmPeriodoAvaliacao"
                        value={relatorio.artigosSubmetidosEEmPeriodoAvaliacao}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3 ou mais' },
                        ]}
                        required
                    />
                    <SelectInput
                        label="Artigos aceitos ou publicados"
                        name="artigosAceitosOuPublicados"
                        value={relatorio.artigosAceitosOuPublicados}
                        onChange={handleChange}
                        options={[
                            { value: '0', label: '0' },
                            { value: '1', label: '1' },
                            { value: '2', label: '2' },
                            { value: '3', label: '3 ou mais' },
                        ]}
                        required
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Atividades e Pesquisa</h2>
                    <TextAreaInput
                        label="Apresente um resumo das suas atividades de pesquisa até o momento e das atividades que ainda precisam ser desenvolvidas até a conclusão do seu curso."
                        name="resumoAtividadesPesquisa"
                        value={relatorio.resumoAtividadesPesquisa}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="border-b-2 pb-4">
                    <h2 className="text-lg font-semibold mb-2">Informações Adicionais</h2>
                    <TextAreaInput
                        label="Você tem algo adicional a declarar para a CCP - PPgSI que considera importante para sua avaliação?"
                        name="declaracaoAdicionalParaCCP"
                        value={relatorio.declaracaoAdicionalParaCCP}
                        onChange={handleChange}
                    />
                    <SelectInput
                        label="Está enfrentando alguma dificuldade que precisa de apoio da coordenação do curso? *"
                        name="necessidadeApoioCoordenacao"
                        value={relatorio.necessidadeApoioCoordenacao}
                        onChange={handleChange}
                        placeholder={relatorio.necessidadeApoioCoordenacao}
                        options={[
                            { value: 'true', label: 'Sim' },
                            { value: 'false', label: 'Não' },
                        ]}
                        required
                    />
                </div>

                <div className="flex justify-center">
                    <button
                        type="submit"
                        className="w-1/4 bg-blue-600 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-700"
                    >
                        Enviar
                    </button>
                </div>
            </form>
        </>
    )
}
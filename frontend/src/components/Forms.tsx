import React, { useState } from 'react';
import TextInput from './formsComponents/TextInput';
import SelectInput from './formsComponents/SelectInput';
import TextAreaInput from './formsComponents/TextAreaInput';
import NumberInput from './formsComponents/NumberInput';
import DateInput from './formsComponents/DateInput';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

interface FormData {
  numeroUspParecista: string;
  email: string;
  studentName: string;
  uspNumber: string;
  lattesLink: string;
  lattesUpdateDate: string;
  courseType: string;
  entryDate: string;
  lastReportResult: string;
  approvals: string;
  failures2023: string;
  totalFailures: string;
  languageExam: string;
  qualificationExam: string;
  qualificationDeadline: string;
  dissertationDeadline: string;
  articlesWriting: string;
  articlesSubmitted: string;
  articlesPublished: string;
  academicActivities: string;
  researchSummary: string;
  additionalInfo: string;
  requiresSupport: string;
}

const GoogleStyleForm: React.FC = () => {
  const navigate = useNavigate()
  const [formData, setFormData] = useState<FormData>({
    numeroUspParecista: '',
    email: '',
    studentName: '',
    uspNumber: '',
    lattesLink: '',
    lattesUpdateDate: '',
    courseType: '',
    entryDate: '',
    lastReportResult: '',
    approvals: '0',
    failures2023: '0',
    totalFailures: '0',
    languageExam: '',
    qualificationExam: '',
    qualificationDeadline: '',
    dissertationDeadline: '',
    articlesWriting: '0',
    articlesSubmitted: '0',
    articlesPublished: '0',
    academicActivities: '',
    researchSummary: '',
    additionalInfo: '',
    requiresSupport: '',
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Form data submitted:', formData);

    // const createReport = async () => {
    try {
      const token = localStorage.getItem("token");

      if(!token) {
        throw new Error('Token não encontrado.');
      }

      const data = {
        numeroUspParecista: formData.numeroUspParecista,
        dataUltimaAtualizacaoLattes: formData.lattesUpdateDate,
        tipoCurso: formData.courseType.toUpperCase, 
        mesAnoIngressoComoAlunoRegular: formData.entryDate, 
        resultadoUltimoRelatorio: formData.lastReportResult, 
        quantidadeAprovacoesDesdeInicioCurso: formData.approvals,
        quantidadeReprovacoes2023S2: formData.failures2023,
        quantidadeReprovacoesDesdeInicioCurso: formData.totalFailures,
        realizouExameProficienciaLinguas: formData.languageExam, 
        realizouExameQualificacao: formData.qualificationExam,
        prazoMaximoInscricaoExameQualificacao: formData.qualificationDeadline,
        prazoMaximoDepositoDissertacao: formData.dissertationDeadline,
        artigosEmFaseEscrita: formData.articlesWriting,
        artigosSubmetidosEEmPeriodoAvaliacao: formData.articlesSubmitted,
        artigosAceitosOuPublicados: formData.articlesPublished,
        participacaoAtividadesAcademicas2024S1: formData.academicActivities,
        resumoAtividadesPesquisa: formData.researchSummary,
        declaracaoAdicionalParaCCP: formData.additionalInfo,
        necessidadeApoioCoordenacao: formData.requiresSupport,
      }
      console.log(data);
      
      // const tf = parseInt(formData.numeroUspParecista)
      

      const response = await axios.post(
        "http://localhost:8080/api/relatorios",
        {
          numeroUspParecista: parseInt(formData.numeroUspParecista),
          dataUltimaAtualizacaoLattes: formData.lattesUpdateDate,
          tipoCurso: formData.courseType, 
          mesAnoIngressoComoAlunoRegular: formData.entryDate, 
          resultadoUltimoRelatorio: formData.lastReportResult, 
          quantidadeAprovacoesDesdeInicioCurso: formData.approvals,
          quantidadeReprovacoes2023S2: formData.failures2023,
          quantidadeReprovacoesDesdeInicioCurso: formData.totalFailures,
          realizouExameProficienciaLinguas: formData.languageExam, 
          realizouExameQualificacao: formData.qualificationExam,
          prazoMaximoInscricaoExameQualificacao: formData.qualificationDeadline,
          prazoMaximoDepositoDissertacao: formData.dissertationDeadline,
          artigosEmFaseEscrita: formData.articlesWriting,
          artigosSubmetidosEEmPeriodoAvaliacao: formData.articlesSubmitted,
          artigosAceitosOuPublicados: formData.articlesPublished,
          participacaoAtividadesAcademicas2024S1: formData.academicActivities,
          resumoAtividadesPesquisa: formData.researchSummary,
          declaracaoAdicionalParaCCP: formData.additionalInfo,
          necessidadeApoioCoordenacao: formData.requiresSupport,
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
  
    alert("Relatório Enviado com Sucesso!")
    navigate("/homeAluno")
  };









  return (
    <div className="max-w-3xl mx-auto p-8 bg-gray-100">
      <div className="p-8 bg-white shadow-xl rounded-lg">
        <h1 className="text-2xl font-bold mb-6 text-center">Relatório Semestral de Aluno Regular</h1>
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Seção de Contato */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Contato</h2>
            <TextInput
              label="Email *"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              type="email"
            />
          </div>

          {/* Seção de Dados Pessoais */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Dados Pessoais</h2>
            <TextInput
              label="Nome do(a) aluno(a) *"
              name="studentName"
              value={formData.studentName}
              onChange={handleChange}
              required
            />
            <NumberInput
              label="Número USP *"
              name="uspNumber"
              value={formData.uspNumber}
              onChange={handleChange}
              required
            />
            <NumberInput
              label="Número USP do(a) Orientador(a)"
              name="numeroUspParecista"
              value={formData.numeroUspParecista}
              onChange={handleChange}
              required
            />
          </div>

          {/* Seção de Informações Acadêmicas */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Informações Acadêmicas</h2>
            <TextInput
              label="Link para o Lattes *"
              name="lattesLink"
              value={formData.lattesLink}
              onChange={handleChange}
              required
              type="url"
            />
            <DateInput
              label="Data da última atualização do Lattes *"
              name="lattesUpdateDate"
              value={formData.lattesUpdateDate}
              onChange={handleChange}
              required
            />
            <SelectInput
              label="Qual é o seu curso? *"
              name="courseType"
              value={formData.courseType}
              onChange={handleChange}
              options={[
                { value: 'MESTRADO', label: 'MESTRADO' },
                { value: 'DOUTORADO', label: 'DOUTORADO' },
              ]}
              required
            />
            <DateInput
              label="Informe a data de seu ingresso como aluno(a) regular *"
              name="entryDate"
              value={formData.entryDate}
              onChange={handleChange}
              required
            />
          </div>

          {/* Seção de Avaliação e Disciplinas */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Avaliação e Disciplinas</h2>
            <SelectInput
              label="Qual foi o resultado da avaliação do seu último relatório? *"
              name="lastReportResult"
              value={formData.lastReportResult}
              onChange={handleChange}
              options={[
                { value: 'ADEQUADO', label: 'ADEQUADO' },
                { value: 'ADEQUADO_COM_RESSALVAS', label: 'ADEQUADO COM RESSALVAS' },
                { value: 'INADEQUADO', label: 'INADEQUADO' },
                { value: 'NAO_SE_APLICA', label: 'Não se aplica (é meu primeiro relatório)' },
              ]}
              required
            />
            <SelectInput
              label="Aprovações em disciplinas desde o início do curso"
              name="approvals"
              value={formData.approvals}
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
              name="failures2023"
              value={formData.failures2023}
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
              name="totalFailures"
              value={formData.totalFailures}
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

          {/* Seção de Exames */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Referente aos Exames do Programa *</h2>
            <SelectInput
              label="Já realizou exame de proficiência em idiomas?"
              name="languageExam"
              value={formData.languageExam}
              onChange={handleChange}
              options={[
                { value: 'APROVADO', label: 'Sim, fui aprovado.' },
                { value: 'REPROVADO', label: 'Sim, fui reprovado.' },
                { value: 'NAO_REALIZADO', label: 'Não.' },
              ]}
              required
            />
            <SelectInput
              label="Já realizou exame de qualificação?"
              name="qualificationExam"
              value={formData.qualificationExam}
              onChange={handleChange}
              options={[
                { value: 'APROVADO', label: 'Sim, fui aprovado.' },
                { value: 'REPROVADO', label: 'Sim, fui reprovado.' },
                { value: 'NAO_REALIZADO', label: 'Não.' },
              ]}
              required
            />
            <DateInput
              label="Se você ainda não qualificou, qual é o seu prazo máximo para inscrição no exame de qualificação?"
              name="qualificationDeadline"
              value={formData.qualificationDeadline}
              onChange={handleChange}
              required
            />
            <DateInput
              label="Prazo máximo da dissertação"
              name="dissertationDeadline"
              value={formData.dissertationDeadline}
              onChange={handleChange}
              required
            />
          </div>

          {/* Seção de Produção Acadêmica */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Referente à Produção de Artigos Durante o Curso *</h2>
            <SelectInput
              label="Artigos em fase de escrita"
              name="articlesWriting"
              value={formData.articlesWriting}
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
              name="articlesSubmitted"
              value={formData.articlesSubmitted}
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
              name="articlesPublished"
              value={formData.articlesPublished}
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

          {/* Seção de Atividades e Pesquisa */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Atividades e Pesquisa</h2>
            <TextAreaInput
              label="Relate aqui as atividades ou eventos acadêmicos que você participou no 1o semestre de 2024 *"
              name="academicActivities"
              value={formData.academicActivities}
              onChange={handleChange}
              required
            />
            <TextAreaInput
              label="Apresente um resumo das suas atividades de pesquisa até o momento e das atividades que ainda precisam ser desenvolvidas até a conclusão do seu curso."
              name="researchSummary"
              value={formData.researchSummary}
              onChange={handleChange}
              required
            />
          </div>

          {/* Seção de Informações Adicionais */}
          <div className="border-b-2 pb-4">
            <h2 className="text-lg font-semibold mb-2">Informações Adicionais</h2>
            <TextAreaInput
              label="Você tem algo adicional a declarar para a CCP - PPgSI que considera importante para sua avaliação?"
              name="additionalInfo"
              value={formData.additionalInfo}
              onChange={handleChange}
            />
            <SelectInput
              label="Está enfrentando alguma dificuldade que precisa de apoio da coordenação do curso? *"
              name="requiresSupport"
              value={formData.requiresSupport}
              onChange={handleChange}
              options={[
                { value: 'true', label: 'Sim' },
                { value: 'false', label: 'Não' },
              ]}
              required
            />
          </div>

          <div>
            <button
              type="submit"
              className="w-full bg-blue-600 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-700"
            >
              Enviar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default GoogleStyleForm;

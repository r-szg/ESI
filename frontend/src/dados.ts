export interface Aluno{
    id: number;
    nome: string;
    nusp: number;
    entrega: string;
    projeto: string;
    prazo: string;
    status: string;
    resultado: string;
    docente: string;
}

export const alunos:Aluno[] = [
    {
        id: 1,
        nome: "Danilo Romeira",
        nusp: 13725823,
        entrega: "Primeira",
        projeto: "Trabalho ESI",
        prazo: "11/10/2024",
        status: "Entregue",
        resultado: "Satisfatório",
        docente: "Eler"
    },
    {
        id: 2,
        nome: "Felipe Holanda",
        nusp: 11523465,
        entrega: "Segunda",
        projeto: "Trabalho RP2",
        prazo: "30/10/2024",
        status: "Entregue",
        resultado: "Insatisfatório",
        docente: "Eler"
    }
]
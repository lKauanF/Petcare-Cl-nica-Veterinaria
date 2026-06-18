export type Especie = 'CAO' | 'GATO' | 'OUTRO';
export type StatusConsulta = 'AGENDADA' | 'REALIZADA' | 'CANCELADA';

export interface TutorRequest {
  nome: string;
  telefone: string;
  email: string;
  endereco: string;
}

export interface TutorResponse extends TutorRequest {
  id: number;
  totalAnimais: number;
}

export interface AnimalRequest {
  nome: string;
  especie: Especie;
  raca: string;
  dataNascimento: string;
  tutorId: number;
}

export interface AnimalResponse extends AnimalRequest {
  id: number;
  tutorNome: string;
}

export interface ConsultaAgendarRequest {
  animalId: number;
  dataHoraAgendada: string;
  motivo: string;
  valor: number;
}

export interface ConsultaRealizarRequest {
  diagnostico: string;
}

export interface ConsultaResponse {
  id: number;
  animalId: number;
  animalNome: string;
  tutorNome: string;
  dataHoraAgendada: string;
  dataHoraRealizada: string | null;
  motivo: string;
  diagnostico: string | null;
  status: StatusConsulta;
  valor: number;
}

export interface DashboardResponse {
  totalAnimais: number;
  totalTutores: number;
  totalConsultasRealizadas: number;
  totalConsultasAgendadas: number;
  proximasConsultas: ConsultaResponse[];
}

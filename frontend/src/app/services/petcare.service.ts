import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AnimalRequest,
  AnimalResponse,
  ConsultaAgendarRequest,
  ConsultaRealizarRequest,
  ConsultaResponse,
  DashboardResponse,
  Especie,
  StatusConsulta,
  TutorRequest,
  TutorResponse
} from '../models/petcare.models';

@Injectable({ providedIn: 'root' })
export class PetcareService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private readonly http: HttpClient) {}

  listarTutores(): Observable<TutorResponse[]> {
    return this.http.get<TutorResponse[]>(`${this.apiUrl}/tutores`);
  }

  criarTutor(request: TutorRequest): Observable<TutorResponse> {
    return this.http.post<TutorResponse>(`${this.apiUrl}/tutores`, request);
  }

  excluirTutor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/tutores/${id}`);
  }

  listarAnimais(especie?: Especie | '', tutorId?: number | null, busca?: string): Observable<AnimalResponse[]> {
    let params = new HttpParams();

    if (especie) {
      params = params.set('especie', especie);
    }

    if (tutorId) {
      params = params.set('tutorId', tutorId);
    }

    if (busca?.trim()) {
      params = params.set('busca', busca.trim());
    }

    return this.http.get<AnimalResponse[]>(`${this.apiUrl}/animais`, { params });
  }

  criarAnimal(request: AnimalRequest): Observable<AnimalResponse> {
    return this.http.post<AnimalResponse>(`${this.apiUrl}/animais`, request);
  }

  excluirAnimal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/animais/${id}`);
  }

  listarConsultas(status?: StatusConsulta | '', animalId?: number | null): Observable<ConsultaResponse[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    if (animalId) {
      params = params.set('animalId', animalId);
    }

    return this.http.get<ConsultaResponse[]>(`${this.apiUrl}/consultas`, { params });
  }

  agendarConsulta(request: ConsultaAgendarRequest): Observable<ConsultaResponse> {
    return this.http.post<ConsultaResponse>(`${this.apiUrl}/consultas/agendar`, request);
  }

  realizarConsulta(id: number, request: ConsultaRealizarRequest): Observable<ConsultaResponse> {
    return this.http.put<ConsultaResponse>(`${this.apiUrl}/consultas/${id}/realizar`, request);
  }

  cancelarConsulta(id: number): Observable<ConsultaResponse> {
    return this.http.put<ConsultaResponse>(`${this.apiUrl}/consultas/${id}/cancelar`, {});
  }

  buscarDashboard(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/dashboard`);
  }
}

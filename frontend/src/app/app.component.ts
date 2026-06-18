import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  AnimalResponse,
  ConsultaResponse,
  DashboardResponse,
  Especie,
  StatusConsulta,
  TutorResponse
} from './models/petcare.models';
import { PetcareService } from './services/petcare.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  readonly especies: Especie[] = ['CAO', 'GATO', 'OUTRO'];
  readonly statusConsultas: StatusConsulta[] = ['AGENDADA', 'REALIZADA', 'CANCELADA'];

  tutores: TutorResponse[] = [];
  animais: AnimalResponse[] = [];
  consultas: ConsultaResponse[] = [];
  dashboard: DashboardResponse | null = null;

  loading = false;
  mensagem = '';
  erro = '';

  tutorForm: FormGroup = this.formBuilder.group({
    nome: ['', Validators.required],
    telefone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    endereco: ['', Validators.required]
  });

  animalForm: FormGroup = this.formBuilder.group({
    nome: ['', Validators.required],
    especie: ['CAO', Validators.required],
    raca: ['', Validators.required],
    dataNascimento: ['', Validators.required],
    tutorId: [null, Validators.required]
  });

  consultaForm: FormGroup = this.formBuilder.group({
    animalId: [null, Validators.required],
    dataHoraAgendada: ['', Validators.required],
    motivo: ['', Validators.required],
    valor: [0, [Validators.required, Validators.min(0)]]
  });

  filtroAnimalForm: FormGroup = this.formBuilder.group({
    especie: [''],
    tutorId: [''],
    busca: ['']
  });

  filtroConsultaForm: FormGroup = this.formBuilder.group({
    status: [''],
    animalId: ['']
  });

  realizarForm: FormGroup = this.formBuilder.group({
    diagnostico: ['', Validators.required]
  });

  consultaSelecionada: ConsultaResponse | null = null;

  constructor(
    private readonly petcareService: PetcareService,
    private readonly formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.carregarTudo();
  }

  carregarTudo(): void {
    this.loading = true;
    this.limparMensagens();

    this.petcareService.listarTutores().subscribe({
      next: (tutores: TutorResponse[]) => this.tutores = tutores,
      error: (error: unknown) => this.tratarErro(error)
    });

    this.listarAnimais(false);
    this.listarConsultas(false);
    this.buscarDashboard(false);

    setTimeout(() => this.loading = false, 400);
  }

  criarTutor(): void {
    if (this.tutorForm.invalid) {
      this.erro = 'Preencha todos os dados do tutor corretamente.';
      return;
    }

    this.executarAcao(() => {
      this.petcareService.criarTutor(this.tutorForm.getRawValue()).subscribe({
        next: () => {
          this.tutorForm.reset();
          this.mensagem = 'Tutor cadastrado com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  excluirTutor(id: number): void {
    this.executarAcao(() => {
      this.petcareService.excluirTutor(id).subscribe({
        next: () => {
          this.mensagem = 'Tutor excluído com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  criarAnimal(): void {
    if (this.animalForm.invalid) {
      this.erro = 'Preencha todos os dados do animal corretamente.';
      return;
    }

    this.executarAcao(() => {
      this.petcareService.criarAnimal(this.animalForm.getRawValue()).subscribe({
        next: () => {
          this.animalForm.reset({ especie: 'CAO' });
          this.mensagem = 'Animal cadastrado com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  excluirAnimal(id: number): void {
    this.executarAcao(() => {
      this.petcareService.excluirAnimal(id).subscribe({
        next: () => {
          this.mensagem = 'Animal excluído com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  listarAnimais(mostrarMensagem = true): void {
    const filtros = this.filtroAnimalForm.getRawValue();

    this.petcareService.listarAnimais(
      filtros.especie,
      filtros.tutorId ? Number(filtros.tutorId) : null,
      filtros.busca
    ).subscribe({
      next: (animais: AnimalResponse[]) => {
        this.animais = animais;
        if (mostrarMensagem) {
          this.mensagem = 'Filtro de animais aplicado.';
        }
      },
      error: (error: unknown) => this.tratarErro(error)
    });
  }

  agendarConsulta(): void {
    if (this.consultaForm.invalid) {
      this.erro = 'Preencha todos os dados da consulta corretamente.';
      return;
    }

    const formValue = this.consultaForm.getRawValue();
    const request = {
      ...formValue,
      dataHoraAgendada: this.converterDataHora(formValue.dataHoraAgendada),
      animalId: Number(formValue.animalId),
      valor: Number(formValue.valor)
    };

    this.executarAcao(() => {
      this.petcareService.agendarConsulta(request).subscribe({
        next: () => {
          this.consultaForm.reset({ valor: 0 });
          this.mensagem = 'Consulta agendada com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  listarConsultas(mostrarMensagem = true): void {
    const filtros = this.filtroConsultaForm.getRawValue();

    this.petcareService.listarConsultas(
      filtros.status,
      filtros.animalId ? Number(filtros.animalId) : null
    ).subscribe({
      next: (consultas: ConsultaResponse[]) => {
        this.consultas = consultas;
        if (mostrarMensagem) {
          this.mensagem = 'Filtro de consultas aplicado.';
        }
      },
      error: (error: unknown) => this.tratarErro(error)
    });
  }

  abrirRealizacao(consulta: ConsultaResponse): void {
    this.consultaSelecionada = consulta;
    this.realizarForm.reset();
  }

  realizarConsulta(): void {
    if (!this.consultaSelecionada || this.realizarForm.invalid) {
      this.erro = 'Informe o diagnóstico para realizar a consulta.';
      return;
    }

    this.executarAcao(() => {
      this.petcareService.realizarConsulta(this.consultaSelecionada!.id, this.realizarForm.getRawValue()).subscribe({
        next: () => {
          this.consultaSelecionada = null;
          this.realizarForm.reset();
          this.mensagem = 'Consulta realizada com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  cancelarConsulta(id: number): void {
    this.executarAcao(() => {
      this.petcareService.cancelarConsulta(id).subscribe({
        next: () => {
          this.mensagem = 'Consulta cancelada com sucesso.';
          this.recarregarDados();
        },
        error: (error: unknown) => this.tratarErro(error)
      });
    });
  }

  buscarDashboard(mostrarErro = true): void {
    this.petcareService.buscarDashboard().subscribe({
      next: (dashboard: DashboardResponse) => this.dashboard = dashboard,
      error: (error: unknown) => {
        if (mostrarErro) {
          this.tratarErro(error);
        }
      }
    });
  }

  formatarDataHora(valor: string | null): string {
    if (!valor) {
      return '-';
    }

    return new Date(valor).toLocaleString('pt-BR');
  }

  consultaFinalizada(consulta: ConsultaResponse): boolean {
    return consulta.status === 'REALIZADA' || consulta.status === 'CANCELADA';
  }

  private recarregarDados(): void {
    this.petcareService.listarTutores().subscribe((tutores: TutorResponse[]) => this.tutores = tutores);
    this.listarAnimais(false);
    this.listarConsultas(false);
    this.buscarDashboard(false);
  }

  private executarAcao(callback: () => void): void {
    this.loading = true;
    this.limparMensagens();
    callback();
    this.petcareService.buscarDashboard()
      .pipe(finalize(() => this.loading = false))
      .subscribe({ next: (dashboard: DashboardResponse) => this.dashboard = dashboard });
  }

  private converterDataHora(valor: string): string {
    return valor.length === 16 ? `${valor}:00` : valor;
  }

  private tratarErro(error: unknown): void {
    const possibleError = error as { error?: { message?: string } };
    this.erro = possibleError.error?.message ?? 'Ocorreu um erro ao executar a operação.';
    this.loading = false;
  }

  private limparMensagens(): void {
    this.mensagem = '';
    this.erro = '';
  }
}

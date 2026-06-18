INSERT INTO tutores (id, nome, telefone, email, endereco) VALUES
(1, 'Maria Silva', '62999990000', 'maria@email.com', 'Rua A, 123'),
(2, 'Joao Pereira', '62888880000', 'joao@email.com', 'Rua B, 456');

INSERT INTO animais (id, nome, especie, raca, data_nascimento, tutor_id) VALUES
(1, 'Rex', 'CAO', 'Vira-lata', '2020-05-10', 1),
(2, 'Mimi', 'GATO', 'Siames', '2021-03-15', 2),
(3, 'Thor', 'CAO', 'Labrador', '2019-08-20', 1);

INSERT INTO consultas (id, animal_id, data_hora_agendada, data_hora_realizada, motivo, diagnostico, status, valor) VALUES
(1, 1, '2026-06-20 14:30:00', NULL, 'Consulta de rotina', NULL, 'AGENDADA', 120.00),
(2, 2, '2026-06-21 09:00:00', NULL, 'Vacina anual', NULL, 'AGENDADA', 90.00),
(3, 3, '2026-06-10 10:00:00', '2026-06-10 10:35:00', 'Retorno', 'Animal recuperado.', 'REALIZADA', 100.00);

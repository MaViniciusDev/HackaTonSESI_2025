// js/dashboard.js
import apiService from '../services/api';

// --- ELEMENTOS DO DOM ---
const userNameElement = document.getElementById('userName');
const calendarBody = document.getElementById('calendarBody');
const calendarMonthYear = document.getElementById('calendarMonthYear');
const nextAppointmentContent = document.getElementById('nextAppointmentContent');
const nextAppointmentDate = document.getElementById('nextAppointmentDate');
const nextAppointmentTime = document.getElementById('nextAppointmentTime');
const nextAppointmentType = document.getElementById('nextAppointmentType');

// --- ESTADO ATUAL DO CALENDÁRIO ---
let currentDate = new Date();

/**
 * Busca e exibe o nome do paciente.
 */
async function loadPatientInfo() {
    try {
        const cpf = localStorage.getItem('username'); // Salvo no login
        if (!cpf) {
            window.location.href = '/login.html'; // Redireciona se não estiver logado
            return;
        }
        const patient = await apiService.getPatientByCpf(cpf);
        userNameElement.textContent = patient.nome.split(' ')[0]; // Pega só o primeiro nome
    } catch (error) {
        console.error("Falha ao carregar informações do paciente:", error);
        userNameElement.textContent = "Paciente";
    }
}

/**
 * Exibe a próxima consulta no card.
 */
function displayNextAppointment(futureAppointments) {
    if (!futureAppointments || futureAppointments.length === 0) {
        nextAppointmentContent.innerHTML = '<p>Nenhuma consulta futura encontrada.</p>';
        return;
    }
    // Ordena para garantir que a primeira é a mais próxima
    futureAppointments.sort((a, b) => new Date(a.dataHora) - new Date(b.dataHora));
    const nextApp = futureAppointments[0];

    const date = new Date(nextApp.dataHora);
    const formattedDate = date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
    const formattedTime = date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }).replace(':', 'h');

    nextAppointmentDate.textContent = formattedDate;
    nextAppointmentTime.textContent = formattedTime;
    nextAppointmentType.textContent = nextApp.procedimento || 'Consulta';
}

/**
 * Renderiza o calendário para um mês e ano específicos.
 */
function renderCalendar(year, month, appointments) {
    calendarBody.innerHTML = '';
    const monthName = new Date(year, month).toLocaleString('pt-BR', { month: 'long' });
    calendarMonthYear.textContent = `${monthName.charAt(0).toUpperCase() + monthName.slice(1)} ${year}`;

    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    let date = 1;
    for (let i = 0; i < 6; i++) {
        let row = document.createElement('tr');
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < firstDay) {
                row.appendChild(document.createElement('td'));
            } else if (date > daysInMonth) {
                break;
            } else {
                let cell = document.createElement('td');
                cell.textContent = date;

                const today = new Date();
                if (date === today.getDate() && year === today.getFullYear() && month === today.getMonth()) {
                    cell.classList.add('today');
                }

                // Verifica agendamentos para o dia
                const cellDate = new Date(year, month, date);
                const appointmentsForDay = appointments.filter(app => {
                    const appDate = new Date(app.dataHora);
                    return appDate.toDateString() === cellDate.toDateString();
                });

                if (appointmentsForDay.length > 0) {
                    const isPast = cellDate < new Date().setHours(0,0,0,0);
                    cell.classList.add(isPast ? 'completed-day' : 'scheduled-day');
                    cell.dataset.tooltip = appointmentsForDay.map(a => a.procedimento || 'Consulta').join(', ');
                }

                row.appendChild(cell);
                date++;
            }
        }
        calendarBody.appendChild(row);
        if (date > daysInMonth) break;
    }
}

/**
 * Função principal que inicializa o dashboard.
 */
async function initializeDashboard() {
    await loadPatientInfo();
    try {
        const [futureAppointments, pastAppointments] = await Promise.all([
            apiService.getFutureAppointments(),
            apiService.getPastAppointments()
        ]);

        displayNextAppointment(futureAppointments);

        const allAppointments = [...futureAppointments, ...pastAppointments];
        renderCalendar(currentDate.getFullYear(), currentDate.getMonth(), allAppointments);
    } catch (error) {
        console.error("Erro ao carregar dados do dashboard:", error);
    }
}

// Inicia a aplicação quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', initializeDashboard);
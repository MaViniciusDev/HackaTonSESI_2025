// js/api.js

// URL base da sua API. Altere se o seu backend rodar em um endereço ou porta diferente.
const BASE_URL = 'http://localhost:8080/api';

/**
 * Função auxiliar que cria os cabeçalhos para as requisições.
 * Se houver um token de autenticação no localStorage, ele é adicionado.
 * @returns {HeadersInit} - Objeto com os cabeçalhos.
 */
const getAuthHeaders = () => {
    const token = localStorage.getItem('authToken');
    const headers = {
        'Content-Type': 'application/json',
    };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
};

/**
 * Objeto que contém todas as funções para interagir com a API.
 */
const apiService = {

    /**
     * Autentica um usuário e salva o token e outras informações no localStorage.
     * @param {string} cpf - O CPF do usuário.
     * @param {string} password - A senha do usuário.
     * @returns {Promise<object>} - Os dados retornados pela API (token, roles, etc.).
     */
    login: async (cpf, password) => {
        try {
            const response = await fetch(`${BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ cpf, password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Falha no login. Verifique suas credenciais.');
            }

            const data = await response.json();

            if (data.token) {
                localStorage.setItem('authToken', data.token);
                localStorage.setItem('userRoles', JSON.stringify(data.roles));
                localStorage.setItem('username', data.username); // 'username' aqui é o CPF
            }

            return data;
        } catch (error) {
            console.error("Erro na função login:", error);
            throw error;
        }
    },

    /**
     * Desloga o usuário limpando os dados do localStorage.
     */
    logout: () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRoles');
        localStorage.removeItem('username');
    },

    /**
     * Busca os dados de um paciente pelo CPF.
     * @param {string} cpf - O CPF do paciente.
     * @returns {Promise<object>} - Os dados do paciente.
     */
    getPatientByCpf: async (cpf) => {
        try {
            const response = await fetch(`${BASE_URL}/patients/by-cpf/${cpf}`, {
                method: 'GET',
                headers: getAuthHeaders(),
            });
            if (!response.ok) {
                throw new Error('Erro ao buscar dados do paciente.');
            }
            return await response.json();
        } catch (error) {
            console.error("Erro na função getPatientByCpf:", error);
            throw error;
        }
    },

    /**
     * Busca a lista de agendamentos futuros do paciente logado.
     * @returns {Promise<Array>} - Uma lista de agendamentos.
     */
    getFutureAppointments: async () => {
        try {
            const response = await fetch(`${BASE_URL}/appointments/future`, {
                method: 'GET',
                headers: getAuthHeaders(),
            });
            if (!response.ok) {
                throw new Error('Erro ao buscar agendamentos futuros.');
            }
            return await response.json();
        } catch (error) {
            console.error("Erro na função getFutureAppointments:", error);
            throw error;
        }
    },

    /**
     * Busca a lista de agendamentos passados do paciente logado.
     * @returns {Promise<Array>} - Uma lista de agendamentos.
     */
    getPastAppointments: async () => {
        try {
            const response = await fetch(`${BASE_URL}/appointments/past`, {
                method: 'GET',
                headers: getAuthHeaders(),
            });
            if (!response.ok) {
                throw new Error('Erro ao buscar agendamentos passados.');
            }
            return await response.json();
        } catch (error) {
            console.error("Erro na função getPastAppointments:", error);
            throw error;
        }
    },

    // Você pode adicionar outras funções aqui conforme sua necessidade. Exemplo:
    /*
    registerUser: async (userData) => {
        try {
            const response = await fetch(`${BASE_URL}/users/register`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(userData)
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Falha no registro de usuário');
            }
            return await response.json();
        } catch(error) {
            console.error("Erro ao registrar usuário:", error);
            throw error;
        }
    },
    */
};

// Exporta o objeto para que ele possa ser importado em outros arquivos, como o dashboard.js
export default apiService;
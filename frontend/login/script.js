// src/js/script.js

// 1. Importa o nosso serviço de API
// O caminho './services/api.js' deve ser relativo à localização deste script.
import apiService from './services/api.js';

// 2. Seleciona os elementos do formulário e de feedback
const loginForm = document.getElementById('loginForm');
const cpfInput = document.getElementById('cpf'); // Supondo que seu input de CPF tenha id="cpf"
const senhaInput = document.getElementById('senha'); // Supondo que seu input de senha tenha id="senha"
const loginButton = document.getElementById('loginButton'); // Supondo que seu botão de submit tenha id="loginButton"
const errorMessageElement = document.getElementById('errorMessage'); // Supondo que você tenha um <p id="errorMessage"></p> para erros

// 3. Adiciona o evento de 'submit'
loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const cpf = cpfInput.value.trim();
    const senha = senhaInput.value.trim();

    if (!cpf || !senha) {
        errorMessageElement.textContent = 'Por favor, preencha o CPF e a Senha.';
        errorMessageElement.style.display = 'block';
        return;
    }

    // --- LÓGICA DE CHAMADA À API ---

    // Desabilita o botão para evitar múltiplos cliques
    loginButton.disabled = true;
    loginButton.textContent = 'Entrando...';
    errorMessageElement.style.display = 'none'; // Esconde erros antigos

    try {
        // 4. Chama a função de login do nosso apiService!
        // A função é 'async', então usamos 'await' para esperar a resposta.
        const data = await apiService.login(cpf, senha);

        // 5. Se o login for bem-sucedido:
        alert('Login realizado com sucesso!');

        // Redireciona o usuário para o painel principal (dashboard)
        // Lembre-se que as roles e o token já foram salvos no localStorage pelo apiService
        window.location.href = '/dashboard.html'; // Mude para a sua página principal

    } catch (error) {
        // 6. Se o login falhar:
        // O apiService lança um erro que capturamos aqui.
        console.error('Falha no login:', error);
        errorMessageElement.textContent = error.message || 'CPF ou senha inválidos. Tente novamente.';
        errorMessageElement.style.display = 'block';

    } finally {
        // 7. 'finally' é executado sempre, seja em caso de sucesso ou erro.
        // Reabilita o botão para que o usuário possa tentar novamente.
        loginButton.disabled = false;
        loginButton.textContent = 'Entrar';
    }
});
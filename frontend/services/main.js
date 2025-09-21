// js/main.js

document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.querySelector('main-sidebar');
    const openSidebarBtn = document.querySelector('.sidebar-toggle');
    const mainContent = document.querySelector('.main-content');

    // NOVO: Seleciona o ícone <i> dentro do botão principal
    const openSidebarIcon = openSidebarBtn.querySelector('i');

    // Espera o componente da sidebar ser definido para poder interagir com ele
    customElements.whenDefined('main-sidebar').then(() => {
        const closeSidebarBtn = sidebar.closeButton;
        // NOVO: Seleciona o ícone <i> dentro do botão de fechar do menu
        const closeSidebarIcon = closeSidebarBtn.querySelector('i');

        // Função para abrir o menu
        const openSidebar = () => {
            sidebar.classList.add('open');
            mainContent.classList.add('sidebar-open');
            // NOVO: Troca os ícones para o 'X' (fechar)
            openSidebarIcon.classList.remove('fa-bars');
            openSidebarIcon.classList.add('fa-times');
            closeSidebarIcon.classList.remove('fa-bars');
            closeSidebarIcon.classList.add('fa-times');
        };

        // Função para fechar o menu
        const closeSidebar = () => {
            sidebar.classList.remove('open');
            mainContent.classList.remove('sidebar-open');
            // NOVO: Troca os ícones de volta para 'hambúrguer' (abrir)
            openSidebarIcon.classList.remove('fa-times');
            openSidebarIcon.classList.add('fa-bars');
            closeSidebarIcon.classList.remove('fa-times');
            closeSidebarIcon.classList.add('fa-bars');
        };

        // Eventos
        openSidebarBtn.addEventListener('click', () => {
            // Verifica se o menu está aberto para decidir se abre ou fecha
            if (sidebar.classList.contains('open')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        closeSidebarBtn.addEventListener('click', closeSidebar);

        // Fecha o menu se clicar no overlay (fundo escurecido)
        mainContent.addEventListener('click', (e) => {
            if (mainContent.classList.contains('sidebar-open')) {
                if (e.target === mainContent) {
                    closeSidebar();
                }
            }
        });

        // Lógica de logout
        sidebar.logoutButton.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.clear();
            window.location.href = '/login.html';
        });
    });
});
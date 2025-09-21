// js/Sidebar.js

const sidebarTemplate = document.createElement('template');
sidebarTemplate.innerHTML = `
    <style>
        :host {
            font-family: 'Roboto', sans-serif;
            width: 280px;
            background-color: var(--teal, #06b6a9);
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            z-index: 1000;
            display: flex;
            flex-direction: column;
            padding: 24px;
            box-shadow: 4px 0 10px rgba(0,0,0,0.1);
            color: var(--white, #fff);
            transform: translateX(-100%);
            transition: transform 0.3s ease-in-out;
        }

        :host(.open) {
            transform: translateX(0);
        }

        .menu-header {
            /* AUMENTADO: Mais espaço abaixo do botão */
            padding-bottom: 32px; 
        }
        
        #closeBtn {
             background: none;
             border: none;
             color: white;
             font-size: 1.5rem;
             cursor: pointer;
             padding: 0;
        }

        nav {
            flex-grow: 1;
            overflow-y: auto;
        }

        .menu-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .menu-list li a {
            color: var(--white, #fff);
            text-decoration: none;
            font-size: 1rem;
            padding: 14px 16px;
            display: block;
            border-radius: 8px;
            transition: background-color 0.2s;
            font-weight: 500;
        }

        .menu-list a:hover, .menu-list a.active {
            background-color: rgba(255, 255, 255, 0.15);
        }

        .logout-link {
            color: var(--white, #fff);
            text-decoration: none;
            font-size: 1rem;
            padding: 12px 16px;
            display: flex;
            align-items: center;
            border-radius: 8px;
            transition: background-color 0.2s;
            font-weight: 500;
        }
        
        .logout-link img {
            margin-right: 12px;
            width: 22px;
            height: 22px;
            filter: brightness(0) invert(1);
        }

        .logout-link:hover {
            background-color: rgba(255, 255, 255, 0.15);
        }
    </style>

    <header class="menu-header">
        <button id="closeBtn" aria-label="Fechar menu"><i class="fas fa-times"></i></button>
    </header>
    <nav>
        <ul class="menu-list">
            <li><a href="#" class="active">Agendamento</a></li>
            <li><a href="#">Meu tratamento</a></li>
            <li><a href="#">Calendário</a></li>
            <li><a href="#">Notícias</a></li>
            <li><a href="#">Meu perfil</a></li>
            <li><a href="#">Configurações</a></li>
        </ul>
    </nav>

    <a href="/login.html" class="logout-link" id="logoutBtn">
        <img src="../assets/icon/Logout.svg" alt="Sair">
        <span>Sair</span>
    </a>
`;

class MainSidebar extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this.shadowRoot.appendChild(sidebarTemplate.content.cloneNode(true));
    }

    get closeButton() {
        return this.shadowRoot.querySelector('#closeBtn');
    }

    get logoutButton() {
        return this.shadowRoot.querySelector('#logoutBtn');
    }
}

customElements.define('main-sidebar', MainSidebar);
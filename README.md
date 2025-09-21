# 🦷 Sistema de Gestão de Clínica Odontológica - SESI+

> **Projeto desenvolvido para o Hackathon SESI Saúde 2025**

Sistema completo de gestão para clínicas odontológicas, desenvolvido em Spring Boot, que oferece controle total sobre pacientes, agendamentos, tratamentos e prontuários médicos.

## 📋 Sobre o Projeto

O **SESI+** é uma solução digital inovadora para modernizar a gestão de clínicas odontológicas, facilitando o dia a dia de dentistas, recepcionistas e pacientes. O sistema foi desenvolvido com foco na experiência do usuário e na eficiência operacional.

### 🎯 Objetivos

- **Modernizar** o atendimento odontológico com tecnologia
- **Centralizar** informações de pacientes e tratamentos
- **Otimizar** agendamentos e disponibilidade dos profissionais
- **Digitalizar** prontuários e histórico médico
- **Facilitar** a comunicação entre pacientes e profissionais
- **Garantir** segurança e privacidade dos dados médicos

### 🏥 Funcionalidades Principais

#### Para Pacientes
- 📝 **Auto-cadastro** no sistema
- 📅 **Agendamento online** de consultas
- ✨ **Dashboard interativo** com calendário de consultas
- 📊 **Acompanhamento visual** do progresso do tratamento
- 📜 **Histórico** de consultas e procedimentos

#### Para Recepcionistas  
- 👥 **Cadastro e gestão** de pacientes
- 📋 **Controle de agendamentos**
- 🔍 **Busca rápida** por pacientes (nome/CPF)
- 📞 **Suporte** ao agendamento telefônico

#### Para Dentistas
- 🗓️ **Dashboard** com agenda do dia
- 📄 **Prontuários eletrônicos** completos
- 💊 **Catálogo de procedimentos** por especialidade
- 🔄 **Encaminhamentos** entre especialistas
- 📈 **Análises de tratamento** com IA
- ⏰ **Histórico** de consultas passadas e futuras

## 🏗️ Arquitetura do Sistema

O projeto foi desenvolvido seguindo uma arquitetura modular bem definida:

### 📦 Módulos Principais

```
src/main/java/org/utopia/hackatonsesi_2025/
├── 🏠 app/                    # Configuração principal da aplicação
├── ⚙️  config/                # Configurações de segurança e beans
├── 👥 patients/               # Gestão de pacientes
├── 📅 scheduling/             # Agendamentos e encaminhamentos  
├── 🦷 treatments/             # Procedimentos e tratamentos
├── 👤 users/                  # Autenticação e usuários
└── 📋 records/                # Prontuários médicos
```

### 🔐 Sistema de Autenticação

- **JWT (JSON Web Tokens)** para autenticação segura
- **Controle de acesso baseado em roles**:
  - `PATIENT` - Pacientes
  - `RECEPTION` - Recepcionistas  
  - `DENTIST` - Dentistas

### 🎭 Especialidades Odontológicas

O sistema suporta todas as principais especialidades:

- 🦷 **Clínico Geral** - Consultas e procedimentos básicos
- ⚕️ **Cirurgia Bucomaxilofacial** - Cirurgias complexas
- 🔬 **Endodontia** - Tratamento de canal
- 🦴 **Implantodontia** - Implantes dentários
- 👑 **Prótese** - Próteses dentárias
- 📐 **Ortodontia** - Aparelhos ortodônticos
- 🦠 **Periodontia** - Tratamento gengival
- 👶 **Odontopediatria** - Atendimento infantil
- ✨ **Estética** - Procedimentos estéticos

## 🚀 APIs e Endpoints

### 🔐 Autenticação (`/api/auth`)
```http
POST /api/auth/login          # Login no sistema
```

### 👥 Usuários (`/api/users`)
```http
POST /api/users/register      # Registro de novos usuários
```

### 🏥 Pacientes (`/api/patients`)
```http
POST /api/patients/self-register    # Auto-cadastro (paciente)
POST /api/patients/intake           # Cadastro (recepção)
GET  /api/patients/search           # Busca por nome
GET  /api/patients/by-cpf/{cpf}     # Busca por CPF
```

### 📅 Agendamentos (`/api/appointments`)
```http
GET  /api/appointments/today               # Agenda do dia
GET  /api/appointments/past               # Consultas passadas  
GET  /api/appointments/future             # Consultas futuras
GET  /api/appointments/availability       # Disponibilidade
GET  /api/appointments/search-by-cpf      # Busca por CPF
GET  /api/appointments/search-by-name     # Busca por nome
```

### 🦷 Procedimentos (`/api/procedures`) 
```http
GET  /api/procedures/catalog              # Catálogo de procedimentos
POST /api/procedures/schedule             # Agendar procedimento
GET  /api/procedures/patient-progress     # Retorna o progresso do tratamento para o paciente logado
GET  /api/procedures/catalog              # Catálogo de procedimentos
POST /api/procedures/schedule             # Agendar procedimento
GET  /api/procedures/orders               # Ordens de procedimento
GET  /api/procedures/orders/{id}          # Detalhes da ordem
POST /api/procedures/orders/{id}/complete # Completar procedimento
GET  /api/procedures/orders/{id}/analyses # Análises do tratamento
```

### 🔄 Encaminhamentos (`/api/referrals`)
```http
POST /api/referrals                    # Criar encaminhamento
GET  /api/referrals/pending           # Encaminhamentos pendentes
POST /api/referrals/{id}/schedule     # Agendar de encaminhamento
```

### 📋 Prontuários (`/api/records`)
```http
POST /api/records                     # Criar prontuário
GET  /api/records/{id}               # Visualizar prontuário  
GET  /api/records/by-patient/{id}    # Prontuários do paciente
PATCH /api/records/{id}              # Atualizar prontuário
```

## 🖥️ Frontend - Interface do Paciente

O frontend foi desenvolvido com HTML5, CSS3 e JavaScript puro (ES6+), focando em performance e manutenibilidade sem a necessidade de frameworks pesados. A interface é limpa, responsiva e consome a API do backend de forma assíncrona.

### ✨ Destaques do Frontend
- **Componentização com Web Components**: O menu lateral foi criado como um componente reutilizável, garantindo um código limpo e fácil de manter em todas as páginas.
- **Layout Responsivo**: Utilização de Flexbox e CSS Grid para uma adaptação perfeita a telas de desktop, tablets e celulares.
- **Dinamismo Puro**: Todos os dados (nome do paciente, calendário, progresso, etc.) são carregados dinamicamente através de chamadas à API, sem recarregar a página.


### 📁 Estrutura de Arquivos (Frontend)
```
frontend/
├── assets/
│   ├── icon/
│   └── imgs/
├── dashboard/
├── login/
└── services/
```

## 🛠️ Stack Tecnológica

### Backend
- ☕ **Java 17** - Linguagem de programação
- 🍃 **Spring Boot 3** - Framework principal
- 🔒 **Spring Security** - Autenticação e autorização
- 🗄️ **Spring Data JPA** - Persistência de dados
- 🐘 **PostgreSQL** - Banco de dados
- 🎫 **JWT** - Tokens de autenticação
- 📝 **Lombok** - Redução de boilerplate
- ✅ **Bean Validation** - Validação de dados

### Frontend
- 🌐 **HTML5** - Estrutura semântica
- 🎨 **CSS3** - Estilização moderna (Flexbox, Grid, Variáveis)
- 💡 **JavaScript (ES6+)** - Lógica e dinamismo (Módulos, Async/Await)
- 🧩 **Web Components** - Componentização reutilizável (Menu Lateral)
- 🖼️ **Font Awesome** - Biblioteca de ícones

### Ferramentas
- 🔨 **Maven** - Gerenciamento de dependências
- 🐳 **Docker** (preparado) - Containerização

  
## ⚙️ Configuração e Instalação

### 📋 Pré-requisitos

- ☕ **JDK 17** ou superior
- 🐘 **PostgreSQL 12** ou superior
- 🔨 **Maven 3.8** ou superior

### 🔧 Configuração do Banco de Dados

1. Crie um banco PostgreSQL:
```sql
CREATE DATABASE hackatonsesi;
CREATE USER postgres WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE hackatonsesi TO postgres;
```

2. Configure as variáveis de ambiente (opcional):
```bash
export DB_HOST=localhost
export DB_PORT=5432  
export DB_NAME=hackatonsesi
export DB_USER=postgres
export DB_PASSWORD=admin
export JWT_SECRET=your-super-secret-key
export JWT_ISSUER=utopia-hackatonsesi
export JWT_EXPIRES_IN_SECONDS=3600
```

### 🚀 Executando a Aplicação

1. Clone o repositório:
```bash
git clone https://github.com/MaViniciusDev/HackaTonSESI_2025.git
cd HackaTonSESI_2025
```

2. Compile o projeto:
```bash
./mvnw clean compile
```

3. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

4. Acesse: `http://localhost:8080`

### 🖥️ Frontend
1.  Não é necessário um passo de build.
2.  Para rodar localmente, é **altamente recomendado** usar um servidor web simples devido ao uso de Módulos JavaScript (ESM). A forma mais fácil é usar a extensão **"Live Server"** no Visual Studio Code.
3.  Com a extensão instalada, clique com o botão direito no arquivo `login.html` e selecione "Open with Live Server".

### 🗄️ Dados de Teste

O sistema inclui um seeder que cria automaticamente:
- ✅ Catálogo completo de procedimentos odontológicos
- 👨‍⚕️ Perfil de dentista para testes (`username: dentista`)
- 🏥 Dados básicos para demonstração

## 📊 Modelo de Dados

### Entidades Principais

- 👤 **AppUser** - Usuários del sistema (autenticação)
- 🏥 **Patients** - Dados dos pacientes  
- 📅 **Appointment** - Agendamentos de consultas
- 🦷 **ProcedureCatalog** - Catálogo de procedimentos
- 📋 **ProcedureOrder** - Ordens de procedimento
- 👨‍⚕️ **DentistProfile** - Perfis dos dentistas
- 🔄 **Referral** - Encaminhamentos entre especialistas
- 📄 **MedicalRecord** - Prontuários médicos
- 📊 **TreatmentAnalysis** - Análises de tratamento

## 🔒 Segurança

- 🛡️ **Autenticação JWT** com expiração configurável
- 🔐 **Controle de acesso baseado em roles**
- 🔒 **Senhas hasheadas** com BCrypt
- 🛡️ **Validação de entrada** em todos os endpoints
- 🔍 **Auditoria** de operações sensíveis

## 🌟 Diferencial do Projeto

- 🚀 **Arquitetura moderna** e escalável
- 🎯 **Foco na experiência do usuário**
- 🔄 **Integração completa** entre módulos
- 📱 **API RESTful** pronta para frontend
- 🧠 **Preparado para IA** (análises de tratamento)
- 🔒 **Segurança robusta** desde o início
- 📊 **Relatórios e análises** integrados

## 👥 Equipe de Desenvolvimento

- **Marcelo Vinicius Gomes** - Desenvolvedor Principal

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. 🍴 Fork o projeto
2. 🌿 Crie uma branch (`git checkout -b feature/amazing-feature`)
3. 💾 Commit suas mudanças (`git commit -m 'Add amazing feature'`)
4. 📤 Push para a branch (`git push origin feature/amazing-feature`)
5. 🔄 Abra um Pull Request

## 📞 Contato

Para dúvidas ou sugestões sobre o projeto, entre em contato através do GitHub.

---

<div align="center">

**Desenvolvido com ❤️ para o Hackathon SESI Saúde 2025**

*Transformando a gestão odontológica através da tecnologia*

</div>

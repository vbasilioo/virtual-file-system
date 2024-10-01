
# Virtual File System

## Sumário
1. [Visão Geral do Projeto](#visão-geral-do-projeto)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Funcionalidades Principais](#funcionalidades-principais)
4. [Arquitetura do Sistema](#arquitetura-do-sistema)
5. [Endpoints da API](#endpoints-da-api)
6. [Instalação e Execução](#instalação-e-execução)
7. [Considerações Finais](#considerações-finais)

## Visão Geral do Projeto
O **Virtual File System (VFS)** é um sistema de gerenciamento de arquivos de computadores virtuais desenvolvido em Java 23, Spring 3.3.4, Next.js 14, Shadcn, TypeScript e Tailwind. O VFS permite que usuários criem e gerenciem suas contas, configurem seu SSD virtual e memória RAM, e armazenem arquivos e diretórios em uma estrutura de árvore binária. O sistema é projetado para ser responsivo, oferecendo suporte a temas claro e escuro, e possui funcionalidades específicas para administradores.

## Tecnologias Utilizadas
- **Java 23**: Para o backend do sistema.
- **Spring 3.3.4**: Para desenvolvimento do backend e gerenciamento de dependências.
- **Next.js 14**: Para a construção da interface do usuário no frontend.
- **Shadcn**: Para estilização e componentes UI.
- **TypeScript**: Para desenvolvimento frontend com tipagem estática.
- **Tailwind CSS**: Para estilização responsiva e customizável.
- **Docker**: Para containerização da aplicação.
- **Clean Architecture e Clean Code**: Para organização e manutenção do código.

## Funcionalidades Principais
- **Gerenciamento de Conta**: Usuários podem criar contas e gerenciar suas configurações de SSD e memória RAM.
- **Armazenamento de Arquivos**: Usuários podem guardar arquivos e criar diretórios, que seguem uma estrutura de árvore binária.
- **Controle de Acesso**: Apenas administradores podem excluir diretórios e gerenciar configurações avançadas.
- **Limitações de Tamanho**: Arquivos não podem exceder o tamanho disponível no SSD virtual do usuário.
- **Responsividade**: Interface adaptável para dispositivos móveis com suporte a temas claro e escuro.
- **Login Único**: A conta root é necessária para a criação de novas contas.

## Arquitetura do Sistema
O sistema é construído utilizando a **Clean Architecture**, garantindo que as camadas de apresentação, aplicação e domínio sejam bem definidas e separadas. O uso de **recursividade** para a criação de diretórios e **filas** para a organização de arquivos assegura a eficiência do sistema.

## Endpoints da API

### Autenticação

- **Login**
  ```http
  POST /auth/login
  Request Body:
  {
      "username": "basilio",
      "password": "123456789"
  }
  ```

- **Registro**
  ```http
  POST /auth/register
  Request Body:
  {
      "username": "basilio",
      "password": "123456789",
      "role": "ADMIN"
  }
  ```

### Gerenciamento de Arquivos

- **Obter Todos os Arquivos**
  ```http
  GET /files
  ```

- **Atualizar Arquivo**
  ```http
  PUT /files/:id_file
  ```

- **Obter Arquivo por Caminho**
  ```http
  GET /files/documents/:file_name
  ```

- **Criar Arquivo**
  ```http
  POST /files
  Request Body:
  {
      "path": "/pasta-principal",
      "name": "arquivo-novo.txt",
      "size": 2048,
      "directory": {
          "id": 3
      }
  }
  ```

- **Excluir Arquivo**
  ```http
  DELETE /files/:id_file
  ```

### Gerenciamento de Diretórios

- **Criar Diretório**
  ```http
  POST /directories
  Request Body:
  {
      "path": "/pasta-principal",
      "name": "Pasta Principal",
      "children": [
          {
              "path": "/pasta-principal/pasta-filho-1",
              "name": "Pasta Filho 1",
              "children": []
          }
      ]
  }
  ```

- **Obter Todos os Diretórios**
  ```http
  GET /directories
  ```

- **Excluir Diretório**
  ```http
  DELETE /directories/:id
  ```

- **Estatísticas de Tamanho Total dos Arquivos**
  ```http
  GET /directories/statistics/total-file-size
  ```

- **Visão Geral**
  ```http
  GET /directories/statistics/overview
  ```

- **Atualizar Diretório**
  ```http
  PUT /directories/:id_directory
  ```

## Instalação e Execução

1. **Pré-requisitos**:
   - Certifique-se de ter o [Docker](https://www.docker.com/) instalado.
   - Java 23 e Maven devem estar instalados para compilar o projeto.

2. **Clonar o Repositório**:
   ```bash
   git clone https://github.com/seu_usuario/virtual-file-system.git
   cd virtual-file-system
   ```

3. **Construir e Executar com Docker**:
   ```bash
   docker-compose up --build
   ```

4. **Acessar a Aplicação**:
   - Frontend: `http://localhost:3000`
   - Backend: `http://localhost:8080/api`

## Considerações Finais
O Virtual File System é um sistema robusto e flexível para gerenciamento de arquivos virtuais. Com uma arquitetura limpa e tecnologias modernas, o VFS visa fornecer uma experiência de usuário fluida e eficaz. Para mais informações ou contribuições, consulte a documentação no repositório do projeto.

## Links Úteis
Acesse [aqui](https://github.com/vbasilioo/virtual-file-system-web) para ficar por dentro do repositório do Front-end!

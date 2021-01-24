# Sistema de Gestão de Carregamentos de Veículos Elétricos

Esta aplicação está inserida no projeto que foi realizado no âmbito da unidade curricular de Sistemas de Engenharia - Automação e Instrumentação, inserida no Mestrado Integrado de Engenharia Electrotécnica e de Computadores da Faculdade de Engenharia da Univerdidade do Porto. O projeto proposto foi o desenvolvimento de um sistema de gestão de carregamento de veículos elétricos, com vista a aplicá-lo num parque de carregamento público ou privado (por exemplo, parque da Faculdade de Engenharia ou de um supermercado).

## Aplicação para dispositivos móveis (Android)

A aplicação para dispositivos móveis foi desenvolvida para o sistema operativo Android e a linguagem utilizada para programação da mesma foi Kotlin (desenvolvimento nativo deste sistema operativo). A plataforma Firebase desenvolvida pela Google para a criação de aplicativos móveis e da web, foi também utilizada para armazenar os dados de login *Authentication* e todos os dados gerados pela aplicação *Cloud Firestore*: informação de carregamentos e informação sobre os veículos adicionados pelo utilizador. Uma vez que a API que estabelece comunicação com o módulo do Controlo e com a base de dados não está alojada em nenhum servidor (é corrida localmente), utilizou-se a ferramenta *ngrok* que permite criar túneis seguros para um servidor local.

### Visão geral da aplicação móvel
![Estrutura](https://user-images.githubusercontent.com/47570179/105553859-24931680-5cfe-11eb-92d2-ac456e6e4e08.jpg)

### Sequência de carregamento
![SequenciaCarregamento](https://user-images.githubusercontent.com/47570179/105553458-52c42680-5cfd-11eb-9218-d124b9fc3a19.jpg)

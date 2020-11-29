# SEAI-Interface

Este repositório é um dos módulos do sistema de gestão de carregamento de veículos elétricos. Este sistema foi dividido em Controlo, Gestão e Monitorização e Interface com o Utilizador.

Uma das partes essenciais deste grande sistema é a interacção com o utilizador de forma a que se consiga apresentar-lhe a informação necessária e permitir que este possa tomar decisões simples associadas ao carregamento. Numa abordagem mínima e essencial a interface será feita localmente no carregador e adicionalmente será feita através de uma aplicação para smartphones.

<p align="center">
  <img src="https://github.com/up201606615/SEAI-Interface/blob/main/Design/SBS-Interface.png">
</p>

A Interface com o utilizador é um elemento que se baseia na comunicação entre o utilizador e o software, mais especificamente, a componente de Controlo. Assim, é possível dividi-la em três diferentes ramos: Comunicação, Interação com o utilizador e Microcontrolador.

**Comunicação**

Para que as configurações feitas pelo utilizador sejam passadas ao sistema, terá de ser estabelecer uma via de comunicação entre a Interface a o módulo do Controlo. A comunicação com o módulo de Controlo será feita através de Transmission Control Protocol (TCP). Além disso, será também preciso estabelecer uma ligação com a base de dados para consulta de dados necessários para apresentar ao utilizador (preços, disponibilidade, etc.).

**Interação com o utilizador**

A interação com o utilizador vai ser feita, inicialmente, através da interface existente no carregador local (requisito mínimo). Onde será possível o utilizador consultar preços, escolher o modo de carregamento (rápido ou normal) e iniciar e interromper o carregamento. Após a concretização desta plataforma, para uma melhor interação com o utilizador, será desenvolvida uma aplicação para dispositivos móveis (requisito adicional). Esta aplicação, inicialmente, terá as mesmas funcionalidades que a plataforma local e, posteriormente, serão desenvolvidas funcionalidades adicionais como o registo do utilizador e a marca e modelo do veículo que conduz para que o carregamento seja mais personalizado. Adicionalmente, considerou-se interessante desenvolver um sistema de pontuação para os utilizadores da aplicação onde seriam distinguidos os "utilizadores do mês" (procurando beneficiar estes clientes), de forma a que os condutores se sentissem motivados a utilizar a aplicação. No entanto, a equipa reconhece que esta funcionalidade provavelmente não será implementada, uma vez que se vai dar prioridade à implementação de funcionalidades essenciais ao produto.

**Microcontrolador**

De forma a que a interface seja acessível ao utilizador, em cada carregador será simulado por um Raspberry Pi ligado a um ecrã (simulação da interface local do carregador). Este microcontrolador estará a correr a aplicação da interface criada e permitirá assim a interação física entre utilizador e aplicação.

## Authors
* André Martins up201605016@fe.up.pt
* Daniel Luiz up201703713@fe.up.pt
* Ines Soares up201606615@fe.up.pt

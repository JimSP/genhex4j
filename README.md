# Genhex4j

Genhex4j é uma ferramenta de geração de código baseada em templates que cria estruturas de classes e conversores de acordo com a Arquitetura Hexagonal (ou Ports and Adapters). A partir de um descritor JSON, o Genhex4j gera automaticamente classes de Domínio, DTOs e Entidades JPA, além dos conversores e adaptadores necessários. Esse processo torna o desenvolvimento mais eficiente e reduz o risco de erros manuais na criação de código repetitivo.

## Requisitos

- **Java 17**
- **Maven 3.6+**
- **Spring Boot 3.0+**
- **FreeMarker Template Engine**

## Estrutura do Projeto

O Genhex4j está organizado nas seguintes seções principais:

- **config**: Contém os arquivos JSON de configuração (`model.json` e `template-config.json`).
- **src/main/java**: Código-fonte principal, incluindo classes de configuração, processadores de templates e descritores.
- **src/main/resources/templates**: Templates FreeMarker (`.ftl`) utilizados para gerar o código Java.
- **generated**: Pasta onde o código gerado é salvo. Ao iniciar o processo de geração, Genhex4j cria classes nesta pasta, com base no conteúdo de `config/model.json`.

## Configuração do Projeto

Antes de executar a geração de código, configure os arquivos JSON na pasta `config`:

### 1. Arquivo `model.json`

Este arquivo define os modelos de dados (Domínio, DTOs e Entidades) que serão gerados. Exemplo:

```json
{
  "domain": {
    "name": "Produto",
    "attributes": [
      { "name": "id", "type": "Long" },
      { "name": "name", "type": "String" },
      { "name": "price", "type": "BigDecimal" }
    ]
  },
  "dto": {
    "name": "ProdutoDTO",
    "attributes": [
      { "name": "id", "type": "Long" },
      { "name": "name", "type": "String" }
    ]
  },
  "entity": {
    "name": "ProdutoEntity",
    "attributes": [
      { "name": "id", "type": "Long" },
      { "name": "name", "type": "String" },
      { "name": "price", "type": "BigDecimal" }
    ]
  }
}
```

### 2. Arquivo `template-config.json`

Este arquivo configura o caminho dos templates a serem utilizados, permitindo personalizar o conteúdo gerado:

```json
{
  "templates": {
    "controller": "controller.ftl",
    "service": "service.ftl",
    "repository": "repository.ftl",
    "entity": "entity.ftl",
    "dto": "dto.ftl"
  }
}
```

### 3. Dependências do Projeto

O arquivo `pom.xml` inclui dependências do **Spring Boot**, **FreeMarker**, e outras bibliotecas essenciais. Certifique-se de atualizar o `pom.xml` conforme necessário.

## Geração de Código

Para iniciar a geração de código, execute o seguinte comando Maven:

```bash
mvn spring-boot:run
```

Durante a execução, o Genhex4j:

1. Lê os arquivos `model.json` e `template-config.json`.
2. Utiliza os descritores definidos em `model.json` para estruturar classes de Domínio, DTOs, Entidades e conversores.
3. Aplica os templates FreeMarker da pasta `templates` para criar o código.
4. Salva as classes geradas na pasta `generated`.

### Estrutura do Código Gerado

Após a execução, você encontrará o seguinte na pasta `generated`:

- **`controller/ProdutoController.java`**: Classe RESTful controller.
- **`domain/ProdutoDomain.java`**: Classe de Domínio que representa o modelo principal de negócios.
- **`dto/ProdutoDTO.java`**: Classe de Data Transfer Object (DTO) para transferência de dados.
- **`entity/ProdutoEntity.java`**: Classe JPA Entity mapeando o modelo ao banco de dados.
- **`repositories/ProdutoRepository.java`**: Interface de repositório JPA.
- **`services/ProdutoService.java`**: Interface e implementação dos serviços.
- **Conversores entre DTO, Domínio e JPA**: Conversores automáticos para cada tipo de classe, facilitando a transição entre as camadas.

### Personalização de Templates

Para adaptar a geração de código às necessidades do seu projeto:

1. Edite os templates FreeMarker na pasta `templates`.
2. Atualize `template-config.json` com novos templates, caso necessário.

## Arquitetura e Boas Práticas

Genhex4j segue os princípios da Arquitetura Hexagonal, separando a lógica de domínio da lógica de persistência e exposição (controladores). Isso garante um código modular, fácil de manter e independente de infraestrutura específica.

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Clone o repositório.
2. Crie uma nova branch para sua feature.
3. Faça um pull request com uma descrição detalhada das alterações.

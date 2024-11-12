// src/components/Chat.js
import React, { useState } from 'react';
import { Form, Button, ListGroup, InputGroup, Spinner, Alert } from 'react-bootstrap';
import { FaPaperPlane } from 'react-icons/fa';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import rehypeSanitize from 'rehype-sanitize';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { darcula } from 'react-syntax-highlighter/dist/esm/styles/prism';

const systemPrompt = `
    Você é um assistente virtual especializado em auxiliar usuários a preencher formulários de geração de API para entidades específicas, como "Produto" ou "Pessoa". Seu objetivo é guiar o usuário passo a passo através do processo de preenchimento do formulário, fornecendo explicações claras e concisas sobre cada etapa.

    ### **Conhecimento de Estruturas de Dados e Templates:**
    Você possui um entendimento detalhado das seguintes estruturas de dados e templates utilizados no formulário:

    - **Estruturas de Dados:**
      - **Credenciais:**
        - \`openAiApiKey\` (string)
        - \`openAiBaseUrl\` (string)
        - \`chatOptionsModel\` (string)
        - \`chatOptionsTemperature\` (float)
      - **Entity Descriptor:**
        - \`packageName\` (string)
        - \`entityName\` (string)
        - \`systemPrompt\` (string)
        - \`jpaDescriptor\`:
          - \`tableName\` (string)
          - \`attributes\` (lista de objetos com campos: \`name\`, \`type\`, \`primaryKey\`, \`required\`, \`maxLength\`, \`generatedValue\`, \`columnDefinition\`)
        - \`rulesDescriptor\` (lista de objetos com campos: \`ruleName\`, \`description\`, \`ruleInput\`, \`ruleAdditionalInput\`, \`ruleOutput\`, \`llmGeneratedLogic\`, \`javaFunctionalInterface\`, \`javaFunctionalIntefaceMethodName\`)
      - **Templates:**
        - \`name\` (string)
        - \`content\` (string)

    ### **Regras de Interação:**
    1. **Guia Passo a Passo:** Oriente o usuário através das diferentes etapas do formulário, uma de cada vez.
    2. **Evitar Código:** Não gere trechos de código ou exemplos de implementação a menos que o usuário solicite explicitamente.
    3. **Confirmação Antes de Código:** Se o usuário solicitar código, pergunte se ele realmente deseja que o código seja gerado antes de fornecê-lo.
    4. **Clareza e Concisão:** Mantenha as respostas claras, objetivas e focadas na orientação necessária.

    ### **Formato das Respostas:**
    - **Organização:** Utilize **títulos**, **subtítulos** e **listas numeradas** ou **com bullets** para estruturar as informações.
    - **Tabelas:** Quando apropriado, apresente informações em tabelas para maior clareza.
    - **Blocos de Código:** Use blocos de código formatados somente quando solicitado pelo usuário.
    - **Ênfase:** Use **itálico** ou **negrito** para destacar termos importantes.
    - **Limite de Tamanho:** Mantenha as respostas concisas, evitando parágrafos muito longos.

    ### **Exemplos de Interações:**

    - **Usuário:** Como funciona a customização de templates?
      
      **Assistente:**
      A customização de templates permite que você ajuste a estrutura e os componentes da API gerada para atender às necessidades específicas do seu projeto. Isso inclui:

      1. **Definição de Nomes:**
         - **Pacote:** Especifique o nome do pacote onde a classe será localizada.
         - **Tabela JPA:** Defina o nome da tabela no banco de dados.
         - **Classe:** Nome da classe Java que representará a entidade.
         - **Arquivo:** Nome do arquivo Java correspondente.

      2. **Campos da Entidade:**
         - Liste os campos da entidade com seus respectivos tipos de dados e restrições.

      3. **Configurações Adicionais:**
         - **Validações:** Adicione anotações como \`@NotNull\` ou \`@Size\` para garantir a integridade dos dados.
         - **Relacionamentos:** Defina relacionamentos com outras entidades usando anotações como \`@ManyToOne\` ou \`@OneToMany\`.
         - **Métodos Personalizados:** Implemente métodos que contenham lógica de negócio específica.

      Você gostaria de personalizar algum desses aspectos específicos?

    - **Usuário:** Por favor, gere um exemplo de template para a entidade Pessoa.
      
      **Assistente:**
      Claro! Antes de gerar o exemplo de código, você gostaria de confirmar os detalhes a seguir para o template da entidade "Pessoa"?

      1. **Campos da Entidade:**
         - \`id\` (Long)
         - \`nome\` (String)
         - \`sobrenome\` (String)
         - \`cpf\` (String)
         - \`dataNascimento\` (Date)
         - \`endereco\` (String)

      2. **Configurações Adicionais:**
         - **Validações:** Incluir \`@NotNull\` e \`@Size\` para campos como \`nome\`, \`sobrenome\`, \`cpf\`, etc.
         - **Relacionamentos:** Estabelecer relacionamento com outras entidades, por exemplo, \`@ManyToOne\` com a entidade \`Cidade\`.
         - **Métodos Personalizados:** Adicionar métodos como \`calcularIdade()\` para calcular a idade com base em \`dataNascimento\`.

      Você confirma que deseja proceder com esses detalhes ou deseja fazer alguma alteração?
    `;

const Chat = ({ apiKey, baseUrl, model, temperature, entityName, packageName, tableName, templates }) => {
	const [messages, setMessages] = useState([
		{ sender: 'bot', text: 'Olá! Como posso ajudar você hoje?' },
	]);
	const [input, setInput] = useState('');
	const [isLoading, setIsLoading] = useState(false);
	const [errorMessage, setErrorMessage] = useState('');

	const handleSendMessage = async (e) => {
		e.preventDefault();
		if (input.trim() === '') return;

		// Verificar se as credenciais estão presentes
		if (!apiKey || !baseUrl || !model) {
			setErrorMessage('Credenciais do OpenAI não estão configuradas. Por favor, configure-as no formulário principal.');
			return;
		}

		const newMessage = { sender: 'user', text: input };
		setMessages((prev) => [...prev, newMessage]);
		setInput('');
		setIsLoading(true);
		setErrorMessage('');

		try {
			const response = await fetch(`${baseUrl}/v1/chat/completions`, { // Ajuste a URL conforme necessário
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${apiKey}`, // Autenticação via Bearer Token
				},
				body: JSON.stringify({
					model: model,
					messages: [
						{ role: 'system', content: systemPrompt },
						...messages.map((msg) => ({
							role: msg.sender === 'user' ? 'user' : 'assistant',
							content: msg.text,
						})),
						{ role: 'user', content: input },
					],
					temperature: parseFloat(temperature) || 0.7,
				}),
			});

			if (!response.ok) {
				if (response.status === 401) {
					throw new Error('Credenciais inválidas. Por favor, verifique suas configurações.');
				} else if (response.status === 429) {
					throw new Error('Limite de requisições excedido. Tente novamente mais tarde.');
				} else {
					throw new Error('Falha na comunicação com o LLM.');
				}
			}

			const data = await response.json();
			const botMessage = data.choices[0].message.content.trim();

			setMessages((prev) => [...prev, { sender: 'bot', text: botMessage }]);
		} catch (error) {
			console.error('Erro ao enviar mensagem:', error);
			setErrorMessage(error.message || 'Desculpe, ocorreu um erro ao processar sua mensagem.');
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<div className="border rounded p-3" style={{ height: '80vh', display: 'flex', flexDirection: 'column' }}>
			<h5 className="mb-3">Assistente de Preenchimento</h5>
			<div className="flex-grow-1 overflow-auto mb-3">
				<ListGroup>
					{messages.map((msg, idx) => (
						<ListGroup.Item
							key={idx}
							className={`d-flex ${msg.sender === 'bot' ? 'justify-content-start' : 'justify-content-end'}`}
						>
							<div
								className={`p-2 rounded ${msg.sender === 'bot' ? 'bg-light' : 'bg-primary text-white'
									}`}
								style={{ maxWidth: '75%' }}
							>
								{msg.sender === 'bot' ? (
									<ReactMarkdown
										children={msg.text}
										remarkPlugins={[remarkGfm]}
										rehypePlugins={[rehypeSanitize]}
										components={{
											code({ node, inline, className, children, ...props }) {
												const match = /language-(\w+)/.exec(className || '');
												return !inline && match ? (
													<SyntaxHighlighter
														style={darcula}
														language={match[1]}
														PreTag="div"
														{...props}
													>
														{String(children).replace(/\n$/, '')}
													</SyntaxHighlighter>
												) : (
													<code className={className} {...props}>
														{children}
													</code>
												);
											},
										}}
									/>
								) : (
									msg.text
								)}
							</div>
						</ListGroup.Item>
					))}
					{isLoading && (
						<ListGroup.Item className="d-flex justify-content-start">
							<div className="p-2 rounded bg-light d-flex align-items-center">
								<Spinner animation="border" size="sm" /> <em className="ms-2">Carregando...</em>
							</div>
						</ListGroup.Item>
					)}
				</ListGroup>
			</div>
			{errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
			<Form onSubmit={handleSendMessage}>
				<InputGroup>
					<Form.Control
						type="text"
						placeholder="Digite sua mensagem..."
						value={input}
						onChange={(e) => setInput(e.target.value)}
						disabled={isLoading}
					/>
					<Button variant="primary" type="submit" disabled={isLoading}>
						<FaPaperPlane />
					</Button>
				</InputGroup>
			</Form>
		</div>
	);
};

export default Chat;

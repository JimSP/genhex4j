// src/components/IntroductionGuide.js
import React from 'react';
import { Carousel, Button, ProgressBar, Container, Row, Col } from 'react-bootstrap';
import {
	FaRocket,
	FaKey,
	FaInfoCircle,
	FaCogs,
	FaGavel,
	FaFileAlt,
	FaCheckCircle,
} from 'react-icons/fa';
import ReactMarkdown from 'react-markdown';
import rehypeRaw from 'rehype-raw';
import rehypeSanitize from 'rehype-sanitize';

const IntroductionGuide = ({ onFinish, onSkip }) => { // Adicionamos a prop onSkip
	const [index, setIndex] = React.useState(0);

	const handleSelect = (selectedIndex) => {
		setIndex(selectedIndex);
	};

	const slides = [
		{
			title: 'Bem-vindo ao Formulário de API',
			icon: <FaRocket size={60} color="#007bff" />,
			description: `
**Bem-vindo!** Nossa ferramenta foi desenvolvida para facilitar a criação e gestão de APIs de forma eficiente e intuitiva. Vamos guiá-lo passo a passo para garantir que você aproveite ao máximo todas as funcionalidades oferecidas.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Credenciais da API',
			icon: <FaKey size={60} color="#28a745" />,
			description: `
Para começar, você precisa inserir sua **API Key** de um modelo de linguagem compatível com OpenAI. Essa chave é essencial para autenticação e para que possamos acessar os recursos necessários para gerar sua API personalizada.

### Como Obter uma API Key:
1. Acesse o site [https://console.groq.com/playground](https://console.groq.com/playground).
2. Crie uma conta ou faça login se já possuir uma.
3. Navegue até a seção de API Keys e gere uma nova chave.
4. Guarde a chave gerada; você a utilizará na próxima etapa do formulário.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Informações Gerais',
			icon: <FaInfoCircle size={60} color="#17a2b8" />,
			description: `
Agora, vamos preencher os detalhes básicos do seu projeto. Essas informações são fundamentais para a organização e estruturação correta da sua API.

### O que Preencher:
- **Nome do Pacote:** Defina o nome do pacote onde sua API será localizada.
- **Nome da Entidade:** Nome da principal entidade que sua API irá gerenciar.
- **Prompt do Sistema:** Forneça um breve prompt que orientará o comportamento do assistente virtual.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Atributos JPA',
			icon: <FaCogs size={60} color="#ffc107" />,
			description: `
Defina os **atributos da sua entidade JPA**, incluindo tipos de dados, chaves primárias e outras propriedades essenciais. Esses atributos determinam como sua API irá interagir com o banco de dados.

### Como Definir Atributos:
- **Nome do Atributo:** Nome identificador do campo.
- **Tipo do Atributo:** Tipo de dado (e.g., String, Integer, Date).
- **Chave Primária:** Indique se o atributo é uma chave primária.
- **Obrigatório:** Defina se o campo é obrigatório.
- **Tamanho Máximo:** Especifique o tamanho máximo permitido.
- **Valor Gerado:** Indique se o valor é gerado automaticamente.
- **Definição da Coluna:** Forneça definições adicionais para a coluna, se necessário.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Regras de Negócio',
			icon: <FaGavel size={60} color="#dc3545" />,
			description: `
Adicione e gerencie **regras de negócio** que orientarão o comportamento da sua API de acordo com as necessidades específicas do seu projeto. Regras bem definidas garantem que sua API funcione corretamente e atenda aos requisitos desejados.

### Como Adicionar Regras:
- **Nome da Regra:** Identifique a regra.
- **Descrição:** Descreva o propósito da regra.
- **Input da Regra:** Defina o que a regra espera receber.
- **Output da Regra:** Especifique o resultado esperado.
- **Lógica Gerada por LLM:** Detalhe a lógica que será implementada.
- **Interface Funcional Java:** Defina a interface funcional correspondente.
- **Nome do Método da Interface:** Nomeie o método que executará a regra.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Templates Personalizados',
			icon: <FaFileAlt size={60} color="#6f42c1" />,
			description: `
Personalize os **templates** utilizados pela sua API para refletir a identidade e os requisitos específicos do seu projeto. Templates customizados aumentam a eficiência e a qualidade do desenvolvimento, garantindo que sua API atenda exatamente às suas necessidades.

### Como Personalizar Templates:
- **Nome do Template:** Dê um nome significativo ao template.
- **Conteúdo do Template:** Insira o conteúdo que define a estrutura e funcionalidades do template.
			`,
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Revisão e Envio',
			icon: <FaCheckCircle size={60} color="#28a745" />,
			description: `
Revise todas as informações inseridas para garantir que tudo está correto antes de enviar seu formulário. Uma revisão cuidadosa evita erros e assegura que sua API seja criada conforme o esperado.

### O que Revisar:
- **Credenciais da API:** Verifique se a API Key está correta.
- **Informações Gerais:** Confirme os detalhes do pacote e da entidade.
- **Atributos JPA:** Revise os atributos definidos.
- **Regras de Negócio:** Assegure-se de que todas as regras estão corretamente configuradas.
- **Templates:** Verifique se os templates personalizados estão adequados.
			`,
			buttonText: 'Iniciar Formulário',
			buttonVariant: 'success',
			onClick: onFinish, // Chama a função de finalizar introdução
		},
	];

	return (
		<Container fluid className="p-5 bg-white rounded shadow">
			<Row className="justify-content-center">
				<Col md={8}>
					<ProgressBar now={((index + 1) / slides.length) * 100} className="mb-4" />
					<Carousel activeIndex={index} onSelect={handleSelect} indicators={false} controls={false} interval={null}>
						{slides.map((slide, idx) => (
							<Carousel.Item key={idx}>
								<div className="d-flex flex-column align-items-center text-center" style={{ minHeight: '60vh' }}>
									<div className="mb-4">{slide.icon}</div>
									<h2>{slide.title}</h2>
									<div className="mt-3" style={{ fontSize: '1.1rem', maxWidth: '600px' }}>
										<ReactMarkdown
											children={slide.description}
											rehypePlugins={[rehypeRaw, rehypeSanitize]}
											components={{
												a: ({ node, ...props }) => <a {...props} target="_blank" rel="noopener noreferrer" />,
												ul: ({ node, ...props }) => <ul {...props} className="list-unstyled list-disc pl-4" />,
												ol: ({ node, ...props }) => <ol {...props} className="list-decimal pl-4" />,
												h2: ({ node, ...props }) => <h2 {...props} />,
												h3: ({ node, ...props }) => <h3 {...props} />,
												p: ({ node, ...props }) => <p {...props} />,
												strong: ({ node, ...props }) => <strong {...props} />,
												em: ({ node, ...props }) => <em {...props} />,
											}}
										/>
									</div>
									<Button
										variant={slide.buttonVariant}
										onClick={() => {
											console.log(`Slide ${idx + 1}: Botão "${slide.buttonText}" clicado`);
											slide.onClick();
										}}
										className="mt-4 px-4 py-2"
									>
										{slide.buttonText}
									</Button>
								</div>
							</Carousel.Item>
						))}
					</Carousel>
					<div className="d-flex justify-content-between mt-4">
						{index > 0 && (
							<Button variant="outline-secondary" onClick={() => setIndex(index - 1)}>
								Voltar
							</Button>
						)}
						{/* Botão "Pular Introdução" */}
						<Button variant="link" onClick={onSkip} className="text-decoration-none">
							Pular Introdução
						</Button>
					</div>
				</Col>
			</Row>
		</Container>
	);
};

export default IntroductionGuide;

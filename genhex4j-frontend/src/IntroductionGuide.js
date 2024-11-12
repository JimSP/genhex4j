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

const IntroductionGuide = ({ onFinish, onSkip }) => { // Adicionamos a prop onSkip
	const [index, setIndex] = React.useState(0);

	const handleSelect = (selectedIndex) => {
		setIndex(selectedIndex);
	};

	const slides = [
		{
			title: 'Bem-vindo ao Formulário de API',
			icon: <FaRocket size={60} color="#007bff" />,
			description:
				'Descubra como nossa ferramenta facilita a criação e gestão de APIs de forma eficiente e intuitiva. Vamos guiá-lo passo a passo para garantir que você aproveite ao máximo todas as funcionalidades oferecidas.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Credenciais da API',
			icon: <FaKey size={60} color="#28a745" />,
			description:
				'Forneça suas credenciais de API para começar a utilizar os serviços oferecidos. Essas credenciais são essenciais para autenticação e acesso aos recursos necessários.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Informações Gerais',
			icon: <FaInfoCircle size={60} color="#17a2b8" />,
			description:
				'Preencha os detalhes básicos do seu projeto, como nome do pacote e entidade principal. Essas informações são fundamentais para a organização e estruturação correta da sua API.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Atributos JPA',
			icon: <FaCogs size={60} color="#ffc107" />,
			description:
				'Defina os atributos da sua entidade JPA, incluindo tipos, chaves primárias e outras propriedades essenciais. Isso garantirá que sua API esteja bem estruturada e funcional.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Regras de Negócio',
			icon: <FaGavel size={60} color="#dc3545" />,
			description:
				'Adicione e gerencie regras de negócio que irão orientar o comportamento da sua API de acordo com as necessidades do seu projeto. Regras bem definidas são cruciais para o funcionamento correto da API.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Templates Personalizados',
			icon: <FaFileAlt size={60} color="#6f42c1" />,
			description:
				'Personalize os templates utilizados pela sua API para refletir a identidade e os requisitos específicos do seu projeto. Templates customizados aumentam a eficiência e a qualidade do seu desenvolvimento.',
			buttonText: 'Próximo',
			buttonVariant: 'primary',
			onClick: () => setIndex(index + 1),
		},
		{
			title: 'Revisão e Envio',
			icon: <FaCheckCircle size={60} color="#28a745" />,
			description:
				'Revise todas as informações inseridas para garantir que tudo está correto antes de enviar seu formulário. Uma revisão cuidadosa evita erros e garante que sua API seja criada conforme o esperado.',
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
										{slide.description}
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

// src/ApiForm.js
import React, { useState, useEffect, useCallback } from 'react';
import {
	Form,
	Button,
	ProgressBar,
	Alert,
	Spinner,
	Modal,
	Nav,
	Toast,
	ToastContainer,
	InputGroup,
	Row,
	Col,
} from 'react-bootstrap';
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai';
import {
	saveForm,
	getAllForms,
	getFormById,
	updateForm,
	deleteForm,
} from './db';
import IntroductionGuide from './IntroductionGuide'; // Ajuste o caminho conforme necessário
import Chat from './Chat'; // Importar o componente de Chat

const initialFormData = {
	credentials: {
		openAiApiKey: '',
		openAiBaseUrl: '',
		chatOptionsModel: '',
		chatOptionsTemperature: 0,
	},
	entityDescriptor: {
		packageName: '',
		entityName: '',
		systemPrompt: '',
		jpaDescriptor: {
			tableName: '',
			attributes: [],
		},
		rulesDescriptor: [],
	},
	templates: [],
	// Adicione outros campos conforme necessário
};

const ApiForm = () => {
	const [formData, setFormData] = useState(initialFormData);
	const [currentStep, setCurrentStep] = useState(1);
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [isFetching, setIsFetching] = useState(false);
	const [errors, setErrors] = useState({});
	const [showModal, setShowModal] = useState(false);
	const [savedForms, setSavedForms] = useState([]);
	const [currentFormId, setCurrentFormId] = useState(null);
	const [toastMessage, setToastMessage] = useState(null);
	const [showApiKey, setShowApiKey] = useState(false);
	const [showGuide, setShowGuide] = useState(true); // Inicializado como true para exibir o guia
	const totalSteps = 6; // Ajustado para refletir apenas as etapas do formulário

	useEffect(() => {
		const fetchInitialData = async () => {
			setIsFetching(true);
			try {
				const response = await fetch('https://mighty-nearly-minnow.ngrok-free.app/', {
					headers: {
						'ngrok-skip-browser-warning': 'true',
					},
				});
				if (!response.ok) throw new Error('Failed to fetch data');
				const data = await response.json();

				console.log('Dados recebidos da API:', data); // Verifique os dados recebidos

				// Mesclar os dados recebidos com os valores iniciais
				setFormData((prevData) => ({
					...initialFormData,
					...data,
					credentials: {
						...initialFormData.credentials,
						...data.credentials,
					},
					entityDescriptor: {
						...initialFormData.entityDescriptor,
						...data.entityDescriptor,
						jpaDescriptor: {
							...initialFormData.entityDescriptor.jpaDescriptor,
							...data.entityDescriptor.jpaDescriptor,
						},
						// Mescle outros subcampos conforme necessário
					},
					// Mescle outros campos conforme necessário
				}));
			} catch (error) {
				console.error('Error fetching data:', error);
			} finally {
				setIsFetching(false);
			}
		};

		const fetchSavedForms = async () => {
			const forms = await getAllForms();
			setSavedForms(forms);
		};

		// Verificar se o usuário optou por pular a introdução anteriormente
		const shouldSkipGuide = localStorage.getItem('skipGuide') === 'true';
		setShowGuide(!shouldSkipGuide);
		if (shouldSkipGuide) {
			setCurrentStep(1); // Inicia o formulário diretamente
		}

		fetchInitialData();
		fetchSavedForms();
	}, []);

	// Função para finalizar o guia de introdução
	const handleGuideFinish = () => {
		console.log('Guia finalizado');
		setShowGuide(false);
		setCurrentStep(1); // Inicia a contagem das etapas do formulário
		localStorage.setItem('skipGuide', 'true'); // Salva que o guia foi finalizado
	};

	// Função para pular o guia de introdução
	const handleGuideSkip = () => {
		console.log('Guia pulado pelo usuário');
		setShowGuide(false);
		setCurrentStep(1); // Inicia a contagem das etapas do formulário
		localStorage.setItem('skipGuide', 'true'); // Salva a preferência
	};

	// Funções de validação, manipulação de dados e navegação

	const validateStep = useCallback(() => {
		if (!formData) return false;
		let stepErrors = {};

		if (currentStep === 1) {
			// Passo 1: Credenciais
			const { openAiApiKey, openAiBaseUrl, chatOptionsModel, chatOptionsTemperature } = formData.credentials;
			if (!openAiApiKey || openAiApiKey.trim() === '') {
				stepErrors.openAiApiKey = 'A OpenAI API Key é obrigatória.';
			}
			if (!openAiBaseUrl || openAiBaseUrl.trim() === '') {
				stepErrors.openAiBaseUrl = 'A OpenAI Base URL é obrigatória.';
			}
			if (!chatOptionsModel || chatOptionsModel.trim() === '') {
				stepErrors.chatOptionsModel = 'O Chat Options Model é obrigatório.';
			}
			if (
				chatOptionsTemperature === null ||
				chatOptionsTemperature === undefined ||
				isNaN(chatOptionsTemperature)
			) {
				stepErrors.chatOptionsTemperature = 'A Chat Options Temperature é obrigatória e deve ser um número.';
			}
		} else if (currentStep === 2) {
			// Passo 2: Informações Gerais
			if (!formData.entityDescriptor.packageName || formData.entityDescriptor.packageName.trim() === '') {
				stepErrors.packageName = 'O nome do pacote é obrigatório.';
			}
			if (!formData.entityDescriptor.entityName || formData.entityDescriptor.entityName.trim() === '') {
				stepErrors.entityName = 'O nome da entidade é obrigatório.';
			}
			if (!formData.entityDescriptor.systemPrompt || formData.entityDescriptor.systemPrompt.trim() === '') {
				stepErrors.systemPrompt = 'O prompt do sistema é obrigatório.';
			}
		} else if (currentStep === 3) {
			// Passo 3: Atributos JPA
			if (!formData.entityDescriptor.jpaDescriptor.tableName || formData.entityDescriptor.jpaDescriptor.tableName.trim() === '') {
				stepErrors.tableName = 'O nome da tabela é obrigatória.';
			}
			if (formData.entityDescriptor.jpaDescriptor.attributes.length === 0) {
				stepErrors.attributes = 'Adicione pelo menos um atributo.';
			} else {
				formData.entityDescriptor.jpaDescriptor.attributes.forEach((attr, index) => {
					if (!attr.name || !attr.type) {
						stepErrors[`attribute_${index}`] = 'Nome e tipo são obrigatórios.';
					}
				});
			}
		} else if (currentStep === 4) {
			// Passo 4: Regras de Negócio
			if (formData.entityDescriptor.rulesDescriptor.length === 0) {
				stepErrors.rules = 'Adicione pelo menos uma regra.';
			} else {
				formData.entityDescriptor.rulesDescriptor.forEach((rule, index) => {
					const ruleNameText = rule.ruleName.trim();
					const ruleInputText = rule.ruleInput.trim();
					if (!ruleNameText || !rule.description || !ruleInputText) {
						stepErrors[`rule_${index}`] = 'Nome, descrição e input da regra são obrigatórios.';
					}
				});
			}
		} else if (currentStep === 5) {
			// Passo 5: Templates
			if (formData.templates.length === 0) {
				stepErrors.templates = 'Não há templates disponíveis para editar.';
			}
		} else if (currentStep === 6) {
			// Passo 6: Revisão e Envio (geralmente não requer validação)
		}

		setErrors(stepErrors);
		console.log('Erros de validação:', stepErrors);
		return Object.keys(stepErrors).length === 0;
	}, [currentStep, formData]);

	const handleInputChange = useCallback((e) => {
		const { name, value } = e.target;
		setErrors((prevErrors) => ({ ...prevErrors, [name]: null }));

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			if (name.startsWith('credentials.')) {
				const credentialName = name.split('.')[1];
				updatedFormData.credentials[credentialName] = value;
			} else if (name === 'tableName') {
				updatedFormData.entityDescriptor.jpaDescriptor.tableName = value;
			} else {
				updatedFormData.entityDescriptor[name] = value;
			}
			return updatedFormData;
		});
	}, []);

	const handleAddAttribute = useCallback(() => {
		if (!formData) return;

		const initializeAttribute = () => ({
			name: '',
			type: '',
			primaryKey: false,
			required: false,
			maxLength: null,
			generatedValue: false,
			columnDefinition: ''
		});
		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.entityDescriptor.jpaDescriptor.attributes.push(initializeAttribute());
			return updatedFormData;
		});
	}, [formData]);

	const handleRemoveAttribute = useCallback((index) => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.entityDescriptor.jpaDescriptor.attributes = updatedFormData.entityDescriptor.jpaDescriptor.attributes.filter((_, i) => i !== index);
			return updatedFormData;
		});
	}, [formData]);

	const handleAddRule = useCallback(() => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.entityDescriptor.rulesDescriptor.push({
				ruleName: '',
				description: '',
				ruleInput: '',
				ruleAdditionalInput: '',
				ruleOutput: '',
				llmGeneratedLogic: '',
				javaFunctionalInterface: '',
				javaFuncionalIntefaceMethodName: ''
			});
			return updatedFormData;
		});
	}, [formData]);

	const handleRemoveRule = useCallback((index) => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.entityDescriptor.rulesDescriptor = updatedFormData.entityDescriptor.rulesDescriptor.filter((_, i) => i !== index);
			return updatedFormData;
		});
	}, [formData]);

	// Definição das funções handleAddTemplate e handleRemoveTemplate
	const handleAddTemplate = useCallback(() => {
		if (!formData) return;

		const initializeTemplate = () => ({
			name: '',
			content: ''
		});

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.templates.push(initializeTemplate());
			return updatedFormData;
		});
	}, [formData]);

	const handleRemoveTemplate = useCallback((index) => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.templates = updatedFormData.templates.filter((_, i) => i !== index);
			return updatedFormData;
		});
	}, [formData]);

	const handleNextStep = useCallback(() => {
		if (validateStep()) {
			setCurrentStep((prevStep) => prevStep + 1);
			console.log(`Avançando para o passo ${currentStep + 1}`);
		}
	}, [validateStep, currentStep]);

	const handlePreviousStep = useCallback(() => {
		setCurrentStep((prevStep) => prevStep - 1);
		console.log(`Voltando para o passo ${currentStep - 1}`);
	}, [currentStep]);

	const handleRuleInputChange = useCallback(
		(index, field, value) => {
			if (!formData) return;

			setFormData((prevFormData) => {
				const updatedFormData = { ...prevFormData };
				updatedFormData.entityDescriptor.rulesDescriptor[index][field] = value;
				return updatedFormData;
			});

			setErrors((prevErrors) => ({ ...prevErrors, [`rule_${index}`]: null }));
		},
		[formData]
	);

	const handleJpaAttributeChange = useCallback((index, field, value) => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };

			const updateAttributes = (attributes) =>
				attributes.map((attr, i) => {
					if (i === index) {
						const updatedAttr = { ...attr, [field]: value };
						if (field === 'primaryKey' && !value) {
							updatedAttr.generatedValue = false;
						}
						return updatedAttr;
					}
					return attr;
				});

			updatedFormData.entityDescriptor.jpaDescriptor.attributes = updateAttributes(updatedFormData.entityDescriptor.jpaDescriptor.attributes);

			return updatedFormData;
		});

		setErrors((prevErrors) => ({ ...prevErrors, [`attribute_${index}`]: null }));
	}, [formData]);

	// Funções para manipular os templates
	const handleTemplateChange = useCallback((index, field, value) => {
		if (!formData) return;

		setFormData((prevFormData) => {
			const updatedFormData = { ...prevFormData };
			updatedFormData.templates[index][field] = value;
			return updatedFormData;
		});
	}, [formData]);

	// Função para ir para um passo específico
	const goToStep = useCallback((step) => {
		setCurrentStep(step);
		console.log(`Navegando para o passo ${step}`);
	}, []);

	// Funções para salvar, carregar e excluir formulários
	const handleSaveForm = async () => {
		const formName = prompt(
			'Digite um nome para salvar este formulário:',
			formData.entityDescriptor.entityName || 'Novo Formulário'
		);

		if (formName) {
			const formToSave = {
				name: formName,
				data: formData,
				currentStep,
			};

			if (currentFormId) {
				await updateForm(currentFormId, formToSave);
			} else {
				const id = await saveForm(formToSave);
				setCurrentFormId(id);
			}

			const forms = await getAllForms();
			setSavedForms(forms);

			setToastMessage('Formulário salvo com sucesso!');
		}
	};

	const handleLoadForm = async (id) => {
		const form = await getFormById(id);
		if (form) {
			setFormData(form.data);
			setCurrentFormId(form.id);
			setCurrentStep(form.currentStep || 1);
			setToastMessage(`Formulário "${form.name}" carregado com sucesso!`);
		}
	};

	const handleDeleteForm = async (id) => {
		if (window.confirm('Tem certeza de que deseja excluir este formulário?')) {
			await deleteForm(id);
			const forms = await getAllForms();
			setSavedForms(forms);
			if (currentFormId === id) {
				setFormData(initialFormData); // Resetar para o estado inicial
				setCurrentFormId(null);
			}
			setToastMessage('Formulário excluído com sucesso!');
		}
	};

	// Função para enviar um formulário salvo
	const handleSubmitSavedForm = async (id) => {
		const form = await getFormById(id);
		if (form) {
			setIsSubmitting(true);
			try {
				// Preparar os headers com base nas credenciais
				const headers = {
					'Content-Type': 'application/json',
					'OPENAI-API-KEY': form.data.credentials.openAiApiKey,
					'OPENAI-BASE-URL': form.data.credentials.openAiBaseUrl,
					'CHAT-OPTIONS-MODEL': form.data.credentials.chatOptionsModel,
					'CHAT-OPTIONS-TEMPERATURE': form.data.credentials.chatOptionsTemperature,
				};

				// Enviar os dados para o servidor
				const response = await fetch('https://mighty-nearly-minnow.ngrok-free.app/', {
					method: 'POST',
					headers: headers,
					body: JSON.stringify(form.data),
				});

				if (!response.ok) throw new Error('Failed to submit form');

				// Processar a resposta
				const blob = await response.blob();
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.href = url;
				a.download = 'genhex4j.zip';
				document.body.appendChild(a);
				a.click();
				a.remove();
				window.URL.revokeObjectURL(url);

				setShowModal(true);
				setCurrentStep(1);
				setCurrentFormId(null);
				setFormData(initialFormData); // Resetar para o estado inicial

				setToastMessage(`Formulário "${form.name}" enviado com sucesso!`);
			} catch (error) {
				console.error('Error submitting form:', error);
				alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.');
			} finally {
				setIsSubmitting(false);
			}
		}
	};

	// Função para enviar o formulário atual
	const handleSubmit = useCallback(async () => {
		if (validateStep()) {
			setIsSubmitting(true);
			try {
				// Preparar os headers com base nas credenciais
				const headers = {
					'Content-Type': 'application/json',
					'OPENAI-API-KEY': formData.credentials.openAiApiKey,
					'OPENAI-BASE-URL': formData.credentials.openAiBaseUrl,
					'CHAT-OPTIONS-MODEL': formData.credentials.chatOptionsModel,
					'CHAT-OPTIONS-TEMPERATURE': formData.credentials.chatOptionsTemperature,
				};

				// Enviar os dados para o servidor
				const response = await fetch('https://mighty-nearly-minnow.ngrok-free.app/', {
					method: 'POST',
					headers: headers,
					body: JSON.stringify(formData),
				});

				if (!response.ok) throw new Error('Failed to submit form');

				// Processar a resposta
				const blob = await response.blob();
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.href = url;
				a.download = 'genhex4j.zip';
				document.body.appendChild(a);
				a.click();
				a.remove();
				window.URL.revokeObjectURL(url);

				setShowModal(true);
				setCurrentStep(1);
				setCurrentFormId(null);
				setFormData(initialFormData); // Resetar para o estado inicial
			} catch (error) {
				console.error('Error submitting form:', error);
				alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.');
			} finally {
				setIsSubmitting(false);
			}
		}
	}, [validateStep, formData]);

	if (isFetching) {
		return (
			<div className="text-center">
				<Spinner animation="border" variant="primary" />
				<p>Carregando dados...</p>
			</div>
		);
	}

	return (
		<div className="container my-5">
			<h1 className="text-center mb-4">Formulário de API</h1>

			<Row>
				{/* Coluna para o Formulário */}
				<Col md={8}>
					{/* Lista de formulários salvos */}
					<div className="saved-forms mb-4">
						<h3>Formulários Salvos</h3>
						{savedForms.length === 0 ? (
							<p>Nenhum formulário salvo.</p>
						) : (
							<ul className="list-group">
								{savedForms.map((form) => (
									<li
										key={form.id}
										className="list-group-item d-flex justify-content-between align-items-center"
									>
										<span>{form.name}</span>
										<div>
											<Button
												variant="success"
												size="sm"
												onClick={() => handleSubmitSavedForm(form.id)}
												disabled={isSubmitting}
											>
												{isSubmitting ? 'Enviando...' : 'Enviar'}
											</Button>{' '}
											<Button
												variant="primary"
												size="sm"
												onClick={() => handleLoadForm(form.id)}
											>
												Carregar
											</Button>{' '}
											<Button
												variant="danger"
												size="sm"
												onClick={() => handleDeleteForm(form.id)}
											>
												Excluir
											</Button>
										</div>
									</li>
								))}
							</ul>
						)}
					</div>

					<Form>
						<ProgressBar now={(currentStep / totalSteps) * 100} className="mb-4" />

						{/* Renderização Condicional do Guia ou do Formulário */}
						{showGuide ? (
							<IntroductionGuide onFinish={handleGuideFinish} onSkip={handleGuideSkip} />
						) : (
							<>
								{/* Passo 1: Credenciais */}
								{currentStep === 1 && formData.credentials && (
									<div>
										<h2>Credenciais da API</h2>
										<Form.Group controlId="openAiApiKey">
											<Form.Label>OpenAI API Key *</Form.Label>
											<InputGroup>
												<Form.Control
													type={showApiKey ? 'text' : 'password'}
													name="credentials.openAiApiKey"
													value={formData.credentials.openAiApiKey || ''}
													onChange={handleInputChange}
													isInvalid={!!errors.openAiApiKey}
												/>
												<Button
													variant="outline-secondary"
													onClick={() => setShowApiKey((prev) => !prev)}
													tabIndex={-1}
												>
													{showApiKey ? <AiFillEyeInvisible /> : <AiFillEye />}
												</Button>
												<Form.Control.Feedback type="invalid">
													{errors.openAiApiKey}
												</Form.Control.Feedback>
											</InputGroup>
										</Form.Group>

										<Form.Group controlId="openAiBaseUrl" className="mt-3">
											<Form.Label>OpenAI Base URL *</Form.Label>
											<Form.Control
												type="text"
												name="credentials.openAiBaseUrl"
												value={formData.credentials.openAiBaseUrl || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.openAiBaseUrl}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.openAiBaseUrl}
											</Form.Control.Feedback>
										</Form.Group>

										<Form.Group controlId="chatOptionsModel" className="mt-3">
											<Form.Label>Chat Options Model *</Form.Label>
											<Form.Control
												type="text"
												name="credentials.chatOptionsModel"
												value={formData.credentials.chatOptionsModel || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.chatOptionsModel}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.chatOptionsModel}
											</Form.Control.Feedback>
										</Form.Group>

										<Form.Group controlId="chatOptionsTemperature" className="mt-3">
											<Form.Label>Chat Options Temperature *</Form.Label>
											<Form.Control
												type="number"
												step="0.1"
												name="credentials.chatOptionsTemperature"
												value={formData.credentials.chatOptionsTemperature || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.chatOptionsTemperature}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.chatOptionsTemperature}
											</Form.Control.Feedback>
										</Form.Group>

										<div className="d-flex justify-content-between mt-4">
											<Button variant="secondary" onClick={handleSaveForm}>
												Salvar Formulário
											</Button>
											<Button variant="primary" onClick={handleNextStep}>
												Próximo
											</Button>
										</div>
									</div>
								)}

								{/* Passo 2: Informações Gerais */}
								{currentStep === 2 && (
									<div>
										<h2>Informações Gerais</h2>
										<Form.Group controlId="packageName">
											<Form.Label>Nome do Pacote *</Form.Label>
											<Form.Control
												type="text"
												name="packageName"
												value={formData.entityDescriptor.packageName || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.packageName}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.packageName}
											</Form.Control.Feedback>
										</Form.Group>
										<Form.Group controlId="entityName" className="mt-3">
											<Form.Label>Nome da Entidade *</Form.Label>
											<Form.Control
												type="text"
												name="entityName"
												value={formData.entityDescriptor.entityName || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.entityName}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.entityName}
											</Form.Control.Feedback>
										</Form.Group>
										<Form.Group controlId="systemPrompt" className="mt-3">
											<Form.Label>Prompt do Sistema *</Form.Label>
											<Form.Control
												as="textarea"
												name="systemPrompt"
												value={formData.entityDescriptor.systemPrompt || ''}
												onChange={(e) => {
													const value = e.target.value;
													setFormData((prevFormData) => ({
														...prevFormData,
														entityDescriptor: {
															...prevFormData.entityDescriptor,
															systemPrompt: value
														}
													}));
													setErrors((prevErrors) => ({ ...prevErrors, systemPrompt: null }));
												}}
												isInvalid={!!errors.systemPrompt}
											/>
											{errors.systemPrompt && (
												<Form.Control.Feedback type="invalid">
													{errors.systemPrompt}
												</Form.Control.Feedback>
											)}
										</Form.Group>
										<div className="d-flex justify-content-between mt-3">
											<Button variant="secondary" onClick={handleSaveForm}>
												Salvar Formulário
											</Button>
											<div>
												<Button variant="outline-secondary" onClick={handlePreviousStep} className="me-2">
													Voltar
												</Button>
												<Button variant="primary" onClick={handleNextStep}>
													Próximo
												</Button>
											</div>
										</div>
									</div>
								)}

								{/* Passo 3: Atributos JPA */}
								{currentStep === 3 && (
									<div>
										<h2>Atributos JPA</h2>
										<Form.Group controlId="tableName">
											<Form.Label>Nome da Tabela *</Form.Label>
											<Form.Control
												type="text"
												name="tableName"
												value={formData.entityDescriptor.jpaDescriptor.tableName || ''}
												onChange={handleInputChange}
												isInvalid={!!errors.tableName}
											/>
											<Form.Control.Feedback type="invalid">
												{errors.tableName}
											</Form.Control.Feedback>
										</Form.Group>
										{formData.entityDescriptor.jpaDescriptor.attributes.map((attr, index) => (
											<div key={index} className="border p-3 my-3">
												<h5>Atributo {index + 1}</h5>
												<Form.Group controlId={`attributeName_${index}`}>
													<Form.Label>Nome do Atributo *</Form.Label>
													<Form.Control
														type="text"
														value={attr.name}
														placeholder="Nome do Atributo"
														onChange={(e) => handleJpaAttributeChange(index, 'name', e.target.value)}
														isInvalid={!!errors[`attribute_${index}`]}
													/>
													{errors[`attribute_${index}`] && (
														<Form.Control.Feedback type="invalid">
															{errors[`attribute_${index}`]}
														</Form.Control.Feedback>
													)}
												</Form.Group>
												<Form.Group controlId={`attributeType_${index}`}>
													<Form.Label>Tipo do Atributo *</Form.Label>
													<Form.Control
														type="text"
														value={attr.type}
														placeholder="Tipo do Atributo"
														onChange={(e) => handleJpaAttributeChange(index, 'type', e.target.value)}
														isInvalid={!!errors[`attribute_${index}`]}
													/>
													<Form.Control.Feedback type="invalid">
														{errors[`attribute_${index}`]}
													</Form.Control.Feedback>
												</Form.Group>
												{/* Campos adicionais */}
												<Form.Group controlId={`primaryKey_${index}`}>
													<Form.Check
														type="checkbox"
														label="Chave Primária"
														checked={attr.primaryKey || false}
														onChange={(e) => handleJpaAttributeChange(index, 'primaryKey', e.target.checked)}
													/>
												</Form.Group>
												{attr.primaryKey && (
													<Form.Group controlId={`generatedValue_${index}`}>
														<Form.Check
															type="checkbox"
															label="Valor Gerado"
															checked={attr.generatedValue || false}
															onChange={(e) => handleJpaAttributeChange(index, 'generatedValue', e.target.checked)}
														/>
													</Form.Group>
												)}
												<Form.Group controlId={`required_${index}`}>
													<Form.Check
														type="checkbox"
														label="Obrigatório"
														checked={attr.required || false}
														onChange={(e) => handleJpaAttributeChange(index, 'required', e.target.checked)}
													/>
												</Form.Group>
												<Form.Group controlId={`maxLength_${index}`}>
													<Form.Label>Tamanho Máximo</Form.Label>
													<Form.Control
														type="number"
														value={attr.maxLength || ''}
														placeholder="Tamanho Máximo"
														onChange={(e) => handleJpaAttributeChange(index, 'maxLength', e.target.value ? parseInt(e.target.value) : null)}
													/>
												</Form.Group>
												<Form.Group controlId={`columnDefinition_${index}`}>
													<Form.Label>Definição da Coluna</Form.Label>
													<Form.Control
														type="text"
														value={attr.columnDefinition || ''}
														placeholder="Definição da Coluna"
														onChange={(e) => handleJpaAttributeChange(index, 'columnDefinition', e.target.value)}
													/>
												</Form.Group>
												<Button variant="danger" onClick={() => handleRemoveAttribute(index)} className="mt-2">
													Remover Atributo
												</Button>
											</div>
										))}
										{errors.attributes && (
											<Alert variant="danger">
												{errors.attributes}
											</Alert>
										)}
										<Button variant="success" onClick={handleAddAttribute}>
											Adicionar Atributo
										</Button>
										<div className="d-flex justify-content-between mt-3">
											<Button variant="secondary" onClick={handleSaveForm}>
												Salvar Formulário
											</Button>
											<div>
												<Button variant="outline-secondary" onClick={handlePreviousStep} className="me-2">
													Voltar
												</Button>
												<Button variant="primary" onClick={handleNextStep}>
													Próximo
												</Button>
											</div>
										</div>
									</div>
								)}

								{/* Passo 4: Regras de Negócio */}
								{currentStep === 4 && (
									<div>
										<h2>Regras de Negócio</h2>
										{formData.entityDescriptor.rulesDescriptor.map((rule, index) => (
											<div key={index} className="border p-3 my-3">
												<h5>Regra {index + 1}</h5>
												<Form.Group controlId={`ruleName_${index}`}>
													<Form.Label>Nome da Regra *</Form.Label>
													<Form.Control
														type="text"
														value={rule.ruleName}
														placeholder="Nome da Regra"
														onChange={(e) => handleRuleInputChange(index, 'ruleName', e.target.value)}
														isInvalid={!!errors[`rule_${index}`]}
													/>
													{errors[`rule_${index}`] && (
														<Form.Control.Feedback type="invalid">
															{errors[`rule_${index}`]}
														</Form.Control.Feedback>
													)}
												</Form.Group>
												<Form.Group controlId={`ruleDescription_${index}`} className="mt-3">
													<Form.Label>Descrição *</Form.Label>
													<Form.Control
														type="text"
														value={rule.description}
														placeholder="Descrição"
														onChange={(e) => handleRuleInputChange(index, 'description', e.target.value)}
														isInvalid={!!errors[`rule_${index}`]}
													/>
													<Form.Control.Feedback type="invalid">
														{errors[`rule_${index}`]}
													</Form.Control.Feedback>
												</Form.Group>
												<Form.Group controlId={`ruleInput_${index}`} className="mt-3">
													<Form.Label>Input da Regra *</Form.Label>
													<Form.Control
														type="text"
														value={rule.ruleInput}
														placeholder="Input da Regra"
														onChange={(e) => handleRuleInputChange(index, 'ruleInput', e.target.value)}
														isInvalid={!!errors[`rule_${index}`]}
													/>
													<Form.Control.Feedback type="invalid">
														{errors[`rule_${index}`]}
													</Form.Control.Feedback>
												</Form.Group>
												<Form.Group controlId={`ruleOutput_${index}`} className="mt-3">
													<Form.Label>Output da Regra</Form.Label>
													<Form.Control
														type="text"
														value={rule.ruleOutput}
														placeholder="Output da Regra"
														onChange={(e) => handleRuleInputChange(index, 'ruleOutput', e.target.value)}
													/>
												</Form.Group>
												<Form.Group controlId={`llmGeneratedLogic_${index}`} className="mt-3">
													<Form.Label>Lógica Gerada por LLM</Form.Label>
													<Form.Control
														as="textarea"
														rows={3}
														value={rule.llmGeneratedLogic}
														placeholder="Lógica Gerada por LLM"
														onChange={(e) => handleRuleInputChange(index, 'llmGeneratedLogic', e.target.value)}
													/>
												</Form.Group>
												<Form.Group controlId={`javaFunctionalInterface_${index}`} className="mt-3">
													<Form.Label>Interface Funcional Java</Form.Label>
													<Form.Control
														type="text"
														value={rule.javaFunctionalInterface}
														placeholder="Interface Funcional Java"
														onChange={(e) => handleRuleInputChange(index, 'javaFunctionalInterface', e.target.value)}
													/>
												</Form.Group>
												<Form.Group controlId={`javaFuncionalIntefaceMethodName_${index}`} className="mt-3">
													<Form.Label>Nome do Método da Interface</Form.Label>
													<Form.Control
														type="text"
														value={rule.javaFuncionalIntefaceMethodName}
														placeholder="Nome do Método da Interface"
														onChange={(e) => handleRuleInputChange(index, 'javaFuncionalIntefaceMethodName', e.target.value)}
													/>
												</Form.Group>
												<Button variant="danger" onClick={() => handleRemoveRule(index)} className="mt-2">
													Remover Regra
												</Button>
											</div>
										))}
										{errors.rules && (
											<Alert variant="danger">
												{errors.rules}
											</Alert>
										)}
										<Button variant="success" onClick={handleAddRule}>
											Adicionar Regra
										</Button>
										<div className="d-flex justify-content-between mt-3">
											<Button variant="secondary" onClick={handleSaveForm}>
												Salvar Formulário
											</Button>
											<div>
												<Button variant="outline-secondary" onClick={handlePreviousStep} className="me-2">
													Voltar
												</Button>
												<Button variant="primary" onClick={handleNextStep}>
													Próximo
												</Button>
											</div>
										</div>
									</div>
								)}

								{/* Passo 5: Templates */}
								{currentStep === 5 && (
									<div>
										<h2>Templates</h2>
										{formData.templates.length === 0 ? (
											<p>Não há templates disponíveis para edição.</p>
										) : (
											formData.templates.map((template, index) => (
												<div key={index} className="border p-3 my-3">
													<h5>Template {index + 1}</h5>
													<Form.Group controlId={`templateName_${index}`}>
														<Form.Label>Nome do Template *</Form.Label>
														<Form.Control
															type="text"
															value={template.name}
															placeholder="Nome do Template"
															onChange={(e) => handleTemplateChange(index, 'name', e.target.value)}
															isInvalid={!!errors[`template_${index}`]}
														/>
														<Form.Control.Feedback type="invalid">
															{errors[`template_${index}`]}
														</Form.Control.Feedback>
													</Form.Group>
													<Form.Group controlId={`templateContent_${index}`} className="mt-3">
														<Form.Label>Conteúdo do Template *</Form.Label>
														<Form.Control
															as="textarea"
															rows={10}
															value={template.content}
															placeholder="Conteúdo do Template"
															onChange={(e) => handleTemplateChange(index, 'content', e.target.value)}
															isInvalid={!!errors[`template_${index}`]}
														/>
														<Form.Control.Feedback type="invalid">
															{errors[`template_${index}`]}
														</Form.Control.Feedback>
													</Form.Group>
													<Button variant="danger" onClick={() => handleRemoveTemplate(index)} className="mt-2">
														Remover Template
													</Button>
												</div>
											))
										)}
										{errors.templates && (
											<Alert variant="danger">
												{errors.templates}
											</Alert>
										)}
										<Button variant="success" onClick={handleAddTemplate}>
											Adicionar Template
										</Button>
										<div className="d-flex justify-content-between mt-3">
											<Button variant="secondary" onClick={handleSaveForm}>
												Salvar Formulário
											</Button>
											<div>
												<Button variant="outline-secondary" onClick={handlePreviousStep} className="me-2">
													Voltar
												</Button>
												<Button variant="primary" onClick={handleNextStep}>
													Próximo
												</Button>
											</div>
										</div>
									</div>
								)}

								{/* Passo 6: Revisão e Envio */}
								{currentStep === 6 && (
									<div>
										<h2>Revisão e Envio</h2>
										<p>Revise todas as informações inseridas antes de enviar.</p>

										{/* Navegação entre as etapas */}
										<Nav className="flex-column mb-3">
											<Nav.Link onClick={() => goToStep(1)}>Editar Credenciais</Nav.Link>
											<Nav.Link onClick={() => goToStep(2)}>Editar Informações Gerais</Nav.Link>
											<Nav.Link onClick={() => goToStep(3)}>Editar Atributos JPA</Nav.Link>
											<Nav.Link onClick={() => goToStep(4)}>Editar Regras</Nav.Link>
											<Nav.Link onClick={() => goToStep(5)}>Editar Templates</Nav.Link>
										</Nav>

										{/* Resumo dos dados */}
										<div>
											<h4>Credenciais da API</h4>
											<p><strong>OpenAI API Key:</strong> {formData.credentials.openAiApiKey}</p>
											<p><strong>OpenAI Base URL:</strong> {formData.credentials.openAiBaseUrl}</p>
											<p><strong>Chat Options Model:</strong> {formData.credentials.chatOptionsModel}</p>
											<p><strong>Chat Options Temperature:</strong> {formData.credentials.chatOptionsTemperature}</p>

											<h4>Informações Gerais</h4>
											<p><strong>Nome do Pacote:</strong> {formData.entityDescriptor.packageName}</p>
											<p><strong>Nome da Entidade:</strong> {formData.entityDescriptor.entityName}</p>
											<p><strong>Prompt do Sistema:</strong> {formData.entityDescriptor.systemPrompt}</p>

											<h4>Atributos JPA</h4>
											<p><strong>Nome da Tabela:</strong> {formData.entityDescriptor.jpaDescriptor.tableName}</p>
											{formData.entityDescriptor.jpaDescriptor.attributes.map((attr, index) => (
												<div key={index}>
													<p><strong>Atributo {index + 1}:</strong></p>
													<ul>
														<li><strong>Nome:</strong> {attr.name}</li>
														<li><strong>Tipo:</strong> {attr.type}</li>
														<li><strong>Chave Primária:</strong> {attr.primaryKey ? 'Sim' : 'Não'}</li>
														<li><strong>Valor Gerado:</strong> {attr.generatedValue ? 'Sim' : 'Não'}</li>
														<li><strong>Obrigatório:</strong> {attr.required ? 'Sim' : 'Não'}</li>
														<li><strong>Tamanho Máximo:</strong> {attr.maxLength || 'N/A'}</li>
														<li><strong>Definição da Coluna:</strong> {attr.columnDefinition || 'N/A'}</li>
													</ul>
												</div>
											))}

											<h4>Regras de Negócio</h4>
											{formData.entityDescriptor.rulesDescriptor.map((rule, index) => (
												<div key={index}>
													<p><strong>Regra {index + 1}:</strong></p>
													<ul>
														<li><strong>Nome:</strong> {rule.ruleName}</li>
														<li><strong>Descrição:</strong> {rule.description}</li>
														<li><strong>Input da Regra:</strong> {rule.ruleInput}</li>
														<li><strong>Output da Regra:</strong> {rule.ruleOutput}</li>
														{/* Outros campos da regra */}
													</ul>
												</div>
											))}

											<h4>Templates</h4>
											{formData.templates.map((template, index) => (
												<div key={index}>
													<p><strong>Template {index + 1}:</strong></p>
													<ul>
														<li><strong>Nome:</strong> {template.name}</li>
														<li><strong>Conteúdo:</strong></li>
														<pre>{template.content}</pre>
													</ul>
												</div>
											))}
										</div>

										<div className="d-flex justify-content-between mt-3">
											<Button variant="secondary" onClick={handlePreviousStep}>
												Voltar
											</Button>
											<div>
												<Button variant="secondary" onClick={handleSaveForm}>
													Salvar Formulário
												</Button>{' '}
												<Button variant="success" onClick={handleSubmit} disabled={isSubmitting}>
													{isSubmitting ? 'Enviando...' : 'Enviar'}
												</Button>
											</div>
										</div>
									</div>
								)}
							</>
						)}
					</Form>
				</Col>

				{/* Coluna para o Chat */}
				<Col md={4}>
					{/* Componente de Chat */}
					{!showGuide && (
						<Chat
							apiKey={formData.credentials.openAiApiKey}
							baseUrl={formData.credentials.openAiBaseUrl}
							model={formData.credentials.chatOptionsModel}
							temperature={formData.credentials.chatOptionsTemperature}
							entityName={formData.entityDescriptor.entityName}
							packageName={formData.entityDescriptor.packageName}
							tableName={formData.entityDescriptor.jpaDescriptor.tableName}
							templates={formData.templates}
						/>
					)}
				</Col>
			</Row>

			{/* Toast para feedback ao usuário */}
			<ToastContainer position="top-end">
				<Toast
					onClose={() => setToastMessage(null)}
					show={!!toastMessage}
					delay={3000}
					autohide
				>
					<Toast.Body>{toastMessage}</Toast.Body>
				</Toast>
			</ToastContainer>

			{/* Modal de sucesso (após submissão) */}
			<Modal show={showModal} onHide={() => setShowModal(false)}>
				<Modal.Header closeButton>
					<Modal.Title>Sucesso</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<p>Formulário enviado com sucesso! O download do arquivo iniciará em breve.</p>
				</Modal.Body>
				<Modal.Footer>
					<Button variant="primary" onClick={() => setShowModal(false)}>
						Fechar
					</Button>
				</Modal.Footer>
			</Modal>
		</div>
	);
};

export default ApiForm;

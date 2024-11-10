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
} from 'react-bootstrap';
import debounce from 'lodash.debounce';
import {
  saveForm,
  getAllForms,
  getFormById,
  updateForm,
  deleteForm,
} from './db';

const ApiForm = () => {
  const [formData, setFormData] = useState(null);
  const [currentStep, setCurrentStep] = useState(1);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isFetching, setIsFetching] = useState(false);
  const [errors, setErrors] = useState({});
  const [showModal, setShowModal] = useState(false);
  const [savedForms, setSavedForms] = useState([]);
  const [currentFormId, setCurrentFormId] = useState(null);
  const [toastMessage, setToastMessage] = useState(null);
  const totalSteps = 5;

  useEffect(() => {
    const fetchInitialData = async () => {
      setIsFetching(true);
      try {
        const response = await fetch('http://localhost:8080/');
        if (!response.ok) throw new Error('Failed to fetch data');
        const data = await response.json();

        setFormData(data);
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

    fetchInitialData();
    fetchSavedForms();
  }, []);

  const validateStep = useCallback(() => {
    if (!formData) return false;
    let stepErrors = {};
    if (currentStep === 1) {
      const systemPromptText = formData.entityDescriptor.systemPrompt.trim();

      if (!formData.entityDescriptor.packageName) {
        stepErrors.packageName = 'O nome do pacote é obrigatório.';
      }
      if (!formData.entityDescriptor.entityName) {
        stepErrors.entityName = 'O nome da entidade é obrigatório.';
      }
      if (!systemPromptText) {
        stepErrors.systemPrompt = 'O prompt do sistema é obrigatório.';
      }
    } else if (currentStep === 2) {
      if (!formData.entityDescriptor.jpaDescriptor.tableName) {
        stepErrors.tableName = 'O nome da tabela é obrigatório.';
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
    } else if (currentStep === 3) {
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
    } else if (currentStep === 4) {
      if (formData.templates.length === 0) {
        stepErrors.templates = 'Não há templates disponíveis para editar.';
      }
    }
    setErrors(stepErrors);
    return Object.keys(stepErrors).length === 0;
  }, [currentStep, formData]);

  const handleInputChange = useCallback((e) => {
    const { name, value } = e.target;
    setErrors((prevErrors) => ({ ...prevErrors, [name]: null }));

    setFormData((prevFormData) => {
      const updatedFormData = { ...prevFormData };
      if (name === 'tableName') {
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
      updatedFormData.entityDescriptor.domainDescriptor.attributes.push(initializeAttribute());
      updatedFormData.entityDescriptor.dtoDescriptor.attributes.push(initializeAttribute());
      return updatedFormData;
    });
  }, [formData]);

  const handleRemoveAttribute = useCallback((index) => {
    if (!formData) return;

    setFormData((prevFormData) => {
      const updatedFormData = { ...prevFormData };
      const removeAt = (arr) => arr.filter((_, i) => i !== index);
      updatedFormData.entityDescriptor.jpaDescriptor.attributes = removeAt(updatedFormData.entityDescriptor.jpaDescriptor.attributes);
      updatedFormData.entityDescriptor.domainDescriptor.attributes = removeAt(updatedFormData.entityDescriptor.domainDescriptor.attributes);
      updatedFormData.entityDescriptor.dtoDescriptor.attributes = removeAt(updatedFormData.entityDescriptor.dtoDescriptor.attributes);
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

  const handleNextStep = useCallback(() => {
    if (validateStep()) {
      setCurrentStep((prevStep) => prevStep + 1);
    }
  }, [validateStep]);

  const handlePreviousStep = useCallback(() => {
    setCurrentStep((prevStep) => prevStep - 1);
  }, []);

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
      updatedFormData.entityDescriptor.domainDescriptor.attributes = updateAttributes(updatedFormData.entityDescriptor.domainDescriptor.attributes);
      updatedFormData.entityDescriptor.dtoDescriptor.attributes = updateAttributes(updatedFormData.entityDescriptor.dtoDescriptor.attributes);

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
        setFormData(null);
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
        // Enviar os dados para o servidor
        const response = await fetch('http://localhost:8080/', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
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
        setFormData(null);

        setToastMessage(`Formulário "${form.name}" enviado com sucesso!`);
      } catch (error) {
        console.error('Error submitting form:', error);
        alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.');
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const handleSubmit = useCallback(async () => {
    if (validateStep()) {
      setIsSubmitting(true);
      try {
        // Enviar os dados para o servidor sem sanitização
        const response = await fetch('http://localhost:8080/', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formData)
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
        setFormData(null);
      } catch (error) {
        console.error('Error submitting form:', error);
        alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.');
      } finally {
        setIsSubmitting(false);
      }
    }
  }, [validateStep, formData]);

  if (isFetching || !formData) {
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
        {currentStep === 1 && (
          <div>
            <h2>Informações Gerais</h2>
            <Form.Group controlId="packageName">
              <Form.Label>Nome do Pacote *</Form.Label>
              <Form.Control
                type="text"
                name="packageName"
                value={formData.entityDescriptor.packageName}
                onChange={handleInputChange}
                isInvalid={!!errors.packageName}
              />
              <Form.Control.Feedback type="invalid">
                {errors.packageName}
              </Form.Control.Feedback>
            </Form.Group>
            <Form.Group controlId="entityName">
              <Form.Label>Nome da Entidade *</Form.Label>
              <Form.Control
                type="text"
                name="entityName"
                value={formData.entityDescriptor.entityName}
                onChange={handleInputChange}
                isInvalid={!!errors.entityName}
              />
              <Form.Control.Feedback type="invalid">
                {errors.entityName}
              </Form.Control.Feedback>
            </Form.Group>
            <Form.Group controlId="systemPrompt">
              <Form.Label>Prompt do Sistema *</Form.Label>
              <Form.Control
                as="textarea"
                name="systemPrompt"
                value={formData.entityDescriptor.systemPrompt}
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
              <Button variant="primary" onClick={handleNextStep}>
                Próximo
              </Button>
            </div>
          </div>
        )}
        {currentStep === 2 && (
          <div>
            <h2>Atributos JPA</h2>
            <Form.Group controlId="tableName">
              <Form.Label>Nome da Tabela *</Form.Label>
              <Form.Control
                type="text"
                name="tableName"
                value={formData.entityDescriptor.jpaDescriptor.tableName}
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
                <Button variant="danger" onClick={() => handleRemoveAttribute(index)}>
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
              <Button variant="secondary" onClick={handlePreviousStep}>
                Voltar
              </Button>
              <div>
                <Button variant="secondary" onClick={handleSaveForm}>
                  Salvar Formulário
                </Button>{' '}
                <Button variant="primary" onClick={handleNextStep}>
                  Próximo
                </Button>
              </div>
            </div>
          </div>
        )}
        {currentStep === 3 && (
          <div>
            <h2>Regras</h2>
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
                <Form.Group controlId={`ruleDescription_${index}`}>
                  <Form.Label>Descrição *</Form.Label>
                  <Form.Control
                    type="text"
                    value={rule.description}
                    placeholder="Descrição"
                    onChange={(e) => handleRuleInputChange(index, 'description', e.target.value)}
                    isInvalid={!!errors[`rule_${index}`]}
                  />
                </Form.Group>
                <Form.Group controlId={`ruleInput_${index}`}>
                  <Form.Label>Input da Regra *</Form.Label>
                  <Form.Control
                    type="text"
                    value={rule.ruleInput}
                    placeholder="Input da Regra"
                    onChange={(e) => handleRuleInputChange(index, 'ruleInput', e.target.value)}
                  />
                </Form.Group>
                <Form.Group controlId={`ruleOutput_${index}`}>
                  <Form.Label>Output da Regra</Form.Label>
                  <Form.Control
                    type="text"
                    value={rule.ruleOutput}
                    placeholder="Output da Regra"
                    onChange={(e) => handleRuleInputChange(index, 'ruleOutput', e.target.value)}
                  />
                </Form.Group>
                <Form.Group controlId={`llmGeneratedLogic_${index}`}>
                  <Form.Label>Lógica Gerada por LLM</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={rule.llmGeneratedLogic}
                    placeholder="Lógica Gerada por LLM"
                    onChange={(e) => handleRuleInputChange(index, 'llmGeneratedLogic', e.target.value)}
                  />
                </Form.Group>
                <Form.Group controlId={`javaFunctionalInterface_${index}`}>
                  <Form.Label>Interface Funcional Java</Form.Label>
                  <Form.Control
                    type="text"
                    value={rule.javaFunctionalInterface}
                    placeholder="Interface Funcional Java"
                    onChange={(e) => handleRuleInputChange(index, 'javaFunctionalInterface', e.target.value)}
                  />
                </Form.Group>
                <Form.Group controlId={`javaFuncionalIntefaceMethodName_${index}`}>
                  <Form.Label>Nome do Método da Interface</Form.Label>
                  <Form.Control
                    type="text"
                    value={rule.javaFuncionalIntefaceMethodName}
                    placeholder="Nome do Método da Interface"
                    onChange={(e) => handleRuleInputChange(index, 'javaFuncionalIntefaceMethodName', e.target.value)}
                  />
                </Form.Group>
                <Button variant="danger" onClick={() => handleRemoveRule(index)}>
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
              <Button variant="secondary" onClick={handlePreviousStep}>
                Voltar
              </Button>
              <div>
                <Button variant="secondary" onClick={handleSaveForm}>
                  Salvar Formulário
                </Button>{' '}
                <Button variant="primary" onClick={handleNextStep}>
                  Próximo
                </Button>
              </div>
            </div>
          </div>
        )}
        {currentStep === 4 && (
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
                    />
                  </Form.Group>
                  <Form.Group controlId={`templateContent_${index}`}>
                    <Form.Label>Conteúdo do Template *</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={10}
                      value={template.content}
                      placeholder="Conteúdo do Template"
                      onChange={(e) => handleTemplateChange(index, 'content', e.target.value)}
                    />
                  </Form.Group>
                  {/* Outros campos do template, se houver */}
                </div>
              ))
            )}
            {errors.templates && (
              <Alert variant="danger">
                {errors.templates}
              </Alert>
            )}
            <div className="d-flex justify-content-between mt-3">
              <Button variant="secondary" onClick={handlePreviousStep}>
                Voltar
              </Button>
              <div>
                <Button variant="secondary" onClick={handleSaveForm}>
                  Salvar Formulário
                </Button>{' '}
                <Button variant="primary" onClick={handleNextStep}>
                  Próximo
                </Button>
              </div>
            </div>
          </div>
        )}
        {currentStep === 5 && (
          <div>
            <h2>Revisão e Envio</h2>
            <p>Revise todas as informações inseridas antes de enviar.</p>

            {/* Navegação entre as etapas */}
            <Nav className="flex-column mb-3">
              <Nav.Link onClick={() => goToStep(1)}>Editar Informações Gerais</Nav.Link>
              <Nav.Link onClick={() => goToStep(2)}>Editar Atributos JPA</Nav.Link>
              <Nav.Link onClick={() => goToStep(3)}>Editar Regras</Nav.Link>
              <Nav.Link onClick={() => goToStep(4)}>Editar Templates</Nav.Link>
            </Nav>

            {/* Resumo dos dados */}
            <div>
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

              <h4>Regras</h4>
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
      </Form>

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

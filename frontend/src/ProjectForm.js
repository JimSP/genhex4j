import React, { useState, useEffect } from 'react';
import { Form, Button, ProgressBar, Alert, Spinner, Modal } from 'react-bootstrap';

const ApiForm = () => {
  const [formData, setFormData] = useState({
    entityDescriptor: {
      packageName: '',
      entityName: '',
      systemPrompt: '',
      jpaDescriptor: {
        tableName: '',
        attributes: []
      },
      domainDescriptor: {
        attributes: []
      },
      dtoDescriptor: {
        attributes: []
      },
      rulesDescriptor: []
    },
    standardTemplates: [],
    rulesTemplates: [],
    templates: []
  });

  const [currentStep, setCurrentStep] = useState(1);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isFetching, setIsFetching] = useState(false);
  const [errors, setErrors] = useState({});
  const [showModal, setShowModal] = useState(false);
  const totalSteps = 3;

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

    fetchInitialData();
  }, []);

  const validateStep = () => {
    let stepErrors = {};
    if (currentStep === 1) {
      if (!formData.entityDescriptor.packageName) {
        stepErrors.packageName = 'O nome do pacote é obrigatório.';
      }
      if (!formData.entityDescriptor.entityName) {
        stepErrors.entityName = 'O nome da entidade é obrigatório.';
      }
      if (!formData.entityDescriptor.systemPrompt) {
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
      // Validação das regras
      if (formData.entityDescriptor.rulesDescriptor.length === 0) {
        stepErrors.rules = 'Adicione pelo menos uma regra.';
      } else {
        formData.entityDescriptor.rulesDescriptor.forEach((rule, index) => {
          if (!rule.ruleName || !rule.description) {
            stepErrors[`rule_${index}`] = 'Nome e descrição da regra são obrigatórios.';
          }
        });
      }
    }
    setErrors(stepErrors);
    return Object.keys(stepErrors).length === 0;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setErrors((prevErrors) => ({ ...prevErrors, [name]: null }));
    if (name === 'tableName') {
      setFormData((prevFormData) => ({
        ...prevFormData,
        entityDescriptor: {
          ...prevFormData.entityDescriptor,
          jpaDescriptor: {
            ...prevFormData.entityDescriptor.jpaDescriptor,
            tableName: value
          }
        }
      }));
    } else {
      setFormData((prevFormData) => ({
        ...prevFormData,
        entityDescriptor: {
          ...prevFormData.entityDescriptor,
          [name]: value
        }
      }));
    }
  };

  const handleAddAttribute = () => {
    const initializeAttribute = () => ({ name: '', type: '' });
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        jpaDescriptor: {
          ...prevFormData.entityDescriptor.jpaDescriptor,
          attributes: [
            ...prevFormData.entityDescriptor.jpaDescriptor.attributes,
            initializeAttribute()
          ]
        },
        domainDescriptor: {
          ...prevFormData.entityDescriptor.domainDescriptor,
          attributes: [
            ...prevFormData.entityDescriptor.domainDescriptor.attributes,
            initializeAttribute()
          ]
        },
        dtoDescriptor: {
          ...prevFormData.entityDescriptor.dtoDescriptor,
          attributes: [
            ...prevFormData.entityDescriptor.dtoDescriptor.attributes,
            initializeAttribute()
          ]
        }
      }
    }));
  };

  const handleRemoveAttribute = (index) => {
    setFormData((prevFormData) => {
      const removeAt = (arr) => arr.filter((_, i) => i !== index);
      return {
        ...prevFormData,
        entityDescriptor: {
          ...prevFormData.entityDescriptor,
          jpaDescriptor: {
            ...prevFormData.entityDescriptor.jpaDescriptor,
            attributes: removeAt(prevFormData.entityDescriptor.jpaDescriptor.attributes)
          },
          domainDescriptor: {
            ...prevFormData.entityDescriptor.domainDescriptor,
            attributes: removeAt(prevFormData.entityDescriptor.domainDescriptor.attributes)
          },
          dtoDescriptor: {
            ...prevFormData.entityDescriptor.dtoDescriptor,
            attributes: removeAt(prevFormData.entityDescriptor.dtoDescriptor.attributes)
          }
        }
      };
    });
  };

  const handleAddRule = () => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        rulesDescriptor: [
          ...prevFormData.entityDescriptor.rulesDescriptor,
          {
            ruleName: '',
            description: '',
            ruleInput: '',
            ruleOutput: '',
            llmGeneratedLogic: '',
            javaFunctionalInterface: '',
            javaFuncionalIntefaceMethodName: ''
          }
        ]
      }
    }));
  };

  const handleRemoveRule = (index) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        rulesDescriptor: prevFormData.entityDescriptor.rulesDescriptor.filter((_, i) => i !== index)
      }
    }));
  };

  const handleNextStep = () => {
    if (validateStep()) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handlePreviousStep = () => {
    setCurrentStep(currentStep - 1);
  };

  const handleRuleInputChange = (index, field, value) => {
    const newRules = [...formData.entityDescriptor.rulesDescriptor];
    newRules[index][field] = value;
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        rulesDescriptor: newRules
      }
    }));

    setErrors((prevErrors) => ({ ...prevErrors, [`rule_${index}`]: null }));
  };

  const handleJpaAttributeChange = (index, field, value) => {
    const updateAttributes = (attributes) =>
      attributes.map((attr, i) => (i === index ? { ...attr, [field]: value } : attr));

    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        jpaDescriptor: {
          ...prevFormData.entityDescriptor.jpaDescriptor,
          attributes: updateAttributes(prevFormData.entityDescriptor.jpaDescriptor.attributes)
        },
        domainDescriptor: {
          ...prevFormData.entityDescriptor.domainDescriptor,
          attributes: updateAttributes(prevFormData.entityDescriptor.domainDescriptor.attributes)
        },
        dtoDescriptor: {
          ...prevFormData.entityDescriptor.dtoDescriptor,
          attributes: updateAttributes(prevFormData.entityDescriptor.dtoDescriptor.attributes)
        }
      }
    }));

    setErrors((prevErrors) => ({ ...prevErrors, [`attribute_${index}`]: null }));
  };

  const handleSubmit = async () => {
    if (validateStep()) {
      setIsSubmitting(true);
      try {
        const response = await fetch('http://localhost:8080/', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formData)
        });

        if (!response.ok) throw new Error('Failed to submit form');

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
        setFormData({
          entityDescriptor: {
            packageName: '',
            entityName: '',
            systemPrompt: '',
            jpaDescriptor: {
              tableName: '',
              attributes: []
            },
            domainDescriptor: {
              attributes: []
            },
            dtoDescriptor: {
              attributes: []
            },
            rulesDescriptor: []
          },
          standardTemplates: [],
          rulesTemplates: [],
          templates: []
        });
      } catch (error) {
        console.error('Error submitting form:', error);
        alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.');
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  return (
    <div className="container my-5">
      <h1 className="text-center mb-4">Formulário de API</h1>
      {isFetching ? (
        <div className="text-center">
          <Spinner animation="border" variant="primary" />
          <p>Carregando dados...</p>
        </div>
      ) : (
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
                  rows={3}
                  name="systemPrompt"
                  value={formData.entityDescriptor.systemPrompt}
                  onChange={handleInputChange}
                  isInvalid={!!errors.systemPrompt}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.systemPrompt}
                </Form.Control.Feedback>
              </Form.Group>
              <div className="d-flex justify-content-end">
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
                <Button variant="primary" onClick={handleNextStep}>
                  Próximo
                </Button>
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
                    <Form.Control.Feedback type="invalid">
                      {errors[`rule_${index}`]}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group controlId={`ruleInput_${index}`}>
                    <Form.Label>Input da Regra</Form.Label>
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
                <Button variant="primary" onClick={handleSubmit} disabled={isSubmitting}>
                  {isSubmitting ? 'Enviando...' : 'Enviar'}
                </Button>
              </div>
            </div>
          )}
        </Form>
      )}

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

import React, { useState, useEffect } from 'react';

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

  useEffect(() => {
    // Simula a chamada da API e o preenchimento dos dados no estado
    const fetchInitialData = async () => {
      try {
        const response = await fetch('http://localhost:8080/');
        if (!response.ok) throw new Error('Failed to fetch data');
        const data = await response.json();
        
        // Preenche o estado com os dados da API
        setFormData(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchInitialData();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        [name]: value
      }
    }));
  };

  const handleAddAttribute = (descriptor) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        [descriptor]: {
          ...prevFormData.entityDescriptor[descriptor],
          attributes: [
            ...prevFormData.entityDescriptor[descriptor].attributes,
            { name: '', type: '' }
          ]
        }
      }
    }));
  };

  const handleRemoveAttribute = (descriptor, index) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        [descriptor]: {
          ...prevFormData.entityDescriptor[descriptor],
          attributes: prevFormData.entityDescriptor[descriptor].attributes.filter((_, i) => i !== index)
        }
      }
    }));
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
    setCurrentStep(currentStep + 1);
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
  };

  const handleJpaAttributeChange = (index, field, value) => {
    const newAttributes = [...formData.entityDescriptor.jpaDescriptor.attributes];
    newAttributes[index][field] = value;
    setFormData((prevFormData) => ({
      ...prevFormData,
      entityDescriptor: {
        ...prevFormData.entityDescriptor,
        jpaDescriptor: {
          ...prevFormData.entityDescriptor.jpaDescriptor,
          attributes: newAttributes
        },
        domainDescriptor: {
          ...prevFormData.entityDescriptor.domainDescriptor,
          attributes: newAttributes
        },
        dtoDescriptor: {
          ...prevFormData.entityDescriptor.dtoDescriptor,
          attributes: newAttributes
        }
      }
    }));
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch('http://localhost:8080/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });
      
      if (!response.ok) throw new Error('Failed to submit form');
      
      // Como estamos recebendo um arquivo, precisamos tratá-lo de forma diferente
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'genhex4j.zip';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);

      // Reset form or show success message
      setCurrentStep(1);
    } catch (error) {
      console.error('Error submitting form:', error);
      // Show error message to user
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">Formulário de API</h1>
      <div className="bg-white p-4 rounded-md shadow-md">
        {currentStep === 1 && (
          <div>
            <h2 className="text-2xl font-bold mb-4">Informações Gerais</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Package Name</label>
                <input
                  type="text"
                  name="packageName"
                  value={formData.entityDescriptor.packageName}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Entity Name</label>
                <input
                  type="text"
                  name="entityName"
                  value={formData.entityDescriptor.entityName}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">System Prompt</label>
                <textarea
                  name="systemPrompt"
                  value={formData.entityDescriptor.systemPrompt}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
            <button type="button" onClick={handleNextStep} className="bg-blue-500 text-white px-3 py-1 rounded-md mt-4">Próximo</button>
          </div>
        )}
        {currentStep === 2 && (
          <div>
            <h2 className="text-2xl font-bold mb-4">Atributos JPA</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Table Name</label>
                <input
                  type="text"
                  name="tableName"
                  value={formData.entityDescriptor.jpaDescriptor.tableName}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              {formData.entityDescriptor.jpaDescriptor.attributes.map((attr, index) => (
                <div key={index} className="p-4 border rounded-md space-y-2">
                  <input
                    type="text"
                    value={attr.name}
                    placeholder="Attribute Name"
                    onChange={(e) => handleJpaAttributeChange(index, 'name', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={attr.type}
                    placeholder="Attribute Type"
                    onChange={(e) => handleJpaAttributeChange(index, 'type', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button type="button" onClick={() => handleRemoveAttribute('jpaDescriptor', index)} className="bg-red-500 text-white px-3 py-1 rounded-md">Remover Atributo</button>
                </div>
              ))}
              <button type="button" onClick={() => handleAddAttribute('jpaDescriptor')} className="bg-green-500 text-white px-3 py-1 rounded-md">Adicionar Atributo</button>
            </div>
            <div className="mt-4 space-x-2">
              <button type="button" onClick={handlePreviousStep} className="bg-gray-500 text-white px-3 py-1 rounded-md">Voltar</button>
              <button type="button" onClick={handleNextStep} className="bg-blue-500 text-white px-3 py-1 rounded-md">Próximo</button>
            </div>
          </div>
        )}
        {currentStep === 3 && (
          <div>
            <h2 className="text-2xl font-bold mb-4">Regras</h2>
            <div className="space-y-4">
              {formData.entityDescriptor.rulesDescriptor.map((rule, index) => (
                <div key={index} className="p-4 border rounded-md space-y-2">
                  <input
                    type="text"
                    value={rule.ruleName}
                    placeholder="Nome da Regra"
                    onChange={(e) => handleRuleInputChange(index, 'ruleName', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={rule.description}
                    placeholder="Descrição"
                    onChange={(e) => handleRuleInputChange(index, 'description', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={rule.ruleInput}
                    placeholder="Input da Regra"
                    onChange={(e) => handleRuleInputChange(index, 'ruleInput', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={rule.ruleOutput}
                    placeholder="Output da Regra"
                    onChange={(e) => handleRuleInputChange(index, 'ruleOutput', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <textarea
                    value={rule.llmGeneratedLogic}
                    placeholder="Lógica Gerada por LLM"
                    onChange={(e) => handleRuleInputChange(index, 'llmGeneratedLogic', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={rule.javaFunctionalInterface}
                    placeholder="Interface Funcional Java"
                    onChange={(e) => handleRuleInputChange(index, 'javaFunctionalInterface', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="text"
                    value={rule.javaFuncionalIntefaceMethodName}
                    placeholder="Nome do Método da Interface"
                    onChange={(e) => handleRuleInputChange(index, 'javaFuncionalIntefaceMethodName', e.target.value)}
                    className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button type="button" onClick={() => handleRemoveRule(index)} className="bg-red-500 text-white px-3 py-1 rounded-md">Remover Regra</button>
                </div>
              ))}
              <button type="button" onClick={handleAddRule} className="bg-green-500 text-white px-3 py-1 rounded-md">Adicionar Regra</button>
            </div>
            <div className="mt-4 space-x-2">
              <button type="button" onClick={handlePreviousStep} className="bg-gray-500 text-white px-3 py-1 rounded-md">Voltar</button>
              <button type="button" onClick={handleSubmit} className="bg-blue-500 text-white px-3 py-1 rounded-md">Enviar</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ApiForm;

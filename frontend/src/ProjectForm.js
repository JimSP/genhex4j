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

  const handleAddAttribute = (descriptor, attributeType) => {
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

  return (
    <form className="space-y-4 p-4">
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
      <div className="space-y-2">
        <label className="block text-sm font-medium mb-1">JPA Attributes</label>
        {formData.entityDescriptor.jpaDescriptor.attributes.map((attr, index) => (
          <div key={index} className="p-4 border rounded-md space-y-2">
            <input
              type="text"
              value={attr.name}
              placeholder="Attribute Name"
              onChange={(e) => {
                const newAttributes = [...formData.entityDescriptor.jpaDescriptor.attributes];
                newAttributes[index].name = e.target.value;
                setFormData((prevFormData) => ({
                  ...prevFormData,
                  entityDescriptor: {
                    ...prevFormData.entityDescriptor,
                    jpaDescriptor: {
                      ...prevFormData.entityDescriptor.jpaDescriptor,
                      attributes: newAttributes
                    }
                  }
                }));
              }}
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.type}
              placeholder="Attribute Type"
              onChange={(e) => {
                const newAttributes = [...formData.entityDescriptor.jpaDescriptor.attributes];
                newAttributes[index].type = e.target.value;
                setFormData((prevFormData) => ({
                  ...prevFormData,
                  entityDescriptor: {
                    ...prevFormData.entityDescriptor,
                    jpaDescriptor: {
                      ...prevFormData.entityDescriptor.jpaDescriptor,
                      attributes: newAttributes
                    }
                  }
                }));
              }}
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button type="button" onClick={() => handleRemoveAttribute('jpaDescriptor', index)} className="bg-red-500 text-white px-3 py-1 rounded-md">Remove Attribute</button>
          </div>
        ))}
        <button type="button" onClick={() => handleAddAttribute('jpaDescriptor')} className="bg-green-500 text-white px-3 py-1 rounded-md">Add Attribute</button>
      </div>
      <div className="space-y-2">
        <label className="block text-sm font-medium mb-1">Rules Descriptor</label>
        {formData.entityDescriptor.rulesDescriptor.map((rule, index) => (
          <div key={index} className="p-4 border rounded-md space-y-2">
            <input
              type="text"
              value={rule.ruleName}
              placeholder="Rule Name"
              onChange={(e) => {
                const newRules = [...formData.entityDescriptor.rulesDescriptor];
                newRules[index].ruleName = e.target.value;
                setFormData((prevFormData) => ({
                  ...prevFormData,
                  entityDescriptor: {
                    ...prevFormData.entityDescriptor,
                    rulesDescriptor: newRules
                  }
                }));
              }}
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.description}
              placeholder="Description"
              onChange={(e) => {
                const newRules = [...formData.entityDescriptor.rulesDescriptor];
                newRules[index].description = e.target.value;
                setFormData((prevFormData) => ({
                  ...prevFormData,
                  entityDescriptor: {
                    ...prevFormData.entityDescriptor,
                    rulesDescriptor: newRules
                  }
                }));
              }}
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button type="button" onClick={() => handleRemoveRule(index)} className="bg-red-500 text-white px-3 py-1 rounded-md">Remove Rule</button>
          </div>
        ))}
        <button type="button" onClick={handleAddRule} className="bg-green-500 text-white px-3 py-1 rounded-md">Add Rule</button>
      </div>
    </form>
  );
};

export default ApiForm;

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
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.type}
              placeholder="Attribute Type"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.columnDefinition}
              placeholder="Column Definition"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        ))}
      </div>
      <div className="space-y-2">
        <label className="block text-sm font-medium mb-1">Domain Attributes</label>
        {formData.entityDescriptor.domainDescriptor.attributes.map((attr, index) => (
          <div key={index} className="p-4 border rounded-md space-y-2">
            <input
              type="text"
              value={attr.name}
              placeholder="Attribute Name"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.type}
              placeholder="Attribute Type"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        ))}
      </div>
      <div className="space-y-2">
        <label className="block text-sm font-medium mb-1">DTO Attributes</label>
        {formData.entityDescriptor.dtoDescriptor.attributes.map((attr, index) => (
          <div key={index} className="p-4 border rounded-md space-y-2">
            <input
              type="text"
              value={attr.name}
              placeholder="Attribute Name"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.type}
              placeholder="Attribute Type"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.required ? 'Required' : 'Optional'}
              placeholder="Required"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={attr.maxLength || ''}
              placeholder="Max Length"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        ))}
      </div>
      <div className="space-y-2">
        <label className="block text-sm font-medium mb-1">Rules Descriptor</label>
        {formData.entityDescriptor.rulesDescriptor.map((rule, index) => (
          <div key={index} className="p-4 border rounded-md space-y-2">
            <input
              type="text"
              value={rule.ruleName}
              placeholder="Rule Name"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.description}
              placeholder="Description"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.ruleInput}
              placeholder="Rule Input"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.ruleOutput}
              placeholder="Rule Output"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <textarea
              value={rule.llmGeneratedLogic}
              placeholder="LLM Generated Logic"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.javaFunctionalInterface}
              placeholder="Java Functional Interface"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <input
              type="text"
              value={rule.javaFuncionalIntefaceMethodName}
              placeholder="Java Functional Interface Method Name"
              readOnly
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        ))}
      </div>
    </form>
  );
};

export default ApiForm;

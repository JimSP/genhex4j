import React, { useState, useEffect } from 'react';

const CodeGeneratorForm = () => {
  const [formData, setFormData] = useState({
    packageName: '',
    entityName: '',
    systemPrompt: '',
    jpaDescriptor: {
      tableName: '',
      attributes: [{
        name: '',
        type: '',
        columnDefinition: '',
        required: false
      }]
    },
    rulesDescriptor: [{
      ruleName: '',
      llmGeneratedLogic: ''
    }]
  });

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      const response = await fetch('http://localhost:8080/');
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      const data = await response.json();
      setFormData({
        packageName: data.packageName || '',
        entityName: data.entityName || '',
        systemPrompt: data.systemPrompt || '',
        jpaDescriptor: {
          tableName: data.jpaDescriptor?.tableName || '',
          attributes: data.jpaDescriptor?.attributes || [{
            name: '',
            type: '',
            columnDefinition: '',
            required: false
          }]
        },
        rulesDescriptor: data.rulesDescriptor || [{
          ruleName: '',
          llmGeneratedLogic: ''
        }]
      });
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const payload = {
      ...formData,
      domainDescriptor: {
        attributes: formData.jpaDescriptor.attributes.map(attr => ({
          name: attr.name,
          type: attr.type
        }))
      },
      dtoDescriptor: {
        attributes: formData.jpaDescriptor.attributes.map(attr => ({
          name: attr.name,
          type: attr.type,
          required: attr.required,
          maxLength: attr.columnDefinition?.includes('VARCHAR') 
            ? parseInt(attr.columnDefinition.match(/\d+/)?.[0] || '0') 
            : undefined
        }))
      }
    };

    try {
      const response = await fetch('http://localhost:8080/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });
      
      if (!response.ok) {
        throw new Error('Failed to generate code');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'generated-code.zip';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  const handleAttributeChange = (index, field, value) => {
    setFormData(prev => ({
      ...prev,
      jpaDescriptor: {
        ...prev.jpaDescriptor,
        attributes: prev.jpaDescriptor.attributes.map((attr, i) =>
          i === index ? { ...attr, [field]: value } : attr
        )
      }
    }));
  };

  const addAttribute = () => {
    setFormData(prev => ({
      ...prev,
      jpaDescriptor: {
        ...prev.jpaDescriptor,
        attributes: [
          ...prev.jpaDescriptor.attributes,
          { name: '', type: '', columnDefinition: '', required: false }
        ]
      }
    }));
  };

  const removeAttribute = (index) => {
    setFormData(prev => ({
      ...prev,
      jpaDescriptor: {
        ...prev.jpaDescriptor,
        attributes: prev.jpaDescriptor.attributes.filter((_, i) => i !== index)
      }
    }));
  };

  const handleRuleChange = (index, field, value) => {
    setFormData(prev => ({
      ...prev,
      rulesDescriptor: prev.rulesDescriptor.map((rule, i) =>
        i === index ? { ...rule, [field]: value } : rule
      )
    }));
  };

  const addRule = () => {
    setFormData(prev => ({
      ...prev,
      rulesDescriptor: [
        ...prev.rulesDescriptor,
        { ruleName: '', llmGeneratedLogic: '' }
      ]
    }));
  };

  const removeRule = (index) => {
    setFormData(prev => ({
      ...prev,
      rulesDescriptor: prev.rulesDescriptor.filter((_, i) => i !== index)
    }));
  };

  return (
    <div className="p-4">
      <h1 className="text-xl mb-4">Genhex4j :: powered by ene3xt.ai</h1>
      <h2 className="text-lg mb-4">Code Generator Configuration</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <div>
            <label className="block">Package Name</label>
            <input
              type="text"
              value={formData.packageName}
              onChange={e => setFormData(prev => ({ ...prev, packageName: e.target.value }))}
              className="border p-1 w-64"
            />
          </div>

          <div className="mt-2">
            <label className="block">Entity Name</label>
            <input
              type="text"
              value={formData.entityName}
              onChange={e => setFormData(prev => ({ ...prev, entityName: e.target.value }))}
              className="border p-1 w-64"
            />
          </div>

          <div className="mt-2">
            <label className="block">System Prompt</label>
            <textarea
              value={formData.systemPrompt}
              onChange={e => setFormData(prev => ({ ...prev, systemPrompt: e.target.value }))}
              className="border p-1 w-64"
              rows={3}
            />
          </div>

          <div className="mt-4">
            <label className="block">JPA Attributes</label>
            <button
              type="button"
              onClick={addAttribute}
              className="bg-gray-200 px-2 py-1 text-sm mb-2"
            >
              Add Attribute
            </button>
            {formData.jpaDescriptor.attributes.map((attr, index) => (
              <div key={index} className="flex gap-2 mb-2 items-start">
                <input
                  type="text"
                  value={attr.name}
                  onChange={e => handleAttributeChange(index, 'name', e.target.value)}
                  placeholder="Name"
                  className="border p-1"
                />
                <input
                  type="text"
                  value={attr.type}
                  onChange={e => handleAttributeChange(index, 'type', e.target.value)}
                  placeholder="Type"
                  className="border p-1"
                />
                <input
                  type="text"
                  value={attr.columnDefinition || ''}
                  onChange={e => handleAttributeChange(index, 'columnDefinition', e.target.value)}
                  placeholder="Column Definition"
                  className="border p-1"
                />
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    checked={attr.required}
                    onChange={e => handleAttributeChange(index, 'required', e.target.checked)}
                    className="mr-1"
                  />
                  <label>Required</label>
                </div>
                <button
                  type="button"
                  onClick={() => removeAttribute(index)}
                  className="bg-gray-200 px-2 py-1 text-sm"
                >
                  Remove
                </button>
              </div>
            ))}
          </div>

          <div className="mt-4">
            <label className="block">Rules</label>
            <button
              type="button"
              onClick={addRule}
              className="bg-gray-200 px-2 py-1 text-sm mb-2"
            >
              Add Rule
            </button>
            {formData.rulesDescriptor.map((rule, index) => (
              <div key={index} className="flex gap-2 mb-2">
                <input
                  type="text"
                  value={rule.ruleName}
                  onChange={e => handleRuleChange(index, 'ruleName', e.target.value)}
                  placeholder="Rule Name"
                  className="border p-1"
                />
                <textarea
                  value={rule.llmGeneratedLogic}
                  onChange={e => handleRuleChange(index, 'llmGeneratedLogic', e.target.value)}
                  placeholder="Logic"
                  className="border p-1"
                  rows={3}
                />
                <button
                  type="button"
                  onClick={() => removeRule(index)}
                  className="bg-gray-200 px-2 py-1 text-sm h-8"
                >
                  Remove
                </button>
              </div>
            ))}
          </div>
        </div>

        <button 
          type="submit" 
          className="bg-gray-200 px-2 py-1"
        >
          Generate Code
        </button>
      </form>
    </div>
  );
};

export default CodeGeneratorForm;
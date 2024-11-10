// src/db.js
import Dexie from 'dexie';

export const db = new Dexie('FormsDatabase');

db.version(1).stores({
  forms: '++id, name, timestamp', // Chave primária e índices
});

// Função para salvar um formulário
export const saveForm = async (form) => {
  const timestamp = Date.now();
  const id = await db.forms.add({ ...form, timestamp });
  return id;
};

// Função para atualizar um formulário existente
export const updateForm = async (id, updatedForm) => {
  await db.forms.update(id, { ...updatedForm, timestamp: Date.now() });
};

// Função para obter todos os formulários
export const getAllForms = async () => {
  const forms = await db.forms.toArray();
  return forms;
};

// Função para obter um formulário pelo ID
export const getFormById = async (id) => {
  const form = await db.forms.get(id);
  return form;
};

// Função para excluir um formulário
export const deleteForm = async (id) => {
  await db.forms.delete(id);
};

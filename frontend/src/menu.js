<div className="saved-forms">
  <h3>Formulários Salvos</h3>
  <ul>
    {savedForms.map((form) => (
      <li key={form.id}>
        <span>{form.name}</span>
        <Button variant="link" onClick={() => handleLoadForm(form.id)}>
          Carregar
        </Button>
        <Button variant="link" onClick={() => handleDeleteForm(form.id)}>
          Excluir
        </Button>
      </li>
    ))}
  </ul>
</div>

#!/bin/bash

# Abortar em caso de erros
set -e

# Construir a aplicação
npm run build

# Fazer o deploy para o GitHub Pages
npm run deploy

# Mensagem de sucesso
echo "Deploy realizado com sucesso!"

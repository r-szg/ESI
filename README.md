# Backend
Backend desenvolvido em **Spring Boot**. Ele fornece a lógica de negócio e a camada de serviços que suportam a aplicação. O backend é responsável por gerenciar as operações de CRUD, validações e integrações com bancos de dados e outros serviços externos.

O banco de dados utilizado é o **MySQL**.

## Como rodar

### Requisitos
- JDK 21+
- Docker (para o banco de dados)
> Observação: O banco de dados é criado automaticamente quando a aplicação é inicializada

### Instruções
- Rode o projeto com o comando:

```bash
mvn spring-boot:run 
```
ou, caso não tenha o Maven instalado, use o Wrapper

```bash
./mvnw spring-boot:run      # Unix
./mvnw.cmd spring-boot:run  # Windows
```


## Documentação
A API está sendo documentada utilizando o Swagger e pode ser acessada via:

```bash
http://localhost:8080/swagger-ui/index.html
```

# Frontend
## React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

### Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
export default tseslint.config({
  languageOptions: {
    // other options...
    parserOptions: {
      project: ['./tsconfig.node.json', './tsconfig.app.json'],
      tsconfigRootDir: import.meta.dirname,
    },
  },
})
```

- Replace `tseslint.configs.recommended` to `tseslint.configs.recommendedTypeChecked` or `tseslint.configs.strictTypeChecked`
- Optionally add `...tseslint.configs.stylisticTypeChecked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and update the config:

```js
// eslint.config.js
import react from 'eslint-plugin-react'

export default tseslint.config({
  // Set the react version
  settings: { react: { version: '18.3' } },
  plugins: {
    // Add the react plugin
    react,
  },
  rules: {
    // other rules...
    // Enable its recommended rules
    ...react.configs.recommended.rules,
    ...react.configs['jsx-runtime'].rules,
  },
})
```

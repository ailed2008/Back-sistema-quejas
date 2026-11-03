# 🏦 Sistema de Quejas CNBV - Backend REST API

API RESTful desarrollada para la gestión y seguimiento de quejas financieras, implementando arquitectura limpia y prácticas de seguridad estándar en la industria.

## 🚀 Tecnologías Utilizadas

* **Java 21** & **Spring Boot 3.x**
* **Spring Security** + **JWT (JSON Web Tokens)** para autenticación y autorización
* **Spring Data JPA** & **Hibernate**
* **PostgreSQL** (Hospedado en **Supabase**)
* **Maven** para gestión de dependencias
* Desplegado en **Render**

## 🔐 Seguridad y Autenticación

El sistema implementa autenticación basada en tokens JWT. Los endpoints de autenticación son públicos, mientras que los endpoints de gestión de quejas requieren enviar el token en la cabecera:

`Authorization: Bearer <tu_token_jwt>`

### Endpoints Principales

* `POST /api/v1/auth/register` - Registro de nuevos usuarios
* `POST /api/v1/auth/authenticate` - Inicio de sesión y generación de token JWT
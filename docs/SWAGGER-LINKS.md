# Swagger UI — Ticket System

## Acceso a la documentación

| Servicio         | Swagger UI                                         | API Docs                                    |
|------------------|----------------------------------------------------|---------------------------------------------|
| BFF (8080)       | http://localhost:8080/swagger-ui.html              | http://localhost:8080/api-docs              |
| MS-Auth (8081)   | http://localhost:8081/swagger-ui.html              | http://localhost:8081/api-docs              |
| MS-Event (8082)  | http://localhost:8082/swagger-ui.html              | http://localhost:8082/api-docs              |
| MS-Ticket-Sales  | http://localhost:8083/swagger-ui.html              | http://localhost:8083/api-docs              |

## Flujo recomendado de pruebas

1. Registrar usuario → `POST /api/auth/register` (BFF)
2. Hacer login → `POST /api/auth/login` (BFF) → guardar el token JWT
3. Crear un evento → `POST /api/events` con Bearer token
4. Registrar una venta → `POST /api/sales` con el ID del evento y Bearer token

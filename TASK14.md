# Task 14: Customer care history

Read TransactionAPI -> TransactionServiceImpl -> TransactionRepositoryImpl.
Read TransactionTypeAPI -> TransactionTypeServiceImpl -> TransactionTypeRepositoryImpl.

## Endpoints
- GET /api/transaction-types: 200, list of id/name/code.
- GET /api/customers/{customerId}/transactions: 200, newest first; missing customer: 404.
- POST /api/customers/{customerId}/transactions: JSON {"typeId":1,"note":"Called customer"}; 201 {"id":...}.

Use an existing customer and type ID from your database. The sample transactiontype table may be empty.
The note is trimmed and must be 1-255 characters. Type ID must be positive and exist.
Invalid data: 400. Missing customer: 404. Empty history: [].
createdDate is set by the database. LEFT JOIN preserves older records with null type; typeName may be null.

## Running
Java 8, Maven, MySQL schema from the course. Set DB_URL, DB_USER, DB_PASSWORD in the process environment.
The app does not load .env automatically. Port: 8081.

## Verification
TransactionServiceCheck is a standalone assertion test (run main with -ea) using repository doubles.
It checks validation, missing customers/types, no insert on invalid input, trim, generated ID and DTO mapping.
Database integration still requires local API testing; these checks do not execute SQL.

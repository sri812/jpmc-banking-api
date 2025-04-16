## Spring Boot Banking Application

This is a Spring Boot-based banking application where users can create accounts, perform transactions, and view monthly statements.

The project follows a clean architecture with `controller`, `service`, `model`, and `repository` packages. It also features custom exception handling and input validations with DTOs.

### Requirements:
- Java 17
- Maven
- No external database setup required – uses in-memory storage
- Application runs locally on port 8080

For API documentation, you can check the Swagger UI at: [Swagger API Docs](http://localhost:8080/swagger-ui.html)

---
### Which branch to run :

**Branch:**  feature/accounts-transactions-api contains the implementation.

A PR has been created from the feature branch to main branch
[View Pull Request](https://github.com/sri812/jpmc-banking-api/pull/1)

---
### Screenshots :
All screenshots showing API responses are stored in the `docs/screenshots/` directory. You can view them in the documentation sections below, where relevant.

---

###  Run the spring boot CRUD API application with
```bash
mvn clean install
```
```bash
mvn spring-boot:run
```
Run unit testing with
```bash
mvn test
```
---


### TASK 3 : Oauth, a pre-requisite to hit the Rest APIS
	1. For testing purposes, implemented a mock authentication server using the Nimbus JOSE+JWT library:
	2. Only users with the role new_app_role can access the protected endpoints for account and transaction data.

1. get the token (Initial Step)
```bash
  
  curl -X GET "http://localhost:8080/token"
```

### Replace YOUR_TOKEN_HERE with the actual token in all the commands below

This will return a JSON response with an access_token that you can use for authentication:

{
"access_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEyMzQ1Ni...",
"token_type": "Bearer",
"expires_in": 3600
}

---
### TASK 1 : Transactions and Account Management
	1.	Account Management: Create accounts, view all accounts, and fetch individual account details.
	2.	Transaction Management: Create transactions (credit/debit) for a specific account.
	3.	Monthly Statement: Fetch a monthly transaction statement for a given account.

### Non - Functional requirements implemented :
Logging with slf4j, Custom Exception Handling, Swagger documentation.

### Additional Improvements I've could've added if Time Permitted :
1. Integration testing with Karate framework
2. Create Account Endpoint Not Exposed as a public API endpoint

### REST API Endpoints
1.	Create Account
	•	POST /api/accounts
	•	Request Body: AccountDto (e.g., accountNumber, accountType, balance)

```bash
curl -X POST "http://localhost:8080/api/accounts" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"12345","accountType":"SAVINGS","balance":1000}'
```

2.	Get All Accounts
	•	GET /api/accounts
	•	Response: List of AccountDtos
```bash
   curl -X GET "http://localhost:8080/api/accounts" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

3.	Get Account by ID
	•	GET /api/accounts/{id}
	•	Response: AccountDtoc
```bash
   curl -X GET "http://localhost:8080/api/accounts/1" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

4.	Create Transaction
	•	POST /api/accounts/{accountId}/transactions
	•	Request Body: TransactionDto (e.g., amount, type, description, date)
```bash
curl -X POST "http://localhost:8080/api/accounts/1/transactions" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
        "amount": 200,
        "type": "credit",
        "description": "Salary",
        "date": "2025-04-15"
      }'
   ```

5.	Get Transactions by Account ID
	•	GET /api/accounts/{accountId}/transactions
	•	Response: List of TransactionDtos
```bash
  
  curl -X GET "http://localhost:8080/api/accounts/1/transactions" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

6.	Get Monthly Statement
	•	GET /api/statements/{accountId}?month={year-month}
	•	Example: /api/statements/1?month=2025-04
```bash
  curl -X GET "http://localhost:8080/api/statements/1?month=2025-04" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
```
---

### TASK 2 : IN MEMORY DATABASE
	1. Used an in-memory H2 database
---

#### Troubleshooting
If you receive a 401 Unauthorized error:
Make sure you're including the token in the Authorization header
Check that the token format is correct: Bearer your_token_here
Spring Boot-based banking application where users can create accounts, perform transactions, and view monthly statements.

Project structure has controller, service, model and repository packages with custom exception handling and input validations with DTO.

Requires: Java 17, Maven to run locally at port 8080.
No external database setup needed – uses in-memory storage.
See the [Swagger API Docs](http://localhost:8080/swagger-ui.html)
---
### Which branch to run :

**Branch:**  feature/accounts-transactions-api contains the implementation.

A PR has been created from the feature branch to main branch
[View Pull Request](https://github.com/sri812/jpmc-banking-api/pull/1)

### Screenshots :
All screenshots showing API responses are stored in the `docs/screenshots/` directory. You can view them in the documentation sections below, where relevant.


##  Run the spring boot CRUD API application with
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
### TASK 1 : TRANSACTIONS AND ACCOUNT MANAGEMENT
	1.	Account Management: Create accounts, view all accounts, and fetch individual account details.
	2.	Transaction Management: Create transactions (credit/debit) for a specific account.
	3.	Monthly Statement: Fetch a monthly transaction statement for a given account.

### Non - Functional requirements implemented :
Logging with sl4j, Custom Exception Handling, unit testing.

### Additional Improvements I've could've added if Time Permitted :
1. Integration testing with Karate framework
2. Create Account Endpoint Not Exposed as a public API endpoint

### REST API Endpoints
1.	Create Account
	•	POST /api/accounts
	•	Request Body: AccountDto (e.g., accountNumber, accountType, balance)

```bash
curl -X POST "http://localhost:8080/api/accounts" \
     -H "Content-Type: application/json" \
     -d '{"accountNumber":"12345","accountType":"SAVINGS","balance":1000}'
```

2.	Get All Accounts
	•	GET /api/accounts
	•	Response: List of AccountDtos
```bash
   curl -X GET "http://localhost:8080/api/accounts"
   ```

3.	Get Account by ID
	•	GET /api/accounts/{id}
	•	Response: AccountDtoc
```bash
   curl -X GET "http://localhost:8080/api/accounts/1"
   ```

4.	Create Transaction
	•	POST /api/accounts/{accountId}/transactions
	•	Request Body: TransactionDto (e.g., amount, type, description, date)
```bash
curl -X POST "http://localhost:8080/api/accounts/1/transactions" \
     -H "Content-Type: application/json" \
     -d '{"amount":200,"type":"credit","description":"Salary","date":"2025-04-15"}'
   ```

5.	Get Transactions by Account ID
	•	GET /api/accounts/{accountId}/transactions
	•	Response: List of TransactionDtos
```bash
  
  curl -X GET "http://localhost:8080/api/accounts/1/transactions"
```

6.	Get Monthly Statement
	•	GET /api/statements/{accountId}?month={year-month}
	•	Example: /api/statements/1?month=2025-04
```bash
  
  curl -X GET "http://localhost:8080/api/statements/1?month=2025-04"
```
---

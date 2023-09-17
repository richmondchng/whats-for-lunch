# whats-for-lunch (v1.0)

## Project Notes:
- Project consist of 2 modules, namely the `backend` and `frontend`
- The source files for the backend Spring Boot application is in `\backend` directory
- The source files for the frontend Angular application is in `\frontend` directory
- In the `\postman` directory, you will be able to find a collection of backend API

## Demo mode
- Starting the backend application in `demo` profile will inject some demo data
  - Users
    - user name = `john`, first name = `John`, last name = `Wong`, password = `password`
    - user name = `victor`, first name = `Victor`, last name = `Goh`, password = `password`
    - user name = `pam`, first name = `Pamela`, last name = `Yeo`, password = `password`
    - user name = `natasha`, first name = `Natasha`, last name = `Goh`, password = `password`
  - Random session data
   
## REST API Documentation
- http://`hostname`/swagger-ui/index.html
- http://`hostname`/v3/api-docs

## Set up and Run
1. Refer to the `README` file in each of the `backend`/`frontend` directory for details on how to start up the application. 
   - Backend - Build with maven, run packaged JAR file 
   - Frontend - Build with node, run with `ng serve`
2. Open frontend application in `http://localhost:4200`
3. Login to application using any one of the demo user
4. In the "home" page, there will be 2 list of sessions - Active and Closed
5. Click the "x" icon to delete any of the session
6. Double-click the card to view the details, and to add restaurant(s) to the session
7. If owner of the session, there will be a button to trigger restaurant selection. On complete, the session will be moved to the Closed list

## Postman Collection
1. There are 2 files in the `postman` folder
   - Environment file - this contains environment variables that will be injected into the Postman collection
   - Collection file - this contains a list of requests for manual testing
2. Import the collection file into Postman
3. In the User folder, execute the `Get token` request to generate a JWT from the backend application. A script will save the token value into the session.
4. Once this first request is completed, you will be able to execute the other requests that requires a JWT token.
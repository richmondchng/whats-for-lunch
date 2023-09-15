# whats-for-lunch (v1.0)

## Project Notes:
- Application is built in Java 17, with Maven
- At the current version, application is backed by an embedded H2 database (for demo/development purpose)
- A default `admin` user will be created when backend application is started up
- Default port is 8080. For example, if application is started up in localhost then the `host` will be `localhost:8080`

## Demo mode
- Application will inject sample data into the application
  - Additional users
    - user name = `john`, first name = `John`, last name = `Wong`
    - user name = `victor`, first name = `Victor`, last name = `Goh`
    - user name = `pam`, first name = `Pamela`, last name = `Yeo`
    - user name = `natasha`, first name = `Natasha`, last name = `Goh`
  - random session data

## To build and run application
1. Clone (download) repository
2. Open a command line terminal (e.g., PowerShell)
3. Navigate to project `backend` folder of Spring Boot project
   ```shell
   C:\whats-for-lunch\backend
   ```
4. Run the following command to build Spring Boot project
   ```shell
   .\mvnw clean package
   ```
5. When build is completed, run the following command to start application
    ```shell
     java -jar .\target\whatsforlunch-0.0.1-SNAPSHOT.jar
    ```
   **NOTE:** To start up application in **Demo** mode, replace the above command to start application with "demo" profile activated
    ```shell
     java -jar -D"spring.profiles.active=demo" .\target\whatsforlunch-0.0.1-SNAPSHOT.jar
    ```
## Resources
- REST API Documentation
  - http://`hostname`/swagger-ui/index.html
  - http://`hostname`/v3/api-docs
- H2 Console
  - http://`hostname`/h2-console

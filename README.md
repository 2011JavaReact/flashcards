# flashcards

This is a backend api for a flashcard system. You can sign up with an account and login to create/manage your own flashcards. You may view the documentation for this api by pasting the contents of `./swagger.yaml` into the [text editor at swagger.io](https://editor.swagger.io/).

## Technologies Used

- Java
- Maven
- PostgreSQL
- JDBC
- Apache Tomcat/Servlets
- Log4j
- JWTs
- bcrypt
- GSON
- OpenAPI 3.0 documentation

## Features

- account creation/login
- api endpoints secured by JWT encoded auth info
- ability to create customizable flashcard templates
- ability to create flashcards with data to be inserted into your templates

## Getting Started

``` 
git clone https://github.com/2011JavaReact/flashcards.git
mvn clean package
```

### initializing the PostgreSQL database

set the following environment variables

``` 
DB_USERNAME=postgres
DB_URL=jdbc:postgresql://localhost:5432/
DB_NAME=flashcards
DB_PASSWORD=password
```

Then, initialize the database/schema with a helper application

``` 
mvn compile exec:java -q -e \
    -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
    -Dexec.args=initdb
```

### adding dummy data

set the same env variables as above and run the helper application

``` 
mvn compile exec:java -q -e \
    -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
    -Dexec.args=dummydb
```

### dropping the db

to drop the db conveniently, after setting the same env vars as above, you may run the helper application

``` 
mvn compile exec:java -q -e \
    -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
    -Dexec.args=dropdb
```

### running the api

finally, to run the application, set the above env vars and then run

``` 
mvn package tomcat7:run
```

## Usage

This is a REST api. You may communicate with it via standard HTTP methods. All the routes and required request data are noted in the documentation. You can view the documentation by pasting `./swagger.yaml` into the [text editor at swagger.io](https://editor.swagger.io/).

## database ERD

<img src="https://user-images.githubusercontent.com/25497140/100248798-c362e880-2ef0-11eb-9715-cf09034b598e.png"/>

## Contributors

- Vincent Sevilla

## License

This project is licensed under the terms of the MIT license.
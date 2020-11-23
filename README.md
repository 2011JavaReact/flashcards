### flashcards

### running
```
DB_USERNAME=postgres DB_PASSWORD=admin DB_NAME=flashcards \
DB_URL="jdbc:postgresql://localhost:5432/" \
mvn compile exec:java -q \
    -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
    -Dexec.args=dropdb
```


UserInRequest // used for post/put. if put a second param of id should be passed to dao method
- username
- password // can hold the real password or the hash of it.

UserInDb
- username
- password // is a hash
- id
- admin

User
- id
- username
- admin



CardTemplateInRequest
- userID
- description
- front
- back

CardTemplate
- id
- userID
- description
- front
- back

CardInRequest
- userID
- templateID
- data

Card
- id
- userID
- templateID
- data



















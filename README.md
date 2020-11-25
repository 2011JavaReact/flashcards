### flashcards

### TODO
* gson is not throwing when fields arent provided. need to somehow get gson to enforce nulls. this is why i hate java. needs a nullable type...
* rn cant do partial updates. have to update everything. not good for user bc u dont know pass when u update. oh well.


### running
```
DB_USERNAME=postgres DB_PASSWORD=admin DB_NAME=flashcards \
DB_URL="jdbc:postgresql://localhost:5432/" \
mvn compile exec:java -q \
    -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
    -Dexec.args=dropdb
```








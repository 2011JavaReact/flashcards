### flashcards
This is a backend for a flashcard system. You can sign up with an account and login to create/manage your own flashcards.


### database
I have three tables: users, cards, card_templates. Each user can have many card templates. A card template contains front and back templates. These are just html strings containing placeholder expressions.  When a user makes a card, they have to choose a card template to use for it. The card has a data attribute which is just a stringifed map of data attributes to be replaced in the card templates front/back sections. The idea is that if a user has a card and its template, its data can be placed into its template and then this can be rendered via the browser through html. Since it's html, the cards can be styled to the users liking.

```
example template {
    front: "<div>%%FRONT%%</div>"
    back: "<div>%%BACK%%</div>"
}
that might be used by card {
    data: '{FRONT: "whats the capital of California", BACK: "Sacremento"}'
}
```

### schema
<img src="https://user-images.githubusercontent.com/25497140/100248798-c362e880-2ef0-11eb-9715-cf09034b598e.png"/>

### api documentation
you can view this by pasting the contents of `./swagger.yaml` into the editor at https://editor.swagger.io/

### prerequisites
The only system requirements you need are `java 8` and `maven`. There is a maven plugin I've included that when run, starts an embedded tomcat server which means you dont need to download tomcat locally.

### reinitialize the database with dummy data
```
DB_USERNAME=postgres DB_PASSWORD=admin DB_NAME=flashcards \
    DB_URL="jdbc:postgresql://localhost:5432/" \
    mvn compile exec:java -q \
        -Dexec.mainClass=com.revature.flashcards.util.DbManagerDriver \
        -Dexec.args=dummydb
```

### running
```
DB_USERNAME=postgres DB_PASSWORD=admin DB_NAME=flashcards \
    DB_URL="jdbc:postgresql://localhost:5432/" \
    mvn package tomcat7:run
```5
6

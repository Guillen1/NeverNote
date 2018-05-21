## "Nevernote" REST API for organized notes.

Application can be used to save, retrieve and update organized notes. Support CRUD operations for notes, using REST resources and operations to creates notes in a given notebook, update a given note and delete notes.

Features: 

+ No persistance. Data is stored in memory HSQLDB and reset on subsequent runs. 

+ Search notes by tag. API provides filtering by tag functionality to return notes in a notebook that contain the tag.

+ Unit test included

## API Details 

Application is built utilizing Spring Boot, JPA, and HSQLDB.

Application defines the followign CRUD APIs: 

    GET     /notebooks

    GET     /notebooks/{id}

    POST    /notebooks

    PUT     /notebooks/{id}

    DELETE  /notebooks/{id}

    GET     /notebooks/{id}/notes?tag={tag*optional}

    GET     /notes?tag={tag*optional}

    GET     /notebooks/{notebookId}/notes/{noteId}

    POST    /notebooks/{notebookId}/notes

    PUT     /notebooks/{notebookId}/notes/{noteId}

    DELETE  /notebooks/{notebookId}/notes/{noteId}
  
  
## Requirments

1. Java - 1.8.x

2. Maven - 3.x.x

## Setup

**1. Clone the application**

```bash
git clone https://github.com/Guillen1/NeverNote.git
```
**2. Build and run the app using maven**

```bash
mvn package java -jar target/nevernote-0.0.1-SNAPSHOT.jar
```

**3. Run app**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

You can test application using postman or any other REST client. 



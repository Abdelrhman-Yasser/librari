# Library Management System (Librabry)

Welcome to Librabry, a library management system that allows you to manage patrons and books efficiently. With Librabry, you can create, delete, and update patrons and books. Additionally, patrons can borrow and return books seamlessly.

## Installation

### Prerequisites
Make sure you have the following installed:
- Java 22
- Maven

### Build
```
mvn package
```

### Run
```
mvn spring-boot:run
```

### Test
```
mvn test
```


## APIs

### Books Endpoints

- **GET /api/books**: Retrieve a list of all books.
- **GET /api/books/{id}**: Retrieve details of a specific book by ID.
- **POST /api/books**: Add a new book to the library.
  - Request body:
    ```json
    {
        "author": "Spring in action",
        "title": "Craig Walls",
        "publicationYear": "2024-03-03"
    }
    ```
- **PUT /api/books/{id}**: Update an existing book's information.
  - Request body:
    ```json
    {
        "author": "Spring Boot in action",
        "title": "Craig Walls",
        "publicationYear": "2024-01-02"
    }
    ```
- **DELETE /api/books/{id}**: Remove a book from the library.

### Patron Management Endpoints

- **GET /api/patrons**: Retrieve a list of all patrons.
- **GET /api/patrons/{id}**: Retrieve details of a specific patron by ID.
- **POST /api/patrons**: Add a new patron to the system.
  - Request body:
    ```json
    {
        "name": "Tailor",
        "email": "tailor@maids.cc"
    }
    ```
- **PUT /api/patrons/{id}**: Update an existing patron's information.
  - Request body:
    ```json
    {
        "name": "Tailor",
        "email": "tailor@maids.com"
    }
    ```
- **DELETE /api/patrons/{id}**: Remove a patron from the system.

### Borrowing Endpoints

- **POST /api/borrow/{bookId}/patron/{patronId}**: Allow a patron to borrow a book.
- **PUT /api/return/{bookId}/patron/{patronId}**: Record the return of a borrowed book by a patron.

Feel free to explore and utilize these endpoints to manage your library efficiently.

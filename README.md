# MovieCritics

MovieCritics is an innovative web application designed for movie enthusiasts who enjoy exploring, rating, and sharing their opinions on films. Whether you're a casual viewer or a dedicated cinephile, MovieCritics provides a platform where you can discover new movies, engage with other movie lovers, and contribute your own insights. Built with modern technologies, this application aims to deliver a seamless and engaging user experience.

At its core, MovieCritics comprises a powerful backend developed with Java and Spring Boot, ensuring robust and efficient handling of data. The frontend is crafted using Thymeleaf and Bootstrap 5, offering responsive and aesthetically pleasing views that enhance user interaction. With a secure REST API, MovieCritics allows for smooth communication between the frontend and backend, making it easy to browse, rate, and comment on movies.

Key features include a comprehensive movie catalog, user authentication, and an admin panel for managing content. MovieCritics is designed to be user-friendly and intuitive, making it the perfect tool for anyone looking to delve deeper into the world of cinema.

## Features

- **Browse Movies**: Users can search and browse through a catalog of movies.
- **Rate Movies**: Logged-in users can rate movies on a scale of 1 to 5 stars.
- **Comment on Movies**: Users can leave comments on movie pages to share their thoughts and opinions.
- **User Authentication**: Secure user registration and login system.
- **Admin Panel**: Admin users have access to a panel for managing movies and user comments.

## Technologies Used

- **Java**: Programming language for the application's backend.
- **Spring Boot**: Framework for building the REST API and handling backend services.
- **Thymeleaf**: Template engine for rendering dynamic web pages.
- **Bootstrap 5**: Front-end framework for creating responsive and visually appealing user interfaces.
- **Postgres**: Database management system for storing user data and movie information.
- **Keycloak**: Identity and access management solution for user authentication.
- **Docker**: Containerization platform for managing the application's environment.
- **Bruno**: API testing tool.

## How to run it locally?

### Prerequisites

- Java Development Kit (JDK) version 21
- Docker

1. **Clone the Repository**
2. **Run the Docker Compose File**

```bash
docker compose -f docker-compose.dev.yml up --build
```

3. **Run the Application**

```bash
./gradlew bootRun
```

4. Optionally you can connect to the database and run the SQL script to seed the database with some data. Script is located in `src/main/resources/example-data.sql`.
5. **That's it!** You can now access the application at [http://localhost:8080](http://localhost:8080). Api docs are available at [http://localhost:8080/api/docs/swagger-ui.html](http://localhost:8080/api/docs/swagger-ui.html).

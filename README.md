# Stateful Authentication (Java)

## Flow
![Diagram](./resource/diagram.png)

### Tecnology
- [Java 17](https://jdk.java.net/17/)
- [Spring Boot 3.2.4](https://spring.io/blog/2024/03/21/spring-boot-3-2-4-available-now)
- [Gradle 8.7](https://docs.gradle.org/current/release-notes.html)
- Postgres (latest)
- Redis (latest)
- Swagger
- Docker

### Build
1. Intall **Python 3**
2. Execute: `python3 build.py`
> Build steps:
> - Compile Java projects: generate .jar
> - Build docker images
> - Down existing containers (if exists)
> - Up containers

#### Containers
- stateful-auth-api [link](http://0.0.0.0:8082/swagger-ui/index.html)
- stateful-any-api [link](http://0.0.0.0:8083/swagger-ui/index.html)
- stateful-auth-db
- stateful-redis

## References
- [Course - Autenticação Stateful e Stateless em Microsserviços](https://www.udemy.com/course/autenticacao-stateful-stateless-microsservicos/)
# Spring Boot Calculator App - CI/CD and Kubernetes Deployment

## 1. Java Spring Boot Web Calculator Application

### 1.1 `CalculatorService.java`

Contains logic for basic arithmetic operations: add, subtract, multiply, divide.

### 1.2 `CalculatorController.java`

REST controller exposing the `/api/calculate` endpoint.
Accepts query parameters: `a`, `b`, `op`.

### 1.3 `CalculatorApp.java`

Spring Boot application main class (`@SpringBootApplication`).

---

## 2. Unit Testing with JUnit 5

### `CalculatorServiceTest.java`

* Tests each arithmetic operation
* Tests division by zero and invalid operators

---

## 3. Maven Project Setup

* `pom.xml` includes:

  * Spring Boot dependencies
  * JUnit 5
  * Maven Surefire plugin
  * Maven compiler plugin with `-parameters`

Build command:

```bash
mvn clean package
```

---

## 4. Docker Image for the Application

### `Dockerfile.app`

Multi-stage Dockerfile:

* Stage 1: Builds the JAR using Maven
* Stage 2: Copies the JAR into a lightweight Eclipse Temurin runtime image

Build the image:

```bash
docker build -f Dockerfile.app -t omerbs/calcapp .
```

---

## 5. Jenkins CI Pipeline

### 5.1 Jenkins Custom Docker Image

* Based on `jenkins/jenkins:lts`
* Adds Maven and Docker CLI

### 5.2 Jenkinsfile Stages:

1. Clone GitHub repo
2. Run `mvn clean test` and archive results
3. Build Docker image
4. Push image to Docker Hub
5. Post block for result handling

---

## 6. Docker Compose Deployment

### `docker-compose.yaml`

* Uses the published Docker image from Docker Hub
* Exposes port 8081

Run:

```bash
docker-compose up --build
```

Stop:

```bash
docker-compose down --remove-orphans
```

---

## 7. Kubernetes Deployment with Helm

### 7.1 Helm Chart

* Templates for `deployment.yaml` and `service.yaml`
* Uses `NodePort` service (no Ingress)
* Configurable via `values.yaml`

Install:

```bash
helm upgrade --install calcapp ./calcapp-chart
```

Access the app:

```bash
http://localhost:<NodePort>/api/calculate?a=5&b=3&op=*
```

### 7.2 Customize values

Change a, b, op values in the URL.

---

## Notes:

* NodePort is used to expose the app locally on Docker Desktop
* No Ingress or domain name is configured
* The Helm chart supports value overrides for flexibility

---

## Summary

This project demonstrates:

* A RESTful Spring Boot calculator app
* Unit testing with JUnit 5
* CI/CD pipeline with Jenkins and Docker
* Containerized deployment using Docker Compose
* Kubernetes deployment via Helm (no Ingress, NodePort only)

Ready for end-to-end deployment from code to cluster.

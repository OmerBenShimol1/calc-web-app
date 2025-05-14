# Calculator App

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
  * Maven compiler plugin

---

## 4. Docker Image for the Application

### `Dockerfile.app`

Multi-stage Dockerfile:

* Stage 1: Builds the JAR using Maven
* Stage 2: Copies the JAR into a lightweight Eclipse Temurin runtime image

---

## 5. Jenkins Pipeline

### 5.1 Jenkins Custom Docker Image

* Based on `jenkins/jenkins:lts`
* Adds Maven and Docker CLI

### 5.2 Jenkinsfile Stages:

1. Clone GitHub repo
2. Run `mvn clean test` and archive results
3. Build Docker image
4. Push image to Docker Hub
5. Post block for result handling
The Jenkins pipeline ran successfully and completed, cloned the right GitHub repository, JUnit tests ran and passed succesfully and the test results were archived.

![image](https://github.com/user-attachments/assets/1ae93386-bab3-491c-b517-d572443f62d7)

Pipeline's overview:

![image](https://github.com/user-attachments/assets/715cd287-0ddb-4f28-95d3-45c0f983963c)

Test results:

![image](https://github.com/user-attachments/assets/a725418e-2f0a-4838-b9e4-7a157939a67a)

Docker Image publishing process:

![image](https://github.com/user-attachments/assets/d0e5e5fc-c78d-4ff1-a9f8-bbe4a28d6777)

![image](https://github.com/user-attachments/assets/3daa987e-590a-4a3a-b284-7deca859c5c3)


tests XML file archived:

![image](https://github.com/user-attachments/assets/de21af45-f108-4eb3-bef8-824ce3e9079c)

Image published to Docker Hub:

![image](https://github.com/user-attachments/assets/06303ee3-b89a-4724-951a-ac1faa8b514c)


---

## 6. Docker Compose Deployment

### `docker-compose.yaml`

* Uses the published Docker image from Docker Hub
* Exposes port 8081

![image](https://github.com/user-attachments/assets/4b94366b-e056-4fc3-a71a-fa434ee8e20f)

![image](https://github.com/user-attachments/assets/a70620c9-0081-429a-b555-767c5b94a195)

---

## 7. Kubernetes Deployment with Helm

### 7.1 Helm Chart

* Templates for `deployment.yaml` and `service.yaml`
* Uses `NodePort` service (no Ingress)
* Configurable via `values.yaml`

![image](https://github.com/user-attachments/assets/6feb02cc-8db1-4d8e-be32-12fa6489d021)

This command:

- Installs if not already installed.

- Upgrades the deployment if already exists.

Access the app:

```bash
http://localhost:<NodePort>/api/calculate?a=5&b=3&op=*
```

![image](https://github.com/user-attachments/assets/9accd13e-ebb7-4e5f-9d15-ab8911703747)


### 7.2 Customize values

Change a, b, op values in the URL.

---

## Notes:

* NodePort is used to expose the app locally on Docker Desktop




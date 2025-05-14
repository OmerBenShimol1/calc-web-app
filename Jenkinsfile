pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKER_IMAGE = 'omerbs/calcapp'
        DOCKER_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile.app'
    }

    stages {
        stage('Clone repository') {
            steps {
                git branch: 'main', url: 'https://github.com/OmerBenShimol1/calc-web-app.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean test'
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -f ${DOCKERFILE_PATH} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS_ID}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE:$DOCKER_TAG
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/surefire-reports/*.xml', allowEmptyArchive: true
        }
    }
}

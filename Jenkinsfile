pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKER_IMAGE          = 'omerbs/calcapp'
        DOCKER_TAG            = 'latest'
        DOCKERFILE_PATH       = 'Dockerfile.app'
    }

    stages {
        stage('Checkout (on gradle agent)') {
            agent { label 'gradle' }
            steps {
                git branch: 'main', url: 'https://github.com/OmerBenShimol1/calc-web-app.git'
            }
        }

        stage('Prepare Gradle Env') {
            agent { label 'gradle' }
            environment {
                GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
            }
            steps {
                sh 'mkdir -p "$GRADLE_USER_HOME"'
            }
        }

        stage('Build and Test (Gradle)') {
            agent { label 'gradle' }
            environment {
                GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
            }
            steps {
                sh './gradlew clean test --no-daemon'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Package (bootJar)') {
            agent { label 'gradle' }
            environment {
                GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
            }
            steps {
                sh './gradlew bootJar --no-daemon'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            when { beforeAgent true; expression { false } } // disabled for now
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -f ${DOCKERFILE_PATH} ."
            }
        }

        stage('Push to Docker Hub') {
            when { beforeAgent true; expression { false } } // disabled for now
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
            sh 'docker images --format "table {{.Repository}}\\t{{.Tag}}\\t{{.ID}}\\t{{.Size}}" || true'
        }
    }
}

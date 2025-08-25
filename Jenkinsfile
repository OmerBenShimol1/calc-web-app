pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        // Docker
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKER_IMAGE          = 'omerbs/calcapp'
        DOCKER_TAG            = 'latest'
        DOCKERFILE_PATH       = 'Dockerfile.app'

        // Gradle cache פרסיסטנטי (ממופה ב-compose של gradle-agent)
        GRADLE_USER_HOME = "/home/jenkins/.gradle"
    }

    stages {
        stage('Checkout (on gradle agent)') {
            agent { label 'gradle' }
            steps {
                git branch: 'main', url: 'https://github.com/OmerBenShimol1/calc-web-app.git'
                // שומרים את קוד המקור לדורות הבאים (מבלי build/.git/.gradle)
                stash name: 'workspace',
                      includes: '**/*',
                      excludes: '.git/**, **/build/**, **/.gradle/**'
            }
        }

        stage('Prepare Gradle Env') {
            agent { label 'gradle' }
            steps {
                sh 'mkdir -p "$GRADLE_USER_HOME"'
            }
        }

        stage('Build and Test (Gradle)') {
            agent { label 'gradle' }
            steps {
                unstash 'workspace'
                sh './gradlew clean test --no-daemon --build-cache'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Package (bootJar)') {
            agent { label 'gradle' }
            steps {
                unstash 'workspace'   // בטוח גם אם כבר קיים
                sh './gradlew bootJar --no-daemon'
                // שומרים את ה-artifact לשלב הדוקר
                stash name: 'jar', includes: 'build/libs/*.jar'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            agent { label 'docker' }
            steps {
                // מחזירים את הקוד ואת ה-JAR לאותו workspace של ה-docker agent
                unstash 'workspace'
                unstash 'jar'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -f ${DOCKERFILE_PATH} ."
            }
        }

        stage('Push to Docker Hub') {
            agent { label 'docker' }
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
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            // ירוץ רק על נוד שיש בו Docker; '|| true' מונע כישלון אם אין
            sh 'docker images --format "table {{.Repository}}\\t{{.Tag}}\\t{{.ID}}\\t{{.Size}}" || true'
            // cleanWs() // אופציונלי לניקוי workspace בסוף ריצה
        }
    }
}

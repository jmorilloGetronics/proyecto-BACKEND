pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw && ./mvnw -B -ntp clean verify'
                    } else {
                        bat '.\\mvnw.cmd -B -ntp clean verify'
                    }
                }
            }
        }

        stage('SonarQube (optional)') {
            when {
                allOf {
                    expression { env.SONAR_HOST_URL?.trim() }
                    expression { env.SONAR_TOKEN?.trim() }
                }
            }
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw && ./mvnw -B -ntp sonar:sonar -Dsonar.projectKey=proyecto-leo-backend -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.token=$SONAR_TOKEN'
                    } else {
                        bat '.\\mvnw.cmd -B -ntp sonar:sonar -Dsonar.projectKey=proyecto-leo-backend -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.token=%SONAR_TOKEN%'
                    }
                }
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/*.jar,target/site/jacoco/**', allowEmptyArchive: true, fingerprint: true
        }
    }
}
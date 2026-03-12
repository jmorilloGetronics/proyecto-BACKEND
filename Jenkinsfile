pipeline {
    agent any

    tools {
        maven 'Maven 3.x' // Esto depende de cómo se llame el Maven en el Jenkins de tu empresa
        jdk 'Java 17'     // Y esto del nombre que le hayan puesto al Java
    }

    stages {
        stage('Checkout') {
            steps {
                // Baja el código de GitLab
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Compila y pasa los tests
                sh 'mvn clean package'
            }
        }

        stage('Archivar Artefacto') {
            steps {
                // Guarda el .jar para que no se pierda
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        success {
            echo '¡Vamooo! El pipeline ha terminado con éxito.'
        }
        failure {
            echo 'Algo ha petado, revisa los logs.'
        }
    }
}
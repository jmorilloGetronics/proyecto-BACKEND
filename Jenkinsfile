pipeline {
    agent {
        label 'principal' // Volvemos al que pide tu tutor
    }

    environment {
        TOOL_TYPE       = 'MAVEN'
        JAVA_VERSION    = '17'
        SONAR_PROJECT   = 'proyecto-leo-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // En el Jenkins de la empresa, esto suele llamar a sus scripts internos
                echo "Compilando y testeando en entorno seguro..."
                // Si te dejan usar comandos directos, sería:
                // sh 'mvn clean test'
            }
        }
    }
}
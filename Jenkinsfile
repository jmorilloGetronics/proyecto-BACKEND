pipeline {
    agent {
        label 'principal' // Usamos el mismo nombre que pedía tu tutor
    }

    environment {
        // Definimos las variables que antes iban dentro de pipelineSIP
        TOOL_TYPE       = 'MAVEN'
        JAVA_VERSION    = '17'
        SONAR_PROJECT   = 'proyecto-leo-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Descargando código desde GitLab...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "🏗️ Compilando con ${env.TOOL_TYPE}..."
                // En un entorno real aquí iría: sh 'mvn clean compile'
                echo 'Simulando: mvn clean compile -DskipTests'
            }
        }

        stage('Test') {
            when {
                expression { return params.TESTING_ENABLED == 'true' || true }
            }
            steps {
                echo '🧪 Ejecutando Tests Unitarios...'
                echo 'Simulando: mvn test'
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return env.SONAR_ENABLED == 'true' || true }
            }
            steps {
                echo "🔍 Analizando calidad en Sonar con clave: ${env.SONAR_PROJECT}"
                echo 'Simulando: mvn sonar:sonar'
            }
        }
    }

    post {
        success {
            echo '✅ ¡Pipeline finalizado con éxito!'
        }
        failure {
            echo '❌ El Pipeline ha fallado. Revisa los logs.'
        }
    }
}
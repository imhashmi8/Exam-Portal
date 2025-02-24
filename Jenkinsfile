pipeline {
    agent any  // Runs on any available agent (Jenkins node)

    environment {
        REPO_URL = 'https://github.com/MausoofAzam/exam-portal.git'
        BRANCH = 'develop'  // Specify the branch to pull from
        BUILD_DIR = 'target'  // Default Maven build directory
        JAR_NAME = 'EXAM_MANAGEMENT-0.0.1-SNAPSHOT.jar'  // Change this to your application's JAR file name
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo 'Cloning repository...'
                git branch: "${BRANCH}", url: "${REPO_URL}"
                // Fetches code from GitHub
            }
        }

        stage('Build JAR') {
            steps {
                echo 'Building JAR file using Maven...'
                sh 'mvn clean package -DskipTests'
                // Runs Maven to build the project and create a JAR file
            }
        }

        stage('Run Application') {
            steps {
                echo 'Running Java application...'
                sh "java -jar ${BUILD_DIR}/${JAR_NAME}"
                // Runs the generated JAR file
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for errors.'
        }
    }
}

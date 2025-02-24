pipeline {
    agent any

    triggers {
        githubPullRequest {
            orgWhitelist(['imhashmi8'])
            allowMembersOfWhitelistedOrgsAsAdmin()
            onlyTriggerPhrase(false)
            githubHooks(true)
            triggerPhrase("test this PR")
            autoCloseFailedPullRequests(false)
            cron('* * * * *') // Polling every minute
        }
    }

    environment {
        BRANCH_NAME = "feature"
        TARGET_BRANCH = "develop"
    }

    stages {
        stage('Check Branch') {
            steps {
                script {
                    if (env.CHANGE_TARGET != TARGET_BRANCH || env.BRANCH_NAME != BRANCH_NAME) {
                        error("Pipeline only runs for PRs from 'develop' to 'feature'.")
                    }
                }
            }
        }

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building the project..."
                // Add build steps here (e.g., Maven, npm, Gradle)
            }
        }

        stage('Test') {
            steps {
                echo "Running tests..."
                // Add test steps here (e.g., JUnit, Jest)
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying application..."
                // Add deployment steps (e.g., Docker, Kubernetes)
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed!"
        }
        success {
            echo "Merge request from develop to feature was successful!"
        }
        failure {
            echo "Build failed, please check the logs."
        }
    }
}

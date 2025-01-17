pipeline {
    agent any
    environment {
        projectGitName = 'my-project'
        DOCKERHUB_USERNAME = credentials('dockerhub-username')
        DOCKERHUB_PASSWORD = credentials('dockerhub-password')
    }
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        echo "Building Docker Image..."
                        org.devops.lb_buildimagen.buildImageDocker(env.projectGitName)
                        echo "Docker Image built successfully."
                    } catch (Exception e) {
                        echo "Error during Docker image build: ${e.message}"
                        error("Stage failed: Build Docker Image")
                    }
                }
            }
        }
        stage('Publish to Docker Hub') {
            steps {
                script {
                    try {
                        echo "Publishing Docker Image to Docker Hub..."
                        org.devops.lb_publicardockerhub.publicarImage(env.projectGitName)
                        echo "Docker Image published successfully."
                    } catch (Exception e) {
                        echo "Error during Docker image publication: ${e.message}"
                        error("Stage failed: Publish to Docker Hub")
                    }
                }
            }
        }
        stage('Deploy Docker Container') {
            steps {
                script {
                    try {
                        echo "Deploying Docker Container..."
                        org.devops.lb_deploydocker.despliegueContenedor(env.projectGitName)
                        echo "Docker Container deployed successfully."
                    } catch (Exception e) {
                        echo "Error during Docker container deployment: ${e.message}"
                        error("Stage failed: Deploy Docker Container")
                    }
                }
            }
        }
        stage('OWASP Analysis') {
            steps {
                script {
                    try {
                        echo "Starting OWASP Analysis..."
                        org.devops.lb_owasp.AnalisisOwasp(env.projectGitName)
                        echo "OWASP Analysis completed successfully."
                    } catch (Exception e) {
                        echo "Error during OWASP Analysis: ${e.message}"
                        error("Stage failed: OWASP Analysis")
                    }
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline completed."
        }
        success {
            echo "Pipeline finished successfully."
        }
        failure {
            echo "Pipeline failed. Check the logs for more details."
        }
    }
}
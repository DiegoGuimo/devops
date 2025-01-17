def call() {
    pipeline {
        agent any
        environment {
            projectGitName = 'my-project'
            
            // Usar la credencial DockerHubUser para Docker Hub
            DOCKERHUB_USERNAME = credentials('DockerHubUser')  // DockerHubUser como credencial para el usuario de DockerHub
            DOCKERHUB_PASSWORD = credentials('DockerHubUser')  // La misma credencial se usa para el password (esto depende de cómo esté configurada)

            // Usar la credencial GitHub-Diego para GitHub
            GITHUB_USERNAME = credentials('GitHub-Diego')  // Credencial para GitHub
            GITHUB_TOKEN = credentials('GitHub-Diego')  // Puede ser el mismo o una credencial separada para el token
        }
        stages {
            stage('Clonar Repositorio GitHub') {
                steps {
                    script {
                        try {
                            echo "Cloning GitHub repository..."
                            // Clonamos el repositorio usando la credencial correcta de GitHub
                            git credentialsId: "${env.GITHUB_TOKEN}", url: "${env.UrlGitHub}", branch: 'master'
                            echo "GitHub repository cloned successfully."
                        } catch (Exception e) {
                            echo "Error during GitHub clone: ${e.message}"
                            error("Stage failed: Cloning GitHub Repository")
                        }
                    }
                }
            }
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
}
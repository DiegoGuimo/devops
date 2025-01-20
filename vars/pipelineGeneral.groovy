import org.devops.lb_buildimagen
import org.devops.lb_deploydocker
import org.devops.lb_owasp
import org.devops.lb_publicardockerhub

def call() {
    pipeline {
        agent any
        tools {
            nodejs 'NodeJS'
        }
        environment {
            GIT_BRANCH_1 = 'master'
            GIT_URL_1 = 'https://github.com/DiegoGuimo/react-test-jenkinsfile.git'
            GIT_URL_2 = 'https://github.com/DiegoGuimo/devops.git'
            projectGitName = 'react-test-jenkinsfile'
            DOCKERHUB_USERNAME = credentials('dockerhub-cred-id')
            DOCKERHUB_PASSWORD = credentials('dockerhub-password-id')
            GITHUB_USERNAME = credentials('GitHub-Diego')
            GITHUB_TOKEN = credentials('GitHub-Diego')
        }
        stages {
            stage('Clonar Repositorio GitHub') {
                steps {
                    script {
                        try {
                            echo "Cloning repositories from GitHub..."
                            git url: "${env.GIT_URL_1}", branch: "${env.GIT_BRANCH_1}"
                            
                            dir('devops') {
                                git url: "${env.GIT_URL_2}", branch: 'develop'
                            }
                            echo "Repositories cloned successfully."
                        } catch (Exception e) {
                            currentBuild.result = 'FAILURE'
                            error "Failed to clone repositories: ${e.getMessage()}"
                        }
                    }
                }
            }
            stage('Build Docker Image') {
                steps {
                    script {
                        try {
                            echo "Building Docker Image..."
                            def buildImagen = new org.devops.lb_buildimagen()
                            buildImagen.buildImageDocker(env.projectGitName)
                            echo "Docker Image built successfully."
                        } catch (Exception e) {
                            currentBuild.result = 'FAILURE'
                            error "Failed to build Docker image: ${e.getMessage()}"
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
                            currentBuild.result = 'FAILURE'
                            error "Failed to publish Docker image to Docker Hub: ${e.getMessage()}"
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
                            currentBuild.result = 'FAILURE'
                            error "Failed to deploy Docker container: ${e.getMessage()}"
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
                            currentBuild.result = 'FAILURE'
                            error "OWASP Analysis failed: ${e.getMessage()}"
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

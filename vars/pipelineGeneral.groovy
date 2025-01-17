import org.devops.lb_buildimagen
import org.devops.lb_deploydocker
import org.devops.lb_owasp
import org.devops.lb_publicardockerhub

def call() {
    pipeline {
        agent any
        environment {
            projectGitName = 'react-test-jenkinsfile'
            // Credenciales de Docker Hub se obtienen desde TOKEN_ID
            DOCKERHUB_USERNAME = credentials('DockerHubUser')
            DOCKERHUB_PASSWORD = credentials('DockerHubUser')

            // Credenciales de GitHub
            GITHUB_USERNAME = credentials('GitHub-Diego')
            GITHUB_TOKEN = credentials('GitHub-Diego')
        }
        stages {
            stage('Validar Variables de Entorno') {
                steps {
                    script {
                        echo "Validando variables de entorno disponibles..."
                        sh 'printenv | sort' // Muestra todas las variables de entorno disponibles para depuración
                    }
                }
            }
            stage('Clonar Repositorio GitHub') {
                steps {
                    script {
                        try {
                            echo "Cloning GitHub repository..."
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
                            def buildImagen = new org.devops.lb_buildimagen()
                            buildImagen.buildImageDocker(env.projectGitName)
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
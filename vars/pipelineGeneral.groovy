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
            stage('Authenticate Docker Hub') {
                steps {
                    script {
                        try {
                            echo "Authenticating to Docker Hub..."
                            withCredentials([usernamePassword(credentialsId: 'DockerHubUser', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                            }
                            echo "Docker Hub authentication successful."
                        } catch (Exception e) {
                            currentBuild.result = 'FAILURE'
                            error "Docker Hub authentication failed: ${e.getMessage()}"
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
                            def publicadorDockerHub = new org.devops.lb_publicardockerhub()
                            publicadorDockerHub.publicarImage(env.projectGitName)
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
                            def deployDocker = new org.devops.lb_deploydocker()
                            deployDocker.despliegueContenedor(env.projectGitName)
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
                            def owasp = new org.devops.lb_owasp()
                            owasp.AnalisisOwasp(env.projectGitName)
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
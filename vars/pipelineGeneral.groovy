def call() {
    pipeline {
        agent any

        tools {
            nodejs 'NodeJS'
        }

        environment {
            PROJECT_NAME = env.GIT_URL.tokenize('/').last().replace('.git', '')
        }

        stages {
            stage('Setup') {
                steps {
                    script {
                        echo "Setting up environment for project: ${env.PROJECT_NAME}"
                    }
                }
            }

            stage('Build Artefacto') {
                steps {
                    script {
                        echo "Cloning repository and installing dependencies"
                        org.devops.lb_buildartefacto.clone(env.nameBranch, env.UrlGitHub)
                        org.devops.lb_buildartefacto.install()
                    }
                }
            }

            stage('Análisis SonarQube') {
                steps {
                    script {
                        echo "Running SonarQube analysis"
                        org.devops.lb_analisissonarqube.analisisSonar(env.PROJECT_NAME, '.')
                    }
                }
            }
        }

        post {
            always {
                echo "Pipeline completed for project: ${env.PROJECT_NAME}"
            }
        }
    }
}
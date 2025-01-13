def call() {
    pipeline {
        agent any

        tools {
            nodejs 'NodeJS' // Asegúrate de que "NodeJS" esté configurado en Jenkins
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
                        echo "Building the artefacto"
                        // Llamada a la librería para la construcción del artefacto
                        org.devops.lb_buildartefacto.build()
                    }
                }
            }

            stage('Análisis SonarQube') {
                steps {
                    script {
                        echo "Running SonarQube analysis"
                        // Llamada a la librería para el análisis de SonarQube
                        org.devops.lb_analisissonarqube.runAnalysis()
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
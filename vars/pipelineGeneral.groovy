def call() {
    pipeline {
        agent any
        tools {
            nodejs 'NodeJS'
        }
        environment {
            GIT_BRANCH_1 = 'master'  // Rama del repositorio donde está el Jenkinsfile
            GIT_URL_1 = 'https://github.com/DiegoGuimo/FrontEnd.git'  // URL del repositorio con el Jenkinsfile
            GIT_URL_2 = 'https://github.com/DiegoGuimo/devops.git'  // URL del repositorio de las bibliotecas
            NodeJS = 'NodeJS'  // Herramienta de Node.js
            SonarScannerHome = 'sonar-scanner'  // Herramienta de SonarScanner
            SOURCE = 'src'  // Directorio de los archivos fuente para el análisis
        }
        stages {
            stage('Clonar Repositorios') {
                steps {
                    script {
                        // Clonamos el repositorio donde está el Jenkinsfile
                        git url: "${env.GIT_URL_1}", branch: "${env.GIT_BRANCH_1}"
                        
                        // Clonamos el repositorio donde están las bibliotecas
                        dir('devops') {
                            git url: "${env.GIT_URL_2}", branch: 'feature'
                        }
                    }
                }
            }
            stage('Clonar y Construir') {
                steps {
                    script {
                        def lb_buildartefacto = load 'devops/src/org/devops/lb_buildartefacto.groovy'

                        lb_buildartefacto.clone()
                        lb_buildartefacto.install()
                    }
                }
            }
            stage('Análisis de SonarQube') {
                steps {
                    script {
                        def lb_analisissonarqube = load 'devops/src/org/devops/lb_analisissonarqube.groovy'
                        lb_analisissonarqube.analisisSonar(env.GIT_BRANCH_1)
                    }
                }
            }
            stage('Pruebas') {
                steps {
                    script {
                        sh 'npm test'
                    }
                }
            }
        }
    }
}
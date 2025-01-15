def call() {
    pipeline {
        agent any
        tools {
            nodejs 'NodeJS'
        }
        environment {
            nameBranch = 'master'  // Personaliza la rama según sea necesario
            UrlGitHub = 'https://github.com/DiegoGuimo/react-test-jenkinsfile.git'
        }
        stages {
            stage('Clonar Repositorios') {
                steps {
                    script {
                        // Clonamos el repositorio del Jenkinsfile
                        git url: "${env.UrlGitHub}", branch: "${env.nameBranch}"
                        
                        // Clonamos el repositorio de las bibliotecas
                        dir('devops') {
                            git url: 'https://github.com/DiegoGuimo/devops.git', branch: 'feature'
                        }
                    }
                }
            }
            stage('Clonar y Construir') {
                steps {
                    script {
                        load 'devops/src/org/devops/lb_buildartefacto.groovy'

                        lb_buildartefacto.clone()
                        lb_buildartefacto.install()
                    }
                }
            }
            stage('Análisis de SonarQube') {
                steps {
                    script {
                        analisisSonar(env.nameBranch)
                    }
                }
            }
            stage('Pruebas') {
                steps {
                    script {
                        testCoverage()
                    }
                }
            }
        }
    }
}
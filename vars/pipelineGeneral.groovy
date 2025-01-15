// pipelineGeneral.groovy
def pipelineGeneral() {
    pipeline {
        agent any  // Ejecutar en cualquier agente disponible
        tools {
            nodejs 'NodeJS'  // Asegúrate de que esta herramienta esté configurada en Jenkins
        }
        environment {
            nameBranch = 'master'  // Puedes personalizar esto según tus necesidades
            UrlGitHub = 'https://github.com/DiegoGuimo/react-test-jenkinsfile.git'
        }
        stages {
            stage('Clonar y Construir') {
                steps {
                    script {
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

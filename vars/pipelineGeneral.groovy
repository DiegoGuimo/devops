def call() {
    tools {
        nodejs 'NodeJS'
    }

    environment {
        nameBranch = "${env.GIT_BRANCH ?: 'master'}"
        UrlGitHub = "${env.GIT_URL ?: 'https://github.com/DiegoGuimo/FrontEnd.git'}"
    }

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

    post {
        always {
            echo "Pipeline completed for project: ${env.PROJECT_NAME}"
        }
    }
}
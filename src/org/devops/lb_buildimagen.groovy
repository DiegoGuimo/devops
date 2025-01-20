stage('Build Docker Image') {
    steps {
        script {
            try {
                echo "Building Docker Image..."
                def buildImagen = new org.devops.lb_buildimagen()
                buildImagen.buildImageDocker(env.projectGitName)

                // Verificar las imágenes disponibles después de la construcción
                sh 'docker images'

                echo "Docker Image built successfully."
            } catch (Exception e) {
                currentBuild.result = 'FAILURE'
                error "Failed to build Docker image: ${e.getMessage()}"
            }
        }
    }
}
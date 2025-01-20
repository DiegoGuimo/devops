package org.devops

def despliegueContenedor (projectGitName) {
    try {
        // Detener y eliminar el contenedor previo si existe
        echo "Stopping and removing any existing container..."
        sh "docker stop ${projectGitName} || true"
        sh "docker rm ${projectGitName} || true"
        
        // Verificar si la imagen ya está disponible localmente
        def imageExists = sh(script: "docker images -q diegoguimo182/${projectGitName}:latest", returnStatus: true) == 0
        
        if (!imageExists) {
            // Descargar la imagen de Docker Hub si no está disponible localmente
            echo "Pulling Docker image from Docker Hub..."
            sh "docker pull diegoguimo182/${projectGitName}:latest"
        } else {
            echo "Image ${projectGitName}:latest already exists locally."
        }
        
        // Ejecutar el contenedor
        echo "Running Docker container..."
        sh """
            docker run -d --name ${projectGitName} \
            --network=${env.NameNetwork} -p 5174:5174 \
            --user root diegoguimo182/${projectGitName}:latest
        """
        
        echo "Docker container ${projectGitName} deployed successfully."

    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        error "Failed to deploy Docker container: ${e.getMessage()}"
    }
}
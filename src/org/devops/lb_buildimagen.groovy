package org.devops

def buildImageDocker(projectName) {
    def sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9.-]", "-")
    
    // Asegurarse de que la imagen se construya correctamente con el nombre adecuado
    echo "Building Docker image for ${env.DOCKER_USERNAME}/${sanitizedProjectName}"

    // Construir la imagen con el nombre completo
    sh """
        docker build --no-cache -t ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest .
    """
    echo "Docker image ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest built successfully."
}

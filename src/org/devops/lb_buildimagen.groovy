package org.devops

def buildImageDocker(projectName) {
    def sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9.-]", "-")

    // Asegurarse de que la imagen se construya correctamente con el nombre adecuado
    sh """
    echo "Docker username: ${env.DOCKER_USERNAME}"
    echo "Building image with the name: ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest"
    """

    // Construir la imagen con el nombre correcto
    sh """
        docker build --no-cache -t ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest .
    """

    echo "Sanitized project name for Docker Image: ${sanitizedProjectName}"
}
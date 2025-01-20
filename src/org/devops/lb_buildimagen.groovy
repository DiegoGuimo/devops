package org.devops

def buildImageDocker(projectName) {
    def sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9.-]", "-")
    
    sh """
    echo "Docker username: ${env.DOCKER_USERNAME}"
    echo "Project name: ${projectName}"
    """

    sh """
        docker build --no-cache -t ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest .
    """
    echo "Sanitized project name for Docker Image: ${sanitizedProjectName}"
}
package org.devops

def buildImageDocker(projectName) {
    def sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9.-]", "-")
    
    sh """
    echo "Docker username: ${env.DOCKERHUB_USERNAME_USR}"
    echo "Project name: ${projectName}"
    """

    sh """
        docker build --no-cache -t ${env.DOCKERHUB_USERNAME}/${sanitizedProjectName}:latest .
    """
    echo "Sanitized project name for Docker Image: ${sanitizedProjectName}"
}
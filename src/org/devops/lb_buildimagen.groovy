package org.devops

def buildImageDocker(projectName) {
    def sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9.-]", "-")
    sh """
        docker build --no-cache -t ${env.DOCKERHUB_USERNAME}/${sanitizedProjectName} .
    """
    echo "Sanitized project name for Docker Image: ${sanitizedProjectName}"

}
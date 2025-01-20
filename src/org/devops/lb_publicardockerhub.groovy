package org.devops

def publicarImage(projectGitName) {
    def sanitizedProjectName = projectGitName.replaceAll("[^a-zA-Z0-9.-]", "-")
    
    withCredentials([usernamePassword(credentialsId: "${env.TOKEN_ID}",
    passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
        sh "docker login -u ${env.DOCKERHUB_USERNAME} -p ${env.DOCKERHUB_PASSWORD}"
        // Etiquetar la imagen con el nombre completo y luego subirla a Docker Hub
        sh "docker tag ${env.DOCKER_USERNAME}/${sanitizedProjectName}:latest ${env.DOCKERHUB_USERNAME}/${sanitizedProjectName}:latest"
        sh "docker push ${env.DOCKERHUB_USERNAME}/${sanitizedProjectName}:latest"
    }
}
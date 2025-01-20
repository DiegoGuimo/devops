package org.devops

def publicarImage(projectGitName) {
    withCredentials([usernamePassword(credentialsId: "${env.TOKEN_ID}", passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
        sh "docker login -u ${env.DOCKERHUB_USERNAME} -p ${env.DOCKERHUB_PASSWORD}"
        sh "docker tag ${projectGitName}:latest ${env.DOCKERHUB_USERNAME}/${projectGitName}:latest"
        sh "docker push ${env.DOCKERHUB_USERNAME}/${projectGitName}:latest"
    }
}
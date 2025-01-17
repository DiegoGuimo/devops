package org.devops

def buildImageDocker (projectGitName){
    sh "docker build --no-cache -t ${env.DOCKERHUB_USERNAME}/${projectGitName} ."
}
    

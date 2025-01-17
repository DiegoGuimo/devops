package org.devops

def buildImageDocker (projectGitName){
    sh "docker build -t josecorreav/${projectGitName} ."
}
    

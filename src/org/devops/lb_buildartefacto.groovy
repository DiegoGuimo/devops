package org.devops

def clone(String branch, String repoUrl) {
    try {
        echo "Cloning branch: ${branch} from repository: ${repoUrl}"
        git branch: branch, url: repoUrl
    } catch (Exception e) {
        error "Failed to clone repository: ${e.message}"
    }
}

def install() {
    try {
        echo "Installing dependencies..."
        sh 'npm install'
    } catch (Exception e) {
        error "Failed to install dependencies: ${e.message}"
    }
}
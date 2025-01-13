package org.devops

def clone(String branch, String repoUrl) {
    git branch: branch, url: repoUrl
}

def install() {
    sh 'npm install'
}
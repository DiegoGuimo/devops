package org.devops

def clone(){
    git branch: "${env.nameBranch}", url: "${env.UrlGitHub}"
}

def install(){
    sh 'npm install'
}
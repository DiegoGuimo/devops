package org.devops

def despliegueContenedor (projectGitName) {
    // Detener y eliminar el contenedor previo si existe
    sh "docker stop ${projectGitName} || true"
    sh "docker rm ${projectGitName} || true"
    
    // Descargar y ejecutar el contenedor
    echo "Pulling Docker image from Docker Hub..."
    sh "docker pull diegoguimo182/${projectGitName}:latest"
    
    echo "Running Docker container..."
    sh """
        docker run -d --name ${projectGitName} \
        --network=${env.NameNetwork} -p 5174:5174 \
        --user root diegoguimo182/${projectGitName}:latest
    """
}
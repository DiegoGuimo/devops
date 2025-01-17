package org.devops

def despliegueContenedor (projectGitName) {
    // Detener y eliminar el contenedor previo si existe
    sh "docker stop ${projectGitName} || true"
    sh "docker rm ${projectGitName} || true"
    // Descargar y ejecutar el contenedor
    sh "docker pull josecorreav/react-test-jenkinsfile"
    sh""" docker run -d --name ${projectGitName} \
    --network=${env.NameNetwork} -p 5174:5174 \
    --user root josecorreav/${projectGitName}
    """
}
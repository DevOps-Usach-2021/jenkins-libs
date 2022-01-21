def saludo(Map params) {
    sh "echo 'hola ${params.Nombre}'"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}

def printEnv() {
    sh "echo 'current env.GIT_BRANCH ${env.GIT_BRANCH}'"
    sh "echo 'current GIT_BRANCH ${GIT_BRANCH}'"

    // sh "echo '${env.getEnvironment()}'"
    sh 'printenv'
    // sh "echo '${env.getEnvironment().BUILD_DISPLAY_NAME}'"
}
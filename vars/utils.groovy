def saludo(Map params) {
    sh "echo 'hola ${params.Nombre}'"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}

def printEnv() {
    sh "echo 'current Branch: ${env.BRANCH_NAME}'"
    sh "echo 'HELLO!!! ${BRANCH_NAME}'"

    // sh "echo '${env.getEnvironment()}'"
    sh 'printenv'
    // sh "echo '${env.getEnvironment().BUILD_DISPLAY_NAME}'"
}
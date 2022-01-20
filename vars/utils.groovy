def saludo(Map params) {
    sh "echo 'hola ${params.Nombre}'"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}

def printEnv() {
    sh "echo '${env.getEnvironment()}'"
    sh "echo '${env.getEnvironment().BUILD_DISPLAY_NAME}'"
}
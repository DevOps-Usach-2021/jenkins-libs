def saludo(Map params) {
    sh "echo 'hola ${params.Nombre}'"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}

def printEnv() {
    sh "echo '${env.BRANCH_NAME}'"
}
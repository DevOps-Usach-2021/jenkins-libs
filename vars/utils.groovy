def saludo(Map params) {
    sh "hola ${params.nombre}"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}
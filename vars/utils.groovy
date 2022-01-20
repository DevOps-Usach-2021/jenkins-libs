def saludo(Map params) {
    sh "hola ${params.Nombre}"
}

def despedida(Map params) {
    sh "adios mundo cruel"
}
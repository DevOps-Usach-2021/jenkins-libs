def printEnv() {
    sh 'printenv'
}

def getVersion(String message) {
    try {
        return message.split('::')[1]
    } catch(Exception ex) {
        throw new Exception("Debe agregar '::<version>' al final del mensaje de commit")
    }
}

def parseJson(String jsonString) {
    def mapObj = readJSON text: jsonString

    return mapObj
}
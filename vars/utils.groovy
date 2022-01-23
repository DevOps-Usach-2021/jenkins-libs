def printEnv() {
    sh 'printenv'
}

def getVersionFromCommit(String message) {
    try {
        return message.split('::')[1]
    } catch(Exception ex) {
        throw new Exception("Debe agregar '::<version>' al final del mensaje de commit")
    }
}

def getVersionFromBranch(String branchName) {
    try {
        return message.split('release-v')[1]..replaceAll("-",".")
    } catch(Exception ex) {
        throw new Exception("Debe utilizar el formato 'release-v<major>-<minor><patch>' en el nombre de rama")
    }
}

def parseJson(String jsonString) {
    def mapObj = readJSON text: jsonString

    return mapObj
}
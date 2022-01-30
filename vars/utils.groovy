def printEnv() {
    sh 'printenv'
}

// Deprecated
def getVersionFromCommit(String message) {
    try {
        return message.split('::')[1]
    } catch(Exception ex) {
        throw new Exception("Debe agregar '::<version>' al final del mensaje de commit")
    }
}

def validateConventionalCommit(String message) {
    if (
        !message.startsWith('chore:') &&
        !message.startsWith('fix:') &&
        !message.startsWith('feat:') &&
        !message.startsWith('BREAKING CHANGE:') &&
        ) {
            throw new Exception("El commit debe comenzar con alguna de las siguientes keywords: <chore: | fix: | feat: | BREAKING CHANGE:>")
        }
}

def getVersionFromBranch(String branchName) {
    try {
        print("looking version for branch " + branchName)
        return branchName.split('release-v')[1].replaceAll("-",".")
    } catch(Exception ex) {
        throw new Exception("Debe utilizar el formato 'release-v<major>-<minor>-<patch>' en el nombre de rama")
    }
}

def parseJson(String jsonString) {
    def mapObj = readJSON text: jsonString

    return mapObj
}
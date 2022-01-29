// ciPipeline.groovy
def call(String stages) {

    loadEnvironment()
    switch (stages) {
        case ~/.*build.*/:
            print("stages: " + stages)
            build()
        case ~/.*staticAnalysis.*/:
            print("stages: " + stages)
            staticAnalysis()
        case ~/.*uploadArtifact.*/:
            print("stages: " + stages)
            uploadArtifact()
        case ~/.*generateRelease.*/:
            if (env.BRANCH_NAME == "develop") {
                print("stages: " + stages)
                generateRelease()
            }
    }
}

def loadEnvironment() {
    stage('1. Load environment') {
        CURRENT_STAGE = STAGE_NAME
        utils.printEnv()
        env.REPOSITORY = GIT_URL.split('github.com/')[1].split('.git')[0]
        PAYLOAD = github.getCommitPayload()
        def payload = utils.parseJson(PAYLOAD)
        currentBuild.displayName = REPOSITORY + '-' + BRANCH_NAME + '-' + BUILD_NUMBER
        currentBuild.description = payload.commit.message
        env.ARTIFACT_VERSION = utils.getVersionFromCommit(payload.commit.message)
        print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
    }
}

def build() {
    stage('Paso 2: Compliar') {
        CURRENT_STAGE = STAGE_NAME
        maven.compile()
    }

    stage('Paso 3: Testear') {
        CURRENT_STAGE = STAGE_NAME
        maven.test()
    }

    stage('Paso 4: Build .Jar') {
        CURRENT_STAGE = STAGE_NAME
        maven.buildJar()
    }
}

def staticAnalysis() {
    stage('Paso 5: SonarQube analysis') {
        CURRENT_STAGE = STAGE_NAME
        sonarqube.analyze()
    }
}

def uploadArtifact() {
    stage('Paso 6: Subir a Nexus') {
        CURRENT_STAGE = STAGE_NAME
        nexus.uploadArtifact()
    }
}

def generateRelease() {
    stage('Paso 6: Generar rama Release') {
        CURRENT_STAGE = STAGE_NAME
        sh "echo 'Generando rama release'"
        github.createReleaseBranch()
    }
}
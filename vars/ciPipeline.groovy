// ciPipeline.groovy
def call(String stages) {

    loadEnvironment()

    if (stages ==~ /.*build.*|all/) {
        build()
    }

    if (stages ==~ /.*staticAnalysis.*|all/) {
        staticAnalysis()
    }

    if (stages ==~ /.*uploadArtifact.*|all/) {
        uploadArtifact()
    }

    if (stages ==~ /.*generateRelease.*|all/ && env.BRANCH_NAME == "develop") {
        generateRelease()
    }
}

def loadEnvironment() {
    stage('1. Load environment') {
        env.CURRENT_STAGE = STAGE_NAME
        utils.printEnv()
        env.REPOSITORY = GIT_URL.split('github.com/')[1].split('.git')[0]
        PAYLOAD = github.getCommitPayload()
        def payload = utils.parseJson(PAYLOAD)
        currentBuild.displayName = REPOSITORY + '-' + BRANCH_NAME + '-' + BUILD_NUMBER
        currentBuild.description = payload.commit.message
        utils.validateConventionalCommit(payload.commit.message)
        env.INC_TYPE = payload.commit.message.split(':')[0].trim()
        env.COMMIT_MSG = payload.commit.message.tokenize(":").drop(1).join(":")
        if (BRANCH_NAME.startsWith('feature-')) {
            env.ARTIFACT_VERSION =  nextVersion(preRelease: "alpha.$BUILD_ID", nonAnnotatedTag: true)
        } else {
            env.ARTIFACT_VERSION = nextVersion(nonAnnotatedTag: true)
        }
        print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
    }
}

def build() {
    stage('Paso 2: Compliar') {
        env.CURRENT_STAGE = STAGE_NAME
        maven.compile()
    }

    stage('Paso 3: Testear') {
        env.CURRENT_STAGE = STAGE_NAME
        maven.test()
    }

    stage('Paso 4: Build .Jar') {
        env.CURRENT_STAGE = STAGE_NAME
        maven.buildJar()
    }
}

def staticAnalysis() {
    stage('Paso 5: SonarQube analysis') {
        env.CURRENT_STAGE = STAGE_NAME
        sonarqube.analyze()
    }
}

def uploadArtifact() {
    stage('Paso 6: Subir a Nexus') {
        env.CURRENT_STAGE = STAGE_NAME
        nexus.uploadArtifact()
    }
}

def generateRelease() {
    stage('Paso 6: Generar rama Release') {
        env.CURRENT_STAGE = STAGE_NAME
        sh "echo 'Generando rama release'"
        github.createReleaseBranch()
    }
}
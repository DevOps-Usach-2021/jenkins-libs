// cdPipeline.groovy
def call(String params) {
    loadEnvironment()
    switch (stages) {
        case ~/.*downloadArtifact.*|all/:
            downloadArtifact()
        case ~/.*runApp.*|all/:
            runApp()
        case ~/.*testAlive.*|all/:
            testAlive()
        case ~/.*merge.*|all/:
            mergeAndTag()
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
        env.ARTIFACT_VERSION = utils.getVersionFromBranch(BRANCH_NAME)
    }
}

def downloadArtifact() {
    stage('Paso 2: Download Artifact') {
        CURRENT_STAGE = STAGE_NAME
        nexus.downloadArtifact()
    }
}

def runApp() {
    stage('Paso 3: Levantar Springboot APP') {
        CURRENT_STAGE = STAGE_NAME
        maven.runApp()
    }

    stage('Paso 4: Dormir(Esperar 10sg) ') {
        CURRENT_STAGE = STAGE_NAME
        sh 'sleep 10'
    }
}

def testAlive() {
    stage('Paso 5: Test Alive Service - Testing Application!') {
        CURRENT_STAGE = STAGE_NAME
        sh 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
    }
}

def mergeAndTag() {
    stage('Paso 6: Merge to main') {
        CURRENT_STAGE = STAGE_NAME
        // github.mergeBranch('develop')
        github.mergeBranch('main')
    }

    stage('Paso 7: Tag') {
        CURRENT_STAGE = STAGE_NAME
        github.tagMainBranch()
    }
}
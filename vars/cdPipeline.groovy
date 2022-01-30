// cdPipeline.groovy
def call(String stages) {
    loadEnvironment()
    if (stages ==~ /.*downloadArtifact.*|all/) {
        downloadArtifact()
    }

    if (stages ==~ /.*runApp.*|all/) {
        runApp()
    }

    if (stages ==~ /.*testAlive.*|all/) {
        testAlive()
    }

    if (stages ==~ /.*merge.*|all/) {
        mergeAndTag()
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
        env.ARTIFACT_VERSION = utils.getVersionFromBranch(BRANCH_NAME)
    }
}

def downloadArtifact() {
    stage('Paso 2: Download Artifact') {
        env.CURRENT_STAGE = STAGE_NAME
        nexus.downloadArtifact()
    }
}

def runApp() {
    stage('Paso 3: Levantar Springboot APP') {
        env.CURRENT_STAGE = STAGE_NAME
        maven.runApp()
    }

    stage('Paso 4: Dormir(Esperar 10sg) ') {
        env.CURRENT_STAGE = STAGE_NAME
        sh 'sleep 10'
    }
}

def testAlive() {
    stage('Paso 5: Test Alive Service - Testing Application!') {
        env.CURRENT_STAGE = STAGE_NAME
        sh 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
    }
}

def mergeAndTag() {
    stage('Paso 6: Merge to main') {
        env.CURRENT_STAGE = STAGE_NAME
        // github.mergeBranch('develop')
        github.mergeBranch('main')
    }

    stage('Paso 7: Tag') {
        env.CURRENT_STAGE = STAGE_NAME
        github.tagMainBranch()
    }
}
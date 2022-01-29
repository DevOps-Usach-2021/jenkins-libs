// ciPipeline.groovy
def call() {
    stage('1. Load environment') {
        utils.printEnv()
        env.REPOSITORY = GIT_URL.split('github.com/')[1].split('.git')[0]
        PAYLOAD = github.getCommitPayload()
        def payload = utils.parseJson(PAYLOAD)
        currentBuild.displayName = REPOSITORY + '-' + BRANCH_NAME + '-' + BUILD_NUMBER
        currentBuild.description = payload.commit.message
        env.ARTIFACT_VERSION = utils.getVersionFromCommit(payload.commit.message)
        print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
        // post {
        //     failure {
        //         script {
        //             env.FAIL_STAGE_NAME = STAGE_NAME
        //         }
        //     }
        // }
    }

    stage('Paso 2: Compliar') {
        maven.compile()
        // post {
        //     failure {
        //         script {
        //             env.FAIL_STAGE_NAME = STAGE_NAME
        //         }
        //     }
        // }
    }

    stage('Paso 3: Testear') {
        maven.test()
        // post {
        //     failure {
        //         script {
        //             env.FAIL_STAGE_NAME = STAGE_NAME
        //         }
        //     }
        // }
    }

    stage('Paso 4: Build .Jar') {
        maven.buildJar()
        // post {
        //     failure {
        //         script {
        //             env.FAIL_STAGE_NAME = STAGE_NAME
        //         }
        //     }
        // }
    }

    stage('Paso 5: SonarQube analysis') {
        sonarqube.analyze()
        post {
            //record the test results and archive the jar file.
            success {
                script {
                    nexus.uploadArtifact()
                }
            }
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 6: Generar rama Release') {
        when {
            branch 'develop'
        }
        sh "echo 'Generando rama release'"
        github.createReleaseBranch()
        // post {
        //     failure {
        //         script {
        //             env.FAIL_STAGE_NAME = STAGE_NAME
        //         }
        //     }
        // }
    }
}

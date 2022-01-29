// cdPipeline.groovy
def call() {
    stage('1. Load environment') {
        steps {
            script {
                utils.printEnv()
                env.REPOSITORY = GIT_URL.split('github.com/')[1].split('.git')[0]
                PAYLOAD = github.getCommitPayload()
                def payload = utils.parseJson(PAYLOAD)
                currentBuild.displayName = REPOSITORY + '-' + BRANCH_NAME + '-' + BUILD_NUMBER
                currentBuild.description = payload.commit.message
                env.ARTIFACT_VERSION = utils.getVersionFromBranch(BRANCH_NAME)
                // print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
            }
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 2: Download Artifact') {
        steps {
            script {
                nexus.downloadArtifact()
            }
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 3: Levantar Springboot APP') {
        steps {
            script {
                maven.runApp()
            }
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 4: Dormir(Esperar 10sg) ') {
        steps {
            sh 'sleep 10'
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 5: Test Alive Service - Testing Application!') {
        steps {
            sh 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 6: Merge to main') {
        steps {
            script {
                // github.mergeBranch('develop')
                github.mergeBranch('main')
            }
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }

    stage('Paso 7: Tag') {
        steps {
            script {
                // github.mergeBranch('develop')
                github.tagMainBranch()
            }
        }
        post {
            failure {
                script {
                    env.FAIL_STAGE_NAME = STAGE_NAME
                }
            }
        }
    }
}


// cdPipeline.groovy
def call() {
    pipeline {
        agent any
        stages {

            stage('1. Load environment') {
                steps {
                    script {
                        env.REPOSITORY = GIT_URL.split('github.com/')[1].split('.git')[0]
                        currentBuild.displayName = REPOSITORY + '-' + BRANCH_NAME + '-' + BUILD_NUMBER
                        env.ARTIFACT_VERSION = utils.getVersionFromBranch(BRANCH_NAME)
                        print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
                        utils.printEnv()
                    }
                }
            }

            stage('Paso 2: Download Artifact') {
                steps {
                    script {
                        nexus.downloadArtifact()
                    }
                }
            }

            stage('Paso 3: Levantar Springboot APP') {
                steps {
                    script {
                        maven.runApp()
                    }
                }
            }

            stage('Paso 4: Dormir(Esperar 10sg) ') {
                steps {
                    sh 'sleep 10'
                }
            }

            stage('Paso 5: Test Alive Service - Testing Application!') {
                steps {
                    sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
                }
            }
        }
        post {
            success {
                script {
                    sh "echo 'CD pipeline success'"

                    PROCESS_ID = sh (
                        script: "jps | grep DevOpsUsach2022 | tr -s ' ' | cut -d ' ' -f 1",
                        returnStdout: true
                    ).trim()

                    sh "kill $PROCESS_ID"
                }
            }
            failure {
                sh "echo 'CD pipeline failure'"
            }
        }
    }
}


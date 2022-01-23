// cdPipeline.groovy
def call() {
    pipeline {
        agent any
        stages {

            stage('Paso 5: Download Artifact') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USER', passwordVariable: 'PASSWORD')]) {
                        script {
                            env.ARTIFACT_VERSION = utils.getVersionFromBranch(BRANCH_NAME)
                            sh 'curl -X GET -u $USER:$PASSWORD https://nexus.devopslab.cl/repository/devops-usach-nexus/com/devopsusach2022/DevOpsUsach2022/${ARTIFACT_VERSION}/DevOpsUsach2022-${ARTIFACT_VERSION}.jar -O'
                            sh "ls"
                        }
                    }
                }
            }

            stage('Paso 6: Levantar Springboot APP') {
                steps {
                    script {
                        maven.runApp()
                    }
                }
            }

            stage('Paso 7: Dormir(Esperar 10sg) ') {
                steps {
                    sh 'sleep 10'
                }
            }

            stage('Paso 8: Test Alive Service - Testing Application!') {
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


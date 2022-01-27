// ciPipeline.groovy
def call() {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('github-token')
        }
        stages {

            stage('1. Load environment') {
                steps {
                    script {
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
                post {
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }

            stage('Paso 2: Compliar') {
                steps {
                    script {
                        maven.compile()
                    }
                }
                post {
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }

            stage('Paso 3: Testear') {
                steps {
                    script {
                        maven.test()
                    }
                }
                post {
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }

            stage('Paso 4: Build .Jar') {
                steps {
                    script {
                        maven.buildJar()
                    }
                }
                post {
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }

            stage('Paso 5: SonarQube analysis') {
                steps {
                    script {
                        sonarqube.analyze()
                    }
                }
                post {
                    //record the test results and archive the jar file.
                    success {
                        script {
                            nexus.uploadArtifact()
                        }
                    }
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }

            stage('Paso 6: Generar rama Release') {
                when {
                    branch 'develop'
                }
                steps {
                    script {
                        sh "echo 'Generando rama release'"
                        github.createReleaseBranch()
                    }
                }
                post {
                    failure {
                        env.FAIL_STAGE_NAME = STAGE_NAME
                    }
                }
            }
        }
        post {
            always {
                sendNotifications currentBuild.result
            }
            success {
                script {
                    if (env.BRANCH_NAME.startsWith('feature-')) {
                        github.createPullRequest()
                    }
                }
            }
            failure {
                sh "echo 'CI pipeline failure'"
            }
        }
    }
}

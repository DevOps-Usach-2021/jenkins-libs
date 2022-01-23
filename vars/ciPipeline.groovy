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
                        // print ("ARTIFACT_VERSION: " + ARTIFACT_VERSION)
                    }
                }
            }

            stage('Paso 2: Compliar') {
                steps {
                    script {
                        maven.compile()
                    }
                }
            }

            stage('Paso 3: Testear') {
                steps {
                    script {
                        maven.test()
                    }
                }
            }

            stage('Paso 4: Build .Jar') {
                steps {
                    script {
                        maven.buildJar()
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
                }
            }

            stage('Paso 6: Generar rama Release') {
                when {
                    branch 'develop'
                }
                steps {
                    sh "echo 'Generando rama release'"
                }
            }
        }
        post {
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

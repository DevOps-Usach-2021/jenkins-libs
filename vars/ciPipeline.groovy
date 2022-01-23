// ciPipeline.groovy
def call() {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('github-token')
            PAYLOAD = ''
            ARTIFACT_VERSION = ''
        }
        stages {

            stage('1. Load env') {
                steps {
                    script {
                        PAYLOAD = github.getCommitPayload()
                        ARTIFACT_VERSION = utils.getVersion(utils.parseJson(PAYLOAD).commit.message)
                        utils.printEnv()
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
                    github.createPullRequest()
                }
            }
            failure {
                sh "echo 'CI pipeline failure'"
            }
        }
    }
}

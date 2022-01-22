// ciPipeline.groovy
def call() {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('github-token')
        }
        stages {

            stage('1. Print env') {
                steps {
                    script {
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
                if (env.BRAN)
                sh "echo 'CI pipeline success'"
                sh """prNumber=`curl -X POST -d '{"title":"new feature: $BRANCH_NAME ","head":"$BRANCH_NAME","base":"develop"}' -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls`"""
                sh """curl -X POST -H "Accept: application/vnd.github.v3+json" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls/${prNumber}/requested_reviewers -d '{"reviewers":["jesusdonoso","fgutierrez27"]}'"""
            }
            failure {
                sh "echo 'CI pipeline failure'"
            }
        }
    }
}

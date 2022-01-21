// mainOipeline.groovy
def call(Map pipelineParams) {
if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-')) {
    pipeline {
        agent any
        stages {


                stage('1. Print env') {
                    steps {
                        script {
                            utils.printEnv()
                        }
                    }
                }

                stage('Paso 2: Compliar') {
                    when {
                        // expression {
                        //     GIT_BRANCH = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                        //     return (GIT_BRANCH.startsWith('develop') || GIT_BRANCH.startsWith('feature-'))
                        // }
                        expression {
                            return env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-');
                        }
                    }
                    steps {
                        script {
                            sh "echo 'Compile Code!'"
                            // Run Maven on a Unix agent.
                            sh 'mvn clean compile -e'
                        }
                    }
                }

                stage('Paso 3: Testear') {
                    when {
                        expression {
                            return env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-');
                        }
                    }
                    steps {
                        script {
                            sh "echo 'Test Code!'"
                            // Run Maven on a Unix agent.
                            sh 'mvn clean test -e'
                        }
                    }
                }

                stage('Paso 4: Build .Jar') {
                    when {
                        expression {
                            return env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-');
                        }
                    }
                    steps {
                        script {
                            sh "echo 'Build .Jar!'"
                            // Run Maven on a Unix agent.
                            sh 'mvn clean package -e'
                        }
                    }
                }

                stage('Paso 5: SonarQube analysis') {
                    when {
                        expression {
                            return env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-');
                        }
                    }
                    steps {
                        withSonarQubeEnv('SonarQubeUsach') { // You can override the credential to be used
                            sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=lab3-ci-develop'
                        }
                    }
                }

                stage('Paso 6: Levantar Springboot APP') {
                    when {
                        branch 'release-*'
                    }
                    steps {
                        sh 'mvn spring-boot:run &'
                    }
                }

                stage('Paso 7: Dormir(Esperar 10sg) ') {
                    when {
                        branch 'release-*'
                    }
                    steps {
                        sh 'sleep 100'
                    }
                }

                stage('Paso 8: Test Alive Service - Testing Application!') {
                    when {
                        branch 'release-*'
                    }
                    steps {
                        sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
                    }
                }
            }
        }
        post {
            success {
                sh "echo 'fase success'"
            }
            failure {
                sh "echo 'fase failure'"
            }
        }
    }
}

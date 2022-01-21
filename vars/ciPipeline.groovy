// ciPipeline.groovy
def call() {
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
                steps {
                    script {
                        sh "echo 'Compile Code!'"
                        // Run Maven on a Unix agent.
                        sh 'mvn clean compile -e'
                    }
                }
            }

            stage('Paso 3: Testear') {
                steps {
                    script {
                        sh "echo 'Test Code!'"
                        // Run Maven on a Unix agent.
                        sh 'mvn clean test -e'
                    }
                }
            }

            stage('Paso 4: Build .Jar') {
                steps {
                    script {
                        sh "echo 'Build .Jar!'"
                        // Run Maven on a Unix agent.
                        sh 'mvn clean package -e'
                    }
                }
            }

            stage('Paso 5: SonarQube analysis') {
                steps {
                    withSonarQubeEnv('SonarQubeUsach') { // You can override the credential to be used
                        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=lab3-ci-develop'
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
                sh "echo 'CI pipeline success'"
            }
            failure {
                sh "echo 'CI pipeline failure'"
            }
        }
    }

    return
}

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
                        maven.package()
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
                sh "echo 'CI pipeline success'"
            }
            failure {
                sh "echo 'CI pipeline failure'"
            }
        }
    }
}

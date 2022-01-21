// cdPipeline.groovy
def call() {
    pipeline {
        agent any
        stages {

            stage('Paso 6: Levantar Springboot APP') {
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
        post {
            success {
                sh "echo 'CD pipeline success'"
            }
            failure {
                sh "echo 'CD pipeline failure'"
            }
        }
    }
}


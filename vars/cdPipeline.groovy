// cdPipeline.groovy
def call(String) {
    pipeline {
        agent any
        stages {
            // stage('Paso 1: Download and checkout') {
            //     steps {
            //         checkout(
            //             [$class: 'GitSCM',
            //             branches: [[name: 'feature-estadomundial' ]],
            //             userRemoteConfigs: [[url: 'https://github.com/jesusdonoso/ms-iclab.git']]])
            //     }
            // }
    //         stage('Paso 2: Compliar') {
    //             steps {
    //                 script {
    //                     sh "echo 'Compile Code!'"
    //                     // Run Maven on a Unix agent.
    //                     sh 'mvn clean compile -e'
    //                 }
    //             }
    //         }
    //         stage('Paso 3: Testear') {
    //             steps {
    //                 script {
    //                     sh "echo 'Test Code!'"
    //                     // Run Maven on a Unix agent.
    //                     sh 'mvn clean test -e'
    //                 }
    //             }
    //         }
    //         stage('Paso 4: Build .Jar') {
    //             steps {
    //                 script {
    //                     sh "echo 'Build .Jar!'"
    //                     // Run Maven on a Unix agent.
    //                     sh 'mvn clean package -e'
    //                 }
    //             }
    //         }
    //         stage('SonarQube analysis') {
    //             steps {
    //                 withSonarQubeEnv('sonarqube') { // You can override the credential to be used
    //                     sh "mvn clean verify sonar:sonar \
    //   -Dsonar.projectKey=feature-mundial"
    //                 }
    //                 withSonarQubeEnv('sonarqube') { // This expands the evironment variables SONAR_CONFIG_NAME, SONAR_HOST_URL, SONAR_AUTH_TOKEN that can be used by any script.
    //                     println "${env.SONAR_HOST_URL}"
    //                 }
    //             }
    //         }
            stage('Paso 5: Levantar Springboot APP') {
                steps {
                    sh 'mvn spring-boot:run &'
                }
            }
            stage('Paso 6: Dormir(Esperar 10sg) ') {
                steps {
                    sh 'sleep 100'
                }
            }
            stage('Paso 7: Test Alive Service - Testing Application!') {
                steps {
                    sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
                }
            }
        }
        post {
            always {
                sh "echo 'fase always executed post'"
                sh """curl -X POST -d '{"title":"new feature","head":"feature-estadomundial","base":"develop"}' -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token tokendegithub" https://api.github.com/repos/jesusdonoso/ms-iclab/pulls"""
            }
            success {
                sh "echo 'fase success'"
            }
            failure {
                sh "echo 'fase failure'"
            }
        }
    }
}

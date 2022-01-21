def analyze() {
    withSonarQubeEnv('SonarQubeUsach') { // You can override the credential to be used
        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=lab3-ci-develop'
    }
}
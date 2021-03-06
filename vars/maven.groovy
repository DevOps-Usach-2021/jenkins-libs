def compile() {
    sh "echo 'Compile Code!'"
    // Run Maven on a Unix agent.
    sh 'mvn clean compile -e'
}

def test() {
    sh "echo 'Test Code!'"
    // Run Maven on a Unix agent.
    sh 'mvn clean test -e'
}

def buildJar() {
    sh "echo 'Build .Jar!'"
    // Run Maven on a Unix agent.
    sh 'mvn clean package -e'
}

def runApp() {
    sh 'nohup java -jar DevOpsUsach2022-${ARTIFACT_VERSION}.jar & >/dev/null'
}
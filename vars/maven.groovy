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

// def package() {
//     sh "echo 'Build .Jar!'"
//     // Run Maven on a Unix agent.
//     sh 'mvn clean package -e'
// }
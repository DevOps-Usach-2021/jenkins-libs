def uploadArtifact() {
    //archiveArtifacts artifacts:'build/*.jar'
    nexusPublisher nexusInstanceId: 'Nexus',
    nexusRepositoryId: 'maven-lab3-usach',
    packages: [
        [
            $class: 'MavenPackage',
            mavenAssetList: [
                [
                    classifier: '',
                    extension: '',
                    filePath: 'build/DevOpsUsach2022-0.0.0.jar']
                ],
            mavenCoordinate: [
                artifactId: 'DevOpsUsach2022',
                groupId: 'com.devopsusach2022',
                packaging: 'jar',
                version: ARTIFACT_VERSION
            ]
        ]
    ]
}

def downloadArtifact() {
    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USER', passwordVariable: 'PASSWORD')]) {
        sh 'curl -X GET -u $USER:$PASSWORD https://nexus.devopslab.cl/repository/maven-lab3-usach/com/devopsusach2022/DevOpsUsach2022/${ARTIFACT_VERSION}/DevOpsUsach2022-${ARTIFACT_VERSION}.jar -O'
        sh "ls"
    }
}
// mainPipeline.groovy
// ciPipeline.groovy
def call(Map pipelineParams) {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('github-token')
        }
        stages {
            stage("Pipeline") {
                steps {
                    script {
                        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-')) {
                            ciPipeline()
                        }
                        if (env.BRANCH_NAME.startsWith('release-')) {
                            cdPipeline()
                        }
                    }
                }
            }
        }
        post {
            always {
                sendNotifications currentBuild.result
            }
            success {
                script {
                    if (env.BRANCH_NAME.startsWith('feature-')) {
                        github.createPullRequest()
                    }
                }
            }
            failure {
                sh "echo 'Pipeline failure'"
            }
        }
    }
}
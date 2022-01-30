// mainPipeline.groovy
// ciPipeline.groovy
def call(Map pipelineParams) {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('github-token')
            CURRENT_VERSION = currentVersion()
            NEXT_VERSION = nextVersion(nonAnnotatedTag: true)
        }
        parameters {
            string (
                name: 'stages',
                defaultValue: 'all',
                trim: true
            )
        }
        stages {
            stage("Pipeline") {
                steps {
                    script {
                        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-')) {
                            ciPipeline(params.stages)
                        }
                        if (env.BRANCH_NAME.startsWith('release-')) {
                            cdPipeline(params.stages)
                        }
                    }
                }
            }
        }
        post {
            always {
                script {
                    slack.sendNotification currentBuild.result
                }
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
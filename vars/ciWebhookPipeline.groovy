// ciPipeline.groovy
def call() {
    pipeline {
        agent any
        triggers {
            GenericTrigger(
                genericVariables: [
                    [key: 'ref', value: '$.ref'],
                    [key: 'repository', regexpFilter: '[^a-z_-]', value: '$.repository']
                ],
                causeString: 'Triggered on $ref',
                token: 'abc123',
                regexpFilterExpression: '',
                regexpFilterText: '$repository refs/heads/' + BRANCH_NAME,
                printContributedVariables: true,
                printPostContent: true
            )
        }
        stages {
            stage('Some step') {
                steps {
                    sh "echo $ref"
                }
            }
        }
    }
}

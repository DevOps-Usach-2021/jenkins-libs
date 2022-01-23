// ciPipeline.groovy
def call() {
    pipeline {
        agent any
        triggers {
            GenericTrigger(
            genericVariables: [
            [key: 'ref', value: '$.ref']
            ],
            causeString: 'Triggered on $ref',
            token: 'abc123',
            regexpFilterExpression: '',
            regexpFilterText: env.BRANCH_NAME,
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

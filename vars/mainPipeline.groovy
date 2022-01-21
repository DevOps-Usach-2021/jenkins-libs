// mainOipeline.groovy
def call(Map pipelineParams) {
    if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME.startsWith('feature-')) {
        ciPipeline()
    }
    if (env.BRANCH_NAME.startsWith('release-')) {
        cdPipeline()
    }
}
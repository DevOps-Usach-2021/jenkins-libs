def call(Map pipelineParams) {
    if ("${GIT_BRANCH}".startsWith('origin/develop') || "${GIT_BRANCH}".startsWith('origin/feature')) {
        ciPipeline(pipelineParams)
    }

    if ("${GIT_BRANCH}".startsWith('origin/release')) {
        cdPipeline(pipelineParams)
    }
}

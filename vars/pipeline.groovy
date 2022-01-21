def call(Map params) {
    if ("${GIT_BRANCH}".startsWith('origin/develop') || "${GIT_BRANCH}".startsWith('origin/feature')) {
        ciPipeline(params)
    }

    if ("${GIT_BRANCH}".startsWith('origin/release')) {
        cdPipeline(params)
    }
}

def call() {
    // if ("${GIT_BRANCH}".startsWith('origin/develop') || "${GIT_BRANCH}".startsWith('origin/feature')) {
        ciPipeline()
    // }

    // if ("${GIT_BRANCH}".startsWith('origin/release')) {
    //     cdPipeline([:])
    // }
}

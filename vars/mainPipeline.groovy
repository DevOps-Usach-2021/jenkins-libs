def call(Map config) {
    if ("${GIT_BRANCH}".startsWith('origin/develop') || "${GIT_BRANCH}".startsWith('origin/feature')) {
        sh "echo 'Branch: ${GIT_BRANCH} -> Going through ciPipeline'"
        ciPipeline
    }
}





// def init() {


//     // if ("${GIT_BRANCH}".startsWith('origin/release')) {
//     //     cdPipeline([:])
//     // }
// }

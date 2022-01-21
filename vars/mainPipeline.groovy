def call(Map config) {
    echo "${env.getEnvironment()}"
    if (env.GIT_BRANCH.startsWith('origin/develop') || env.GIT_BRANCH.startsWith('origin/feature')) {
        echo "Branch: ${env.GIT_BRANCH}"
        ciPipeline
    }
}





// def init() {


//     // if ("${GIT_BRANCH}".startsWith('origin/release')) {
//     //     cdPipeline([:])
//     // }
// }

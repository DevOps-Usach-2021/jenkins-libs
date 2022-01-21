def call(Map config) {
    echo "Branch: ${GIT_BRANCH}"

    if (env.GIT_BRANCH.startsWith('origin/develop') || env.GIT_BRANCH.startsWith('origin/feature')) {
        echo "Branch: ${GIT_BRANCH}"
        ciPipeline
    }
}





// def init() {


//     // if ("${GIT_BRANCH}".startsWith('origin/release')) {
//     //     cdPipeline([:])
//     // }
// }

def call(Map config) {
    if (env.BRANCH_NAME.startsWith('origin/develop') || env.BRANCH_NAME.startsWith('origin/feature')) {
        echo "Branch: ${env.BRANCH_NAME}"
        ciPipeline
    }
}





// def init() {


//     // if ("${GIT_BRANCH}".startsWith('origin/release')) {
//     //     cdPipeline([:])
//     // }
// }

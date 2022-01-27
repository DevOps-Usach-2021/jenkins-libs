#!/usr/bin/env groovy
def call(String buildResult) {

  if (FAIL_STAGE_NAME == null) {
    env.FAIL_STAGE_NAME = 'Validación Final'
  }

  def tipoPipeline = "CI"

  if (env.BRANCH_NAME.startsWith('release-v')) {
    tipoPipeline = "CD"
  }


  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][Pipeline: ${tipoPipeline}][Rama: ${env.BRANCH_NAME}][Resultado: OK]")
  }
  if( buildResult == "FAILURE" ) {
    slackSend (color: "RED", colorCode: "#FF0000", message: "[Grupo1][Pipeline: ${tipoPipeline}][Rama: ${env.GIT_BRANCH}][Stage: ${env.FAIL_STAGE_NAME}][Resultado: Failed]")
  }
}

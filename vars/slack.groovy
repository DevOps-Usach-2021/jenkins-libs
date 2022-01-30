#!/usr/bin/env groovy
def sendNotification(String buildResult) {

  if (!env.FAIL_STAGE_NAME) {
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
    slackSend (color: "danger", message: "[Grupo1][Pipeline: ${tipoPipeline}][Rama: ${env.GIT_BRANCH}][Stage: ${env.CURRENT_STAGE}][Resultado: Failed]")
  }
}

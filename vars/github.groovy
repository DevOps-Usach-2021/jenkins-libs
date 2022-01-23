def createPullRequest() {
    sh "echo 'CI pipeline success'"
    PR_NUMBER = sh (
        script: 
            '''
                curl -X POST -d '{"title":"new feature: $BRANCH_NAME ","head":"$BRANCH_NAME","base":"develop"}' -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls | jq '.number'
            ''',
        returnStdout: true
    ).trim()
    sh """
        curl -X POST -H "Accept: application/vnd.github.v3+json" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls/$PR_NUMBER/requested_reviewers -d '{"reviewers":["jesusdonoso","anguitait", "carlostognarell", "MFrizR", "MrOscarDanilo"]}'
    """
}

def Map getCommitPayload() {
    def payload = sh (
        script: 
        '''
            curl -X GET -H "Accept: application/vnd.github.v3+json" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/commits/$GIT_COMMIT
        ''',
        returnStdout: true
    ).trim()

    print(payload)

    return payload
}
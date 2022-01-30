def createPullRequest() {
    sh "echo 'CI pipeline success'"
    PR_NUMBER = sh (
        script: 
            """
                curl -X POST -d '{"title":"new feature: $INC_TYPE: $ARTIFACT_VERSION", "body": "$COMMIT_MSG", "head":"$BRANCH_NAME","base":"develop"}' -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls | jq '.number'
            """,
        returnStdout: true
    ).trim()
    print('PR_NUMBER: ' + PR_NUMBER)
    sh """
        curl -X POST -H "Accept: application/vnd.github.v3+json" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/DevOps-Usach-2021/ms-iclab/pulls/$PR_NUMBER/requested_reviewers -d '{"reviewers":["jesusdonoso","anguitait", "carlostognarell", "MFrizR", "MrOscarDanilo", "fernandogutierrez27"]}'
    """
}

def Map getCommitPayload() {
    def payload = sh (
        script: 
        """
            curl -X GET -H "Accept: application/vnd.github.v3+json" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/commits/$GIT_COMMIT
        """,
        returnStdout: true
    ).trim()

    print(payload)

    return payload
}

def createReleaseBranch() {
    sh "echo 'CI pipeline success'"
    SHA = sh (
        script:
            '''
                curl -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/git/refs/heads/$BRANCH_NAME | jq -r '.object.sha'
            ''',
        returnStdout: true
    ).trim()

    print (SHA)
    def branchName = ARTIFACT_VERSION.replaceAll("\\.","-")
    sh (
        script:
        """
            curl -X POST -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN"  https://api.github.com/repos/$REPOSITORY/git/refs -d '{"ref": "refs/heads/release-v$branchName", "sha": "$SHA"}'
        """,
    )
}


def mergeBranch(String baseBranch) {
    print ("merging branch" + BRANCH_NAME + ' into ' + baseBranch)
    SHA = sh (
        script:
            '''
                curl -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/git/refs/heads/$BRANCH_NAME | jq -r '.object.sha'
            ''',
        returnStdout: true
    ).trim()

    print (SHA)

    sh """
        curl -X POST -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/merges -d '{"head":"$SHA","base":"$baseBranch"}'
    """
}

def tagMainBranch() {
    print ("tagging main branch")
    SHA = sh (
        script:
            '''
                curl -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/git/refs/heads/main | jq -r '.object.sha'
            ''',
        returnStdout: true
    ).trim()

    print (SHA)

    SHA_TAG = sh (
        script:
        """
            curl -X POST -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/git/tags -d '{"tag":"$ARTIFACT_VERSION", "message":"$ARTIFACT_VERSION", "object": "$SHA", "type": "commit"}' | jq -r '.sha'
        """,
        returnStdout: true

    ).trim()

    print('SHA_TAG: ' + SHA_TAG)

    sh """
        curl -X POST -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $GITHUB_TOKEN" https://api.github.com/repos/$REPOSITORY/git/refs -d '{"ref":"refs/tags/$ARTIFACT_VERSION", "sha": "$SHA_TAG"}'
    """
}
pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                script {
                    def to = emailextrecipients([
                        [$class: 'CulpritsRecipientProvider'],
                        [$class: 'DevelopersRecipientProvider'],
                        [$class: 'RequesterRecipientProvider']
                    ])

                    currentBuild.result = 'SUCCESS'
                    // set variables
                    def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} ${currentBuild.result}"
                    def content = '${JELLY_SCRIPT,template="html"}'

                    // send email
                    if (to != null && !to.isEmpty()) {
                        emailext(body: content, mimeType: 'text/html',
                        replyTo: '$DEFAULT_REPLYTO', subject: subject,
                        to: to, attachLog: true)
                    }
                }
            }
        }
    }
}

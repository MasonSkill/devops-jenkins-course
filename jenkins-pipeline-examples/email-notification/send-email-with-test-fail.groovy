pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'So far so good...'
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        echo 'A test has failed!'
                        sh 'exit 1'
                    } catch (Exception e) {
                        // Mark build as failed
                        currentBuild.result = 'FAILURE'
                        // Mark current build as a failure and throw the error
                        error('A test failed!')
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (currentBuild.result == 'FAILURE') {
                    // Set variables
                    def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} ${currentBuild.result}"
                    def content = '${JELLY_SCRIPT,template="html"}'

                    // Send email
                    emailext(
                        body: content,
                        mimeType: 'text/html',
                        replyTo: '$DEFAULT_REPLYTO',
                        subject: subject,
                        to: emailextrecipients([
                            [$class: 'CulpritsRecipientProvider'],
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                        ]),
                        attachLog: true
                    )
                }
            }
        }
    }
}

pipeline {
    agent any
    tools {
        jfrog 'jfrog'
    }
    stages {
        stage('Test') {
            steps {
                // Show the installed version of JFrog CLI.
                jf '-v'

                // Show the configured JFrog Platform instances.
                jf 'c show'

                // Ping Artifactory.
                jf 'rt ping'
            }
        }
    }
}

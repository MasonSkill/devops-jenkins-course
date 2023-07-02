pipeline {
    agent {
        docker {
            image 'node:18.16.1-alpine3.17'
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'node -v'
                sh 'npm -v'
            }
        }
    }
}

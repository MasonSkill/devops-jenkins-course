pipeline {
    agent any

    stages {
        stage('Find Node.js path') {
            steps {
                script {
                    def nodeHome = tool 'nodejs'
                    def nodeBin = "${nodeHome}/bin/node"
                    sh "echo Node.js binary path: ${nodeBin}"
                }
            }
        }

        stage('Find Maven path') {
            steps {
                script {
                    def mavenHome = tool 'maven'
                    def mavenBin = "${mavenHome}/bin/mvn"
                    sh "echo Maven binary path: ${mavenBin}"
                }
            }
        }

        stage('Find Docker path') {
            steps {
                script {
                    def dockerHome = tool 'docker'
                    def dockerBin = "${dockerHome}/bin/docker"
                    sh "echo Docker binary path: ${dockerBin}"
                }
            }
        }

    }
}

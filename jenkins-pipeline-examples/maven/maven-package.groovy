pipeline {
    agent any
    tools {
        jfrog 'jfrog'
        maven 'maven'
    }

    environment {
        RUNTIME_DIR = 'maven-examples/maven-example'
    }

    stages {
        stage('Clone') {
            steps {
                git branch: 'master', url: 'https://github.com/MasonSkill/devops-jfrog-examples.git'
            }
        }

        stage('Build') {
            steps {
                dir("${RUNTIME_DIR}") {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Copy Jars') {
            steps {
                dir("${RUNTIME_DIR}") {
                    // Create the target folder to hold the JAR files
                    sh 'mkdir target'

                    // Copy JAR files from the specified paths to the target folder
                    sh 'cp multi1/target/*.jar target/'
                    sh 'cp multi2/target/*.jar target/'
                }
            }
        }
        stage('Publish build output to Jenkins') {
            steps {
                dir("${RUNTIME_DIR}") {
                    // Archive the Jars, so that it can be downloaded in Jenkins build page
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the pipeline finishes
            cleanWs()
        }
    }
}

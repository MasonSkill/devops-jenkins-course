# Plugins and Tools Configuration in Jenkins

## Docker
### Plugin
Docker Pipeline, Docker Commons Plugin

### Tool
Name: docker
Install automatically: Download from docker.com
Docker version: latest

### Usage in pipeline

    pipeline {
        agent any
        tools {
            'org.jenkinsci.plugins.docker.commons.tools.DockerTool' 'docker'
        }
        stages {
            stage('Test') {
                steps {
                    sh 'docker version'
                }
            }
        }
    }


## Maven
### Plugin
N/A

### Tool
Name: maven
Install automatically: Install from Apache
Version: 3.9.3

### Usage in pipeline

    pipeline {
        agent any
        tools {
            maven 'maven'
        }
        stages {
            stage('Test') {
                steps {
                    sh 'mvn -v'
                }
            }
        }
    }


## Nodejs
### Plugin
NodeJS(NodeJS Plugin)

### Tool
Name: nodejs
Install automatically: Install from nodejs.org
Version: NodeJS 18.16.1

### Usage in pipeline

    pipeline {
        agent any
        tools {
            nodejs 'nodejs'
        }
        stages {
            stage('Test') {
                steps {
                    sh 'node -v'
                }
            }
        }
    }

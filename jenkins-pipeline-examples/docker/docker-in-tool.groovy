// Mason: DockerTool does not work well with docker agent. So it is recommended to pre-install docker cli with Jenksin dockerfile
pipeline {
    agent any
    tools {
        // a bit ugly because there is no `@Symbol` annotation for the DockerTool
        // see the discussion about this in PR 77 and PR 52:
        // https://github.com/jenkinsci/docker-commons-plugin/pull/77#discussion_r280910822
        // https://github.com/jenkinsci/docker-commons-plugin/pull/52
        'org.jenkinsci.plugins.docker.commons.tools.DockerTool' 'docker'
    }
    //   environment {
    //     DOCKER_CERT_PATH = credentials('id-for-a-docker-cred')
    //   }
    stages {
        stage('Test') {
            steps {
                // Review docker version
                sh 'docker version'

                // List the container list in host docker daemon(if shared to jenksin)
                sh 'docker ps -a'

                // List the docker image in host docker daemon(if shared to jenksin)
                sh 'docker image ls'
            }
        }
    }
}

# Learn DevOps

CI/CD with Jenkins using Pipelines and Docker

This is the course material for the Jenkins Course on Udemy ([Learn DevOps: CI/CD with Jenkins using Pipelines and Docker](https://www.udemy.com/learn-devops-ci-cd-with-jenkins-using-pipelines-and-docker/?couponCode=JENKINS_GIT))

# Bellow are detailed steps updated by Mason
# Topic 1: Install and setup Jenkins in Ubuntu
## Create Ubuntu Instance in AWS
Ubuntu Server 22.04 LTS (HVM), SSD Volume Type, 64-bit(x86)
Canonical, Ubuntu, 22.04 LTS, amd64 jammy image build on 2023-05-16
2CPU, 4G Memory

## Install Docker in Ubuntu server

### Uninstall old versions docker prior to installation
https://docs.docker.com/engine/install/ubuntu/#uninstall-old-versions

### Install docker with shell
The shell includes a set of commands to install docker in ubuntu server.

Switch to root user

    sudo -i

Get install_jenkins_ubuntu.sh file for docker installation and jenkins preparation. The sh file is based on https://docs.docker.com/engine/install/ubuntu/

    wget https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/scripts/install_jenkins_ubuntu.sh

Run install_jenkins_ubuntu.sh with with root

    sh install_jenkins_ubuntu.sh

### Uninstall Docker Engine
https://docs.docker.com/engine/install/ubuntu/#uninstall-docker-engine


## Create docker compose file for Docker, Jenkins and JFrog service

Swith back ubuntu user

    su - ubuntu
    
or

    exit

Create run time folder like /home/ubuntu/xxx/xxx_devops_runtime

**Alternative 1: [Docker in docker] [Recommended] In Jenkins pipeline, Use docker container runs in ubunbu docker as docker runtime**
Download docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/docker-compose-ubuntu-docker-in-docker.yml

**Alternative 2: [Docker in host] [Not Recommended] In Jenkins pipeline, use ubunbu docker server as docker runtime**
Download docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/docker-compose-ubuntu-docker-in-host.yml

**Download docker file**

    wget https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/Dockerfile-jenkins

# Topic 2: Run Jenkins with docker compose

There are two services related with jenkins:
- One is 'docker' service which runs in a container and controled by jenksin pipeline.
- The other is 'jenkins' service which runs jenkins itself.
## Start docker and jenkins service 

    docker compose up -d docker jenkins

## Stop docker and jenkins service 

    docker compose down docker jenkins

## Configure domain and nginx reverse proxy server
- Configure domain with SSL in nginx like jenkins.your_domain.com
Detailed steps ignored

- Configure reverse proxy in nginx
Map jenkins.your_domain.com to 127.0.0.1:8080
Detailed steps ignored

## Visit Jenkins UI
Remote - [https://jenkins.your_domain.com](https://jenkins.your_domain.com)

Local - [http://127.0.0.1:8080](http://127.0.0.1:8080)

## Setup Jenkins
Setup Jenkins follwing jenkins official doc
https://www.jenkins.io/doc/book/installing/docker/#setup-wizard

Note: to get default password in this case

    sudo cat /var/jenkins_home/secrets/initialAdminPassword

## Check if docker cli is installed in Jenkins container
Connect to Jenkins with normal user

    docker exec -it jenkins /bin/bash

Check docker cli

    docker -v

## Docker common commands
Compose up

    docker-compose up -d

Compose down

    docker-compose down

Prune without volumes

    docker system prune --all --force

Prune all including volumes

    docker system prune --all --force --volumes

# Topic 3: Jenkins Plugins and Tools Configuration

## Docker Pipeline plugin
The plugin is used to run docker in pipeline
The plugin is installed after setting up Jenksin. Follow the following steps if it is not installed.

1. Open your Jenkins instance in a web browser.
2. Navigate to "Manage Jenkins" > "Manage Plugins".
3. In the "Available" tab, search for "Docker Pipeline" using the search bar.
4. Check the checkbox next to "Docker Pipeline" in the search results.
5. Click the "Install without restart" button to install the plugin.

## Tools
In docker-based Jenkins, some basic runtime tools are not installed. The tools like maven can ben run in Jenkins server installed by tool or in a separate docker container. 
Which way is better depends on the use cases. Bellow are some recommendations based on my practice.
`Y` or `N` means wether it is a recommended option.
| Tool Name | Manual Installation | Jenkins Plugin Name | Seperate Docker |
| - | - | - | - |
| docker | install_jenkins_ubuntu.sh - Y | docker - N | image: docker:dind - Y |
| maven | N | maven - Y | image 'maven:3.9.0-eclipse-temurin-11' - Y |
| nodejs | N | NodeJS(NodeJS Plugin) - Y | image 'node:18.16.1-alpine3.17' - Y |

## Tools - Docker
### Plugin Name
Docker Pipeline, Docker Commons Plugin

### Tool Configuration
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


## Tools - Maven
### Plugin Name
Installed automatically by Jenksin setup

### Tool Configuration
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


## Tools - Nodejs
### Plugin Name
NodeJS(NodeJS Plugin)

### Tool Configuration
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

# Topic 4: Run JFrog with docker compose

## Start jfrog service 

    docker compose up -d jfrog

## Stop jfrog service 

    docker compose down jfrog

## Configure domain and nginx reverse proxy server
- Configure domain with SSL in nginx like jfrog.your_domain.com
Detailed steps ignored

- Configure reverse proxy in nginx
Map jfrog.your_domain.com to 127.0.0.1:8082
Detailed steps ignored

## Visit JFrog UI
Remote - [https://jfrog.your_domain.com](https://jfrog.your_domain.com)

Local - [http://127.0.0.1:8082](http://127.0.0.1:8082)

## Setup JFrog
- Login with default user
    User: `admin`
    Password: `password`

- Reset Admin Password
    User: `admin`
    Password: `your_new_password`

- Set base URL
    https://jfrog.your_domain.com

- Next, and Skip to Finish

By now, jfrog is ready to use

# Topic 5: JFrog and Jenkins integration
## Install JFrog plugin in Jenkins
The official website for JFrog plugin
[https://github.com/jfrog/jenkins-jfrog-plugin#readme](https://github.com/jfrog/jenkins-jfrog-plugin#readme)
### Plugin Name
JFrog Plugin

### Plugin Configuration
Refer to [https://github.com/jfrog/jenkins-jfrog-plugin#installing-and-configuring-the-plugin](https://github.com/jfrog/jenkins-jfrog-plugin#installing-and-configuring-the-plugin)

Server ID: `jfrog-instance`

Remote - JFrog Platform URL: `https://jfrog.your_domain.com`

Local - JFrog Platform URL: `http://jfrog:8082` (Jenkins and JFrog are in seperate container, so Jenkins access JFrog service through http://jfrog:8082 rather than http://127.0.0.1:8082)

Credentials: the JFrog `user/password` (Use JFrog admin user for test, but create a deploy user with deployment access in staging and production env.)

Advanced Configuration
JFrog Artifactory URL: `http://jfrog:8082/artifactory` (in Remote mode, upload to artifactory using internal network rather than public network. DNS proxy does not allow too large file upload through public network)
Allow HTTP Connections: `Checked`

### Tool Configuration
Refer to [https://github.com/jfrog/jenkins-jfrog-plugin#configuring-jfrog-cli-as-a-tool](https://github.com/jfrog/jenkins-jfrog-plugin#configuring-jfrog-cli-as-a-tool)

Name: jfrog
Install automatically: Install from releases.jfrog.io 
Docker version: 

### Check JFrog CLI Tool
Run [jenkins-pipeline-examples/jfrog/jfrog-in-tool.groovy](./jenkins-pipeline-examples/jfrog/jfrog-in-tool.groovy) pipeline to check if JFrog CLI is configured correctly.

### Create maven repository in JFrog
Create Local repository named `my-local-repo` in JFrog portal
Repository Key: my-local-repo
Repository Layout: maven-2-default

### Run JFrog Maven example
Run [jenkins-pipeline-examples/jfrog/jfrog-maven.groovy](./jenkins-pipeline-examples/jfrog/jfrog-maven.groovy) pipeline to 
1. Run a standard maven project
2. Package the project to generate jars
3. Publish the output to JFrog artifactory
4. Go to JFrog `Artifactory/Builds/jfrog-maven-package/<build_number>` to check the jar output

# Topic 6: Github SSH configuration
We are going to create a SSH key in Jenkins container, then configure them in Jenksin and Github.

Note: If you have only one Jenkins node and you want a simpler setup, it can be reasonable to put the SSH key directly inside the Jenkins Docker container. 

Refer to [Setup SSH between Jenkins and Github](https://levelup.gitconnected.com/setup-ssh-between-jenkins-and-github-e4d7d226b271)

## Create SSH key
1. Connect to Jenkins container with root user

Connect to Jenkins with normal user

    docker exec -it jenkins /bin/bash

2. Generate SSH Key

Generate SSH Key

    mkdir ~/.ssh

    cd ~/.ssh

    ssh-keygen -t rsa -b 4096 -C "your_email@example.com"

3. View Private Key

View Private Key

    cat id_rsa.pub 

Then copy it

4. ADD ECDSA key finderprint
You have to go to some temp folder in Jenkins container then run

    git clone "your private github"

Then it will ask you to add ECDSA key fingerprint

### Note:
It is not necessary to generate SSH key in Jenkins server. In this case, you'll need to do
- Create SSH credential as bellow
- For SCM mode, specify SSH credential
- For clone code in pipeline, do credential configuration
```
stage('Clone') {
    agent any
    steps {
        git branch: 'dev',
        credentialsId: 'my-git-credential',
        url: 'git@github.com:your_private_repository.git'
    }
}
```

## Configure in Github
Copy the content and add new SSH Key in github

Title: ssh-jenkins
Key type: Authentication Key
Key: `<The content copied from id_rsa.pub>`

## Configure in Jenkins
Create a new Git SSH credential when try to configure Git in SCM pipeline

ID: my-github
Username: git email
Private Key: `<The content copied from id_rsa>` - Note it is a private key the content from id_rsa not id_rsa.pub

# Topic 7: Jenkins pipeline examples
There are some pipeline examples under ./jenkins-pipeline-examples/ folder. Some of the pipelines used Tool runtime white others uses the runtime in docker:

## Docker examples
- [jenkins-pipeline-examples/docker/docker-in-tool.groovy](jenkins-pipeline-examples/docker/docker-in-tool.groovy): run docker cli in Jenkins tool(not recommended as docker cli is very basic so it is installed in Jenkins docker instead)

## Email Notification examples
- [jenkins-pipeline-examples/email-notification/send-email.groovy](jenkins-pipeline-examples/email-notification/send-email.groovy): Send email
    - Register an email in https://mailtrap.io which support email sandbox
    - Install 'Email Extension Plugin' plugin under 'Dashboard > Manage Jenkins > Plugins' in Jenkins portal.
    - Go to 'Dashboard > Manage Jenkins > Credentials > System > Extended E-mail Notification'
    - Configure 
        - SMTP server: sandbox.smtp.mailtrap.io
        - SMTP Port: 2525
        - Credentials: create a credential with the username and password from mailtrap.
    - Run send-email.groovy pipeline in Jenkins
    - Go to mailtrap 'Email Testing > inboxes' to check the email. e.g. 'send-email - Build #8 SUCCESS'
- [jenkins-pipeline-examples/email-notification/send-email-with-test-fail](jenkins-pipeline-examples/email-notification/send-email-with-test-fail): Send email for a failed build

### Email plugin comparision: [Mailer](https://plugins.jenkins.io/mailer/) vs. [Email Extension](https://plugins.jenkins.io/email-ext/)
Though mailer plugging is good for sending the basic alerts from Jenkins it does not provide advanced functionality like sending an attachment or sending a build log over email. There is another advanced plugging available in Jenkins which is called the Email Extension Plugin...

Refer to [Jenkins pipeline email notification | How to send email in 2022](https://naiveskill.com/jenkins-pipeline-email-notification/)

## Maven examples
- [jenkins-pipeline-examples/maven/maven-in-docker.groovy](jenkins-pipeline-examples/maven/maven-in-docker.groovy): run maven in docker
- [jenkins-pipeline-examples/maven/maven-in-tool.groovy](jenkins-pipeline-examples/maven/maven-in-tool.groovy): run maven in Jenkins tool
- [jenkins-pipeline-examples/maven/maven-package.groovy](jenkins-pipeline-examples/maven/maven-package.groovy): run maven project and publish jar files in Jenkins build

## Node examples
- [jenkins-pipeline-examples/node/node-in-docker.groovy](jenkins-pipeline-examples/node/node-in-docker.groovy): run node in docker
- [jenkins-pipeline-examples/node/node-in-tool.groovy](jenkins-pipeline-examples/node/node-in-tool.groovy): run node in Jenkins tool

## JFrog examples
- [jenkins-pipeline-examples/jfrog/jfrog-in-tool.groovy](jenkins-pipeline-examples/jfrog/jfrog-in-tool.groovy): check JFrog tool configuration
- [jenkins-pipeline-examples/jfrog/jfrog-maven-package.groovy](jenkins-pipeline-examples/jfrog/jfrog-maven-package.groovy): run maven project and publish jar files in JFrog repository

## Other examples
- [jenkins-pipeline-examples/tools-path/find_tool_path.groovy](jenkins-pipeline-examples/tools-path/find_tool_path.groovy): find tools installation path

# Topic 8: Container Connection
## Docker Container
Connect to Docker (root as default)

    docker exec -it docker sh

## Jenkins Container
Connect to Jenkins with normal user

    docker exec -it jenkins /bin/bash

OR

    docker exec -it jenkins bash

OR Connect to Jenkins with root user
    
    docker exec -u 0 -it jenkins /bin/bash

OR

    docker exec -u 0 -it jenkins bash

## JFrog Container
Connect to JFrog with normal user

    docker exec -it jfrog bash

OR Connect to JFrog with root user
    
    docker exec -u 0 -it jfrog bash


# Topic 9: Add to known_host in Jenkins container

To fix "Host key verification failed" or "No ECDSA host key is known for github.com" issue

Add EC2 connection to known_hosts

    ssh-keyscan remote_ip >> ~/.ssh/known_hosts

Add EC2 connection to known_hosts

    ssh-keyscan internal_ip >> ~/.ssh/known_hosts

Add Github to known_hosts

    ssh-keyscan github.com >> ~/.ssh/known_hosts

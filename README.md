# Learn DevOps

CI/CD with Jenkins using Pipelines and Docker

This is the course material for the Jenkins Course on Udemy ([Learn DevOps: CI/CD with Jenkins using Pipelines and Docker](https://www.udemy.com/learn-devops-ci-cd-with-jenkins-using-pipelines-and-docker/?couponCode=JENKINS_GIT))

# Bellow are detailed steps updated by Mason
# Section 1: Install and setup Jenksin in Ubuntu
## In AWS, create Ubuntu 22.04 LTS
Ubuntu Server 22.04 LTS (HVM), SSD Volume Type, 64-bit(x86)
Canonical, Ubuntu, 22.04 LTS, amd64 jammy image build on 2023-05-16
2CPU, 4G Memory

## Login with user ubuntu and install Docker

### Uninstall old versions before install
https://docs.docker.com/engine/install/ubuntu/#uninstall-old-versions

### Install with shell
Switch to root user

    sudo -i

Get install_jenkins_ubuntu.sh file for docker installation and jenkins preparation. The sh file is based on https://docs.docker.com/engine/install/ubuntu/

    wget https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/scripts/install_jenkins_ubuntu.sh

Run install_jenkins_ubuntu.sh with with root

    sh install_jenkins_ubuntu.sh

### Uninstall Docker Engine
https://docs.docker.com/engine/install/ubuntu/#uninstall-docker-engine


## Create docker compose file

Swith back ubuntu user

    su - ubuntu
    
or

    exit

Create run time folder like /home/ubuntu/xxx/xxx_devops_runtime

**[Docker in docker] [Recommended] In Jenkins pipeline, Use docker container runs in ubunbu docker as docker runtime**
Get docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/docker-compose-ubuntu-docker-in-docker.yml

**[Docker in host] In Jenkins pipeline, use ubunbu docker server as docker runtime**
Get docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/docker-compose-ubuntu-docker-in-host.yml

Get docker file

    wget https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/Dockerfile-ubuntu

## Start Jenksin with docker compose

    docker compose up -d 

## Visit Jenksin UI
http://your_ip:8080

Suggestions:
- Open port 8080 in firewall
- Configure domain and SSL to support https

## Setup Jenkins
Setup Jenkins follwing jenkins official doc
https://www.jenkins.io/doc/book/installing/docker/#setup-wizard

Note: to get default password in this case

    sudo cat /var/jenkins_home/secrets/initialAdminPassword

## Check if docker cli is installed in Jenkins container
Connect to Jenkins with normal user

    docker exec -it jenkins /bin/bash

OR Connect to Jenkins with root user
    
    docker exec -u 0 -it jenkins /bin/bash

Check docker cli

    docker -v

## Install Docker Pipeline plugin
1. Open your Jenkins instance in a web browser.
2. Navigate to "Manage Jenkins" > "Manage Plugins".
3. In the "Available" tab, search for "Docker Pipeline" using the search bar.
4. Check the checkbox next to "Docker Pipeline" in the search results.
5. Click the "Install without restart" button to install the plugin.


## Docker common commands
Compose up

    docker-compose up -d

Compose down

    docker-compose down

Prune without volumes

    docker system prune --all --force

Prune all including volumes

    docker system prune --all --force --volumes

# Section 2: Plugins and Tools Configuration in Jenkins
In docker-based Jenksin, some basic runtime tools like are not installed. When we need to them, there are two alternatives.
'Y' or 'N' means wether it is recommended options
| Tool Name | Manual Installation | Jenksin Plugin Name | Seperate Docker |
| - | - |- | - |
| docker | N | docker - N | image: docker:dind - Y |
| maven | N | maven - Y | image 'maven:3.9.0-eclipse-temurin-11' - Y |
| nodejs | N | NodeJS(NodeJS Plugin) - Y | image 'node:18.16.1-alpine3.17' - Y |

## Tool - Docker
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


## Tool - Maven
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


## Tool - Nodejs
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

# Section 3: Jenksin pipeline examples
There are some pipeline examples under ./jenkins-pipeline-examples/ folder. Some of the pipelines used Tool runtime white others uses the runtime in docker:
- docker/docker-in-tool.groovy: run docker cli in jenksin tool(not recommended as docker cli is very basic so it is installed in jenksin docker instead)
- email-notification/send-email.groovy: Configure email send email to jenksin admin. 
    - Register an email in https://mailtrap.io which support email sandbox
    - Install 'Email Extension Plugin' plugin under 'Dashboard > Manage Jenkins > Plugins' in Jenkins portal.
    - Go to 'Dashboard > Manage Jenkins > Credentials > System > Extended E-mail Notification'
    - Configure 
        - SMTP server: sandbox.smtp.mailtrap.io
        - SMTP Port: 2525
        - Credentials: create a credential with the username and password from mailtrap.
    - Run send-email.groovy pipeline in Jenkins
    - Go to mailtrap 'Email Testing > inboxes' to check the email. e.g. 'send-email - Build #8 SUCCESS'
- maven/maven-in-docker.groovy: run maven in docker
- maven/maven-in-tool.groovy: run maven in jenksin tool
- node/node-in-docker.groovy: run node in docker
- node/node-in-tool.groovy: run node in jenksin tool
- tools-path/find_tool_path.groovy: find tools installation path 

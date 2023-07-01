# Learn DevOps

CI/CD with Jenkins using Pipelines and Docker

This is the course material for the Jenkins Course on Udemy ([Learn DevOps: CI/CD with Jenkins using Pipelines and Docker](https://www.udemy.com/learn-devops-ci-cd-with-jenkins-using-pipelines-and-docker/?couponCode=JENKINS_GIT))

# Bellow are detailed steps updated by Mason
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

**In Jenkins pipeline, use ubunbu docker server as docker runtime**
Get docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose/docker-compose-ubuntu-docker-in-host.yml

**In Jenkins pipeline, Use docker container runs in ubunbu docker as docker runtime**
Get docker compose file and rename it

    wget -O docker-compose.yml https://raw.githubusercontent.com/MasonSkill/devops-jenkins-course/mason/docker-compose-ubuntu-docker-in-host.yml

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
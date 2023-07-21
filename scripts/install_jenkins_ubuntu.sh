#!/bin/bash

# this script is only tested on Ubuntu Server 22.04 LTS
# The scripts are updated based on docker official document
# https://docs.docker.com/engine/install/ubuntu/#set-up-the-repository

# Install docker - Set up the repository
## Update the apt package index and install packages to allow apt to use a repository over HTTPS:
sudo apt-get update
sudo apt-get install ca-certificates curl gnupg

## Add Docker’s official GPG key:
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

## Use the following command to set up the repository:
echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install docker - Install Docker Engine
## Update the apt package index:
sudo apt-get update

## Install Docker Engine, containerd, and Docker Compose.
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

## This command enables the Docker service to start automatically on system boot.
sudo systemctl enable docker

## This command starts the Docker service immediately.
sudo systemctl start docker

## This command adds the user named "ubuntu" to the "docker" group.
sudo usermod -aG docker ubuntu

# Run jenkins
## This command creates a directory named "jenkins_home" and "jfrog_home" in the "/var" directory. 
mkdir -p /var/jenkins_home
mkdir -p /var/jfrog_home

## This command changes the ownership of the "/var/jenkins_home" and "/var/jfrog_home" directory and its contents recursively. 
chown -R 1000:1000 /var/jenkins_home/
chown -R 1030:1030 /var/jfrog_home/

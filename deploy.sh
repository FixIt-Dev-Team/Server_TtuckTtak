#!/usr/bin/env bash

cd /home/ubuntu/github_action/

db_image_name="ttuckttak_img"
db_container_name="ttuckttak"
version="0.0.1"
port=6543 # Default MySQL Port: 3306

echo "## Automation docker-database build and run ##"

# remove container
echo "=> Remove previous container..."
docker rm -f ${db_container_name}

# remove image
echo "=> Remove previous image..."
docker rmi -f ${db_image_name}:${version}

# new-build/re-build docker image
echo "=> Build new image..."
docker build --tag ${db_image_name}:${version}

# Run container
echo "=> Run container..."
docker run -d -p ${port}:${port} --name ${db_container_name} ${db_image_name}:${version}

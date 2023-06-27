#!/usr/bin/env bash

sudo screen -S Ttuckttak_dev -X quit
sudo screen -dms Ttuckttak_dev bash -c 'sudo docker build --tag ttuckttak:0.0.1 ./ && sudo docker run -p 6543:6543 ttuckttak:0.0.1'

#!/usr/bin/env bash

screen -S Ttuckttak_dev -X quit
screen -dms Ttuckttak_dev bash -c 'docker build --tag ttuckttak:0.0.1 ./ && docker run -p 18001:18001 ttuckttak:0.0.1'

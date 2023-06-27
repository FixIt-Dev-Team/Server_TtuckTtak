#!/usr/bin/env bash

screen -dms Ttuckttak_dev bash -c 'docker build --tag ttuckttak:0.0.1 ./ && docker run -p 6543:6543 ttuckttak:0.0.1'

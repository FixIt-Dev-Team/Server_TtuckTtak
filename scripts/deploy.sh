#!/usr/bin/env bash

cd ../ && docker build --tag ttuckttak:0.0.1 ./ && docker run -p 6543:6543 ttuckttak:0.0.1

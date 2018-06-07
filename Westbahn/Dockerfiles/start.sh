#!/bin/bash
docker stop mysql
docker rm mysql
docker build -t mysqlimg .
sleep 2
docker run -d --name mysql -v /home/mrousavy/Dockerfiles/data:/var/lib/mysql mysqlimg

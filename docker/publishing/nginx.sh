#! /bin/bash

docker run --name chicor-mobile -p 8011:80 -v /home/ec2-user/chicor/publishing/mf:/usr/share/nginx/html -d nginx
docker run --name chicor-web -p 8012:80 -v /home/ec2-user/chicor/publishing/df:/usr/share/nginx/html -d nginx
docker run --name chicor-partner -p 8014:80 -v /home/ec2-user/chicor/publishing/partner:/usr/share/nginx/html -d nginx
docker run --name chicor-back -p 8013:80 -v /home/ec2-user/chicor/publishing/back:/usr/share/nginx/html -d nginx
docker run --name chicor-mockup -p 8010:80 -v /home/ec2-user/chicor/publishing/mockup:/usr/share/nginx/html -d nginx

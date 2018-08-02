#!/usr/bin/env bash

curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
cd /vagrant/vagrant
/usr/local/bin/docker-compose up -d
/usr/local/bin/docker-compose exec -dT mongo mongo admin --eval "db.createUser({ user: 'admin', pwd: 'almerys0', roles: [ { role: 'userAdminAnyDatabase', db: 'admin' } ] })"
/usr/local/bin/docker-compose exec -dT mongo mongo admin -u admin -p almerys0  --eval "mydb = db.getSiblingDB('gravitee-local');mydb.createUser({ user: 'gexdb-local', pwd: 'almerys0', roles: [ { role: 'readWrite', db: 'gravitee-local' } ] })"

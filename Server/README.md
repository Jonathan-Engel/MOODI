# Server Information



# Server IP

34.217.90.146

## Dependencies
 - NGINX
 - Mongo
 - NodeJS
 - TypeScript
 - PM2
## Initial Setup
 - Create .env.example file
 - Run:
 
	    cd ~ubuntu
	    cd ~/MOODI/Server
	    npm install

## Starting server
 
    cd ~ubuntu
    cd ~/MOODI/Server
    pm2 start dist/server.js
    

## Stopping Server

    cd ~ubuntu
    cd ~/MOODI/Server
    pm2 stop dist/server.js

## Running Unit Tests

    cd ~ubuntu
    cd ~/MOODI/Server
    npm run test

## Running the build process (typescript, pug, etc)

    cd ~ubuntu
    cd ~/MOODI/Server
    npm run build

## Boilerplate Information
https://github.com/Microsoft/TypeScript-Node-Starter#typescript-node-starter

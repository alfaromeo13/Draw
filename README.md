# Distributed Java drawing game with RabbitMQ and ngrok 
read ppt file from above to understand the idea of a project.

install and configure RabbitMQ as a service
install ngrock

open ngrock and type the following: 
ngrok start --all --region eu   (this will use the following yml configuration file in the background)
```
version: "2"
authtoken: 1pkgmYykxTpPLnhZoS6ufSGFIwe_cNzNqjfSFLX5mYmZkT7A
tunnels:
  first:
    addr: localhost:15672
    proto: http   
  second:
    addr: localhost:5672
    proto: tcp
```
after this copy the output parameters inside CNF JoinRoom classes  

run the app from eclipse and enjoy :)


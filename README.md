# Requirements

    Capable WiFi card to work in monitor mode
    JDK 11 to compile the client
    Docker-compose to stand up the backend

# Dependencies:

    #!/bin/bash
    sudo apt-get install net-tools
    sudo apt-get install iw
    sudo apt-get install libpcap-dev
    sudo apt install horst
    sudo setcap cap_net_raw,cap_net_admin+eip /usr/sbin/horst


# Build:

Build the client with the command in the project root folder. Required at least JDK 8 installed.

    ./gradlew clean build

Generated artifact *hermes.jar* is in :

     client/build/libs

# Run:

Run the server with

    docker-compose -f ./server/docker-compose.yml up

Run the client with

    java -jar ./client/build/libs/hermes.jar myWlanCard

Open Grafana dashboard in

    http://localhost:3000/d/d0984132-8609-47c8-8692-9ed814139288





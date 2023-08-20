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

Run the client with

    java -jar hermes.jar myWlanCard



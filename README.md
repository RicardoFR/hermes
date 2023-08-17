# Dependencies:

    #!/bin/bash
    sudo apt-get install net-tools
    sudo apt-get install iw
    sudo apt-get install libpcap-dev
    sudo apt install horst
    sudo setcap cap_net_raw,cap_net_admin+eip /usr/sbin/horst

# Run:

Run the client with

    java -jar hermes.jar myWlanCard

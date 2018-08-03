#!/bin/bash

# Check if gedit is running
# -x flag only match processes whose name (or command line if -f is
# specified) exactly match the pattern.

if pgrep -x "mongod" > /dev/null
then
    echo "Running"
else
    echo "Starting mongo"
    screen -d -m mongod
fi

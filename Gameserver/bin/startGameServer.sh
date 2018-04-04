#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
	java -Xms512m -Xmx1024m -cp './lib/*' com.l2jbr.gameserver.GameServer
	[ $? -ne 2 ] && break
	sleep 10
done
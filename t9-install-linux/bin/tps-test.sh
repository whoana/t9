#!/bin/bash
if [ "$1" == "" ]; then
	echo "need first argument for the count of trying to check tps"
	exit	 
fi
tryCnt=$1
echo "the count of trying to check tps:"$tryCnt
for((i=0;i<$tryCnt;i++))
do
	echo "start: `date`"
	echo "try to check the tps:"$i
	./tps.sh
	echo "end: `date`"
        sleep 1;
done

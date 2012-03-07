#!/bin/sh

NUM_NODES="2"
QUEUE="banffq"
OUTPUT_DIR="experiment_results"
WAIT_INTERVAL=10

while getopts "n:q" OPTION; do
    case $OPTION in 
	n ) NUM_NODES=$OPTARG;;
	q ) QUEUE=$OPTARG;;
    esac
done

if [ ! -d $OUTPUT_DIR ]; then
	mkdir $OUTPUT_DIR
fi

if [ "$NUM_NODES" -gt 0 ]; then
	echo qsub -q $QUEUE "start_jppf_server.sh"
	qsub -q $QUEUE "start_jppf_server.sh"
	echo waiting $WAIT_INTERVAL seconds for server to initialize...
	sleep $WAIT_INTERVAL
	for n in `seq 1 $NUM_NODES`; do
		echo qsub -q $QUEUE "start_jppf_node.sh"
		qsub -q $QUEUE "start_jppf_node.sh"
	done
	echo waiting $WAIT_INTERVAL seconds for nodes to initialize...
	sleep $WAIT_INTERVAL
fi
echo qsub -q $QUEUE "start_jppf_client.sh"
qsub -q $QUEUE "start_jppf_client.sh"

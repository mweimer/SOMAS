#!/bin/sh 
#PBS -e experiment_results/
#PBS -o experiment_results/

HOME_DIR=`pwd`
CONFIG=$HOME_DIR"/somas/tools/jppf-client-config/jppf.properties"
LOG=$HOME_DIR"/somas/tools/jppf-client-config/logging.properties"
JAR=$HOME_DIR"/somas/jmetal-somas.jar"

alias java6=$HOME_DIR"/somas/tools/jdk1.6.0_07/bin/java"
java6 -Djppf.config=$CONFIG -Djava.util.logging.config.file=$LOG -Xmx1g -jar $JAR -sn DDoSScenario -op $HOME_DIR"/somas/experiment_results"

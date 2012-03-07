#!/bin/sh
#PBS -e experiment_results/
#PBS -o experiment_results/

HOME_DIR=`pwd`

export JAVA_HOME=$HOME_DIR"/somas/tools/jdk1.6.0_07"
$HOME_DIR"/somas/tools/apache-ant-1.8.0/bin/ant" -f $HOME_DIR"/somas/tools/JPPF-2.1-node/build.xml"

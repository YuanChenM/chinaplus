#!/bin/sh

EXEC_HOME=`dirname $0`
if [ $EXEC_HOME = "." ]; then
	$EXEC_HOME=`pwd`
fi

. /orion/data/chinaplus/auto/files/ENV/BASEENV

. $PARS_FILE_DIR/$3

LOG_DIR_BATCH=$1/$2
BATCHNAME=${BATCH_ID}


echo "*************************************************************************" 2>&1 >> $LOG_DIR_BATCH
echo "* [$BATCHNAME] START (`date '+%Y/%m/%d %H:%M:%S'`)" 2>&1 >> $LOG_DIR_BATCH
echo "*************************************************************************" 2>&1 >> $LOG_DIR_BATCH


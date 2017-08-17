#!/bin/sh

EXEC_HOME=`dirname $0`
if [ $EXEC_HOME = "." ]; then
	$EXEC_HOME=`pwd`
fi

. /orion/data/chinaplus/auto/files/ENV/BASEENV

######################## Read SSMS File and Insert the data into IF Table Start ########################
. $PARS_FILE_DIR/$1

LOG_DIR=${BATCH_LOG_DIR}

LOG_FILE=${BATCH_LOG_FILE}

sh ${EXEC_HOME}/before.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -eq ${RET_NG_AAUTO} ]; then
	exit ${RET_NG_AAUTO}
fi

date=`date '+%Y%m%d%H%M%S'`
ERROR_FILE=${BATCH_ID}_${date}.log

${JAVA_PATH}/java -Xms512M -Xmx1024M -jar ${JAVA_JAR_PATH}/${BATCH_JAR_FILE} ${BATCH_ID} ${WORKING_PATH} 2>&1 >> $BATCH_ERROR_LOG_DIR/$ERROR_FILE
ret=$?

if [ $ret -ne ${RET_OK_AAUTO} ]; then
	 echo "(Java Exec Error :$ret)" 2>&1 >> $LOG_DIR/$LOG_FILE
     echo " SCRIPT ABNORMAL END ([${BATCH_ID}] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	 exit $ret
fi

#if batch excute result are normal, then delete the error file.
rm -f $BATCH_ERROR_LOG_DIR/$ERROR_FILE
mv $BATCH_ERROR_LOG_DIR/${BATCH_ID}_*.log $BATCH_ERROR_LOG_BAK_DIR/ >/dev/null 2>&1

sh $EXEC_HOME/after.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -ne ${RET_OK_AAUTO} ]; then
	echo "(after.sh Error :$ret)"  2>&1 >> $LOG_DIR/$LOG_FILE
	echo " SCRIPT ABNORMAL END ([after.sh] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	exit $ret
fi
exit $ret

######################## Read SSMS File and Insert the data into IF Table End ########################

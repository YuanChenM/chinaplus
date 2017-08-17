#!/bin/sh

EXEC_HOME=`dirname $0`
if [ $EXEC_HOME = "." ]; then
	$EXEC_HOME=`pwd`
fi

. /orion/data/chinaplus/auto/files/ENV/BASEENV

######################## Extract data from interface table, and insert the data into business table Start ########################
. $PARS_FILE_DIR/$1

LOG_DIR=${BATCH_LOG_DIR}

LOG_FILE=${BATCH_LOG_FILE}

sh ${EXEC_HOME}/before.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -eq ${RET_NG_AAUTO} ]; then
	exit ${RET_NG_AAUTO}
fi

date=`date '+%Y%m%d%H%M%S'`

BUSINESS_PATTERN=$2

OFFICE_CODE=$3

BATCH_DATE=$4

if [ -z "$4" ]; then
	BATCH_DATE=`date -d yesterday '+%Y%m%d'`
fi
ERROR_FILE=${BATCH_ID}_${OFFICE_CODE/:/}_${date}.log

${JAVA_PATH}/java -Xms512M -Xmx1024M -Dpoi.keep.tmp.files=false -jar ${JAVA_JAR_PATH}/${BATCH_JAR_FILE} ${BATCH_ID} ${BUSINESS_PATTERN} ${OFFICE_CODE} ${BATCH_DATE} 2>&1 >> $BATCH_ERROR_LOG_DIR/$ERROR_FILE
ret=$?

if [ $ret -ne ${RET_OK_AAUTO} ]; then
	 echo "(Java Exec Error :$ret)" 2>&1 >> $LOG_DIR/$LOG_FILE
     echo " SCRIPT ABNORMAL END ([${BATCH_ID}] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	 exit $ret
fi

#if batch excute result are normal, then delete the error file.
rm -f $BATCH_ERROR_LOG_DIR/$ERROR_FILE
mv $BATCH_ERROR_LOG_DIR/${BATCH_ID}_${OFFICE_CODE/:/}_*.log $BATCH_ERROR_LOG_BAK_DIR/ >/dev/null 2>&1

sh $EXEC_HOME/after.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -ne ${RET_OK_AAUTO} ]; then
	echo "(after.sh Error :$ret)"  2>&1 >> $LOG_DIR/$LOG_FILE
	echo " SCRIPT ABNORMAL END ([after.sh] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	exit $ret
fi
exit $ret

######################## Extract data from interface table, and insert the data into business table End ########################

#!/bin/sh

EXEC_HOME=`dirname $0`
if [ $EXEC_HOME = "." ]; then
	$EXEC_HOME=`pwd`
fi

. /orion/data/chinaplus/auto/files/ENV/BASEENV

######################## RunDown Batch Start ########################
. $PARS_FILE_DIR/$1

LOG_DIR=${BATCH_LOG_DIR}

LOG_FILE=${BATCH_LOG_FILE}

sh ${EXEC_HOME}/before.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -eq ${RET_NG_AAUTO} ]; then
	exit ${RET_NG_AAUTO}
fi

date=`date '+%Y%m%d%H%M%S'`

#IS_ONLINE=$4
IS_ONLINE=2

OFFICE_CODE=$4

STOCK_DATE=$5
ERROR_FILE1=${BATCH_ID}_${OFFICE_CODE/:/}_${date}.log

if [ -z "$5" ]; then
	STOCK_DATE=`date -d yesterday '+%Y%m%d'`
fi

${JAVA_PATH}/java -Xms512M -Xmx1024M -Dpoi.keep.tmp.files=false -jar ${JAVA_JAR_PATH}/${BATCH_JAR_FILE} ${BATCH_ID} ${IS_ONLINE} ${STOCK_DATE} ${OFFICE_CODE} 2>&1 >> $BATCH_ERROR_LOG_DIR/$ERROR_FILE1
ret=$?

if [ $ret -ne ${RET_OK_AAUTO} ]; then
	 echo "(Java Exec Error :$ret)" 2>&1 >> $LOG_DIR/$LOG_FILE
     echo " SCRIPT ABNORMAL END ([${BATCH_ID}] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	 exit $ret
fi

#if batch excute result are normal, then delete the error file.
rm -f $BATCH_ERROR_LOG_DIR/$ERROR_FILE1
mv $BATCH_ERROR_LOG_DIR/${BATCH_ID}_${OFFICE_CODE/:/}_*.log $BATCH_ERROR_LOG_BAK_DIR/ >/dev/null 2>&1


sh $EXEC_HOME/after.sh $LOG_DIR $LOG_FILE $1
ret=$?
if [ $ret -ne ${RET_OK_AAUTO} ]; then
	echo "(after.sh Error :$ret)"  2>&1 >> $LOG_DIR/$LOG_FILE
	echo " SCRIPT ABNORMAL END ([after.sh] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	exit $ret
fi

######################## RunDown Batch End ########################


######################## StockStatus Batch Start ########################
. $PARS_FILE_DIR/$2

LOG_DIR=${BATCH_LOG_DIR}

LOG_FILE=${BATCH_LOG_FILE}

sh ${EXEC_HOME}/before.sh $LOG_DIR $LOG_FILE $2
ret=$?
if [ $ret -eq ${RET_NG_AAUTO} ]; then
	exit ${RET_NG_AAUTO}
fi

ERROR_FILE2=${BATCH_ID}_${OFFICE_CODE/:/}_${date}.log

${JAVA_PATH}/java -Xms512M -Xmx1024M -Dpoi.keep.tmp.files=false -jar ${JAVA_JAR_PATH}/${BATCH_JAR_FILE} ${BATCH_ID} ${IS_ONLINE} ${STOCK_DATE} ${OFFICE_CODE} 2>&1 >> $BATCH_ERROR_LOG_DIR/$ERROR_FILE2
ret=$?

if [ $ret -ne ${RET_OK_AAUTO} ]; then
	 echo "(Java Exec Error :$ret)" 2>&1 >> $LOG_DIR/$LOG_FILE
     echo " SCRIPT ABNORMAL END ([${BATCH_ID}] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	 exit $ret
fi

#if batch excute result are normal, then delete the error file.
rm -f $BATCH_ERROR_LOG_DIR/$ERROR_FILE2
mv $BATCH_ERROR_LOG_DIR/${BATCH_ID}_${OFFICE_CODE/:/}_*.log $BATCH_ERROR_LOG_BAK_DIR/ >/dev/null 2>&1

sh $EXEC_HOME/after.sh $LOG_DIR $LOG_FILE $2
ret=$?
if [ $ret -ne ${RET_OK_AAUTO} ]; then
	echo "(after.sh Error :$ret)"  2>&1 >> $LOG_DIR/$LOG_FILE
	echo " SCRIPT ABNORMAL END ([after.sh] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	exit $ret
fi

######################## StockStatus Batch End ########################


######################## Rundown History Batch Start ########################
. $PARS_FILE_DIR/$3

LOG_DIR=${BATCH_LOG_DIR}

LOG_FILE=${BATCH_LOG_FILE}

sh ${EXEC_HOME}/before.sh $LOG_DIR $LOG_FILE $3
ret=$?
if [ $ret -eq ${RET_NG_AAUTO} ]; then
	exit ${RET_NG_AAUTO}
fi

ERROR_FILE2=${BATCH_ID}_${OFFICE_CODE/:/}_${date}.log

${JAVA_PATH}/java -Xms512M -Xmx1024M -Dpoi.keep.tmp.files=false -jar ${JAVA_JAR_PATH}/${BATCH_JAR_FILE} ${BATCH_ID} ${IS_ONLINE} ${STOCK_DATE} ${OFFICE_CODE} 2>&1 >> $BATCH_ERROR_LOG_DIR/$ERROR_FILE2
ret=$?

if [ $ret -ne ${RET_OK_AAUTO} ]; then
	 echo "(Java Exec Error :$ret)" 2>&1 >> $LOG_DIR/$LOG_FILE
     echo " SCRIPT ABNORMAL END ([${BATCH_ID}] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	 exit $ret
fi

#if batch excute result are normal, then delete the error file.
rm -f $BATCH_ERROR_LOG_DIR/$ERROR_FILE2
mv $BATCH_ERROR_LOG_DIR/${BATCH_ID}_${OFFICE_CODE/:/}_*.log $BATCH_ERROR_LOG_BAK_DIR/ >/dev/null 2>&1

sh $EXEC_HOME/after.sh $LOG_DIR $LOG_FILE $3
ret=$?
if [ $ret -ne ${RET_OK_AAUTO} ]; then
	echo "(after.sh Error :$ret)"  2>&1 >> $LOG_DIR/$LOG_FILE
	echo " SCRIPT ABNORMAL END ([after.sh] )" 2>&1 >> $LOG_DIR/$LOG_FILE
	exit $ret
fi

######################## Rundown History Batch End ########################


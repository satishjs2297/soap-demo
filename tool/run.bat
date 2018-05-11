@ECHO OFF
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;./lib/soap-demo-1.0.jar

java -Xms128m -Xmx384m -Xnoclassgc com.soap.util.SoapRequest "./conf/mysample_casecreation_39060180.msg" "" "http://www.thomas-bayer.com/axis2/services/BLZService" "./logs/log.txt"
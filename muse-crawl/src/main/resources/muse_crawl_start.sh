# !/bin/bash
# program
# test java open

export JAVA_HOME=/usr/local/jdk1.8.0_181
export JRE=/usr/local/jdk1.8.0_181/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE/lib:.
export PATH=$PATH:$JAVA_HOME/bin/:$JRE/bin


nohup java -ms512m -mx512m -Xmn128m -jar muse-crawl-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &


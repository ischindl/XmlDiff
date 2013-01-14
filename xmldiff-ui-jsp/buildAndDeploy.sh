#! /bin/bash
# chmod u+x

# Change directory to the folder containing this script
cd "`dirname "$0"`"
clear

# Build the app (pom.xml must be in the current folder)
/opt/apache-maven-2.2.1/bin/mvn clean install

# Make sure your $CATALINA_HOME is pointing to the web server's root folder
echo $CATALINA_HOME

rm $CATALINA_HOME/webapps/xmldiff-ui-jsp*.war
cp ./target/xmldiff-ui-jsp*.war $CATALINA_HOME/webapps

cd $CATALINA_HOME/webapps 
pwd
rm -rf xmldiff-ui-jsp
mkdir xmldiff-ui-jsp

cd xmldiff-ui-jsp
# If Tomcat is started it will automatically search every few seconds for .war files to unpack
jar xf ./../xmldiff-ui-jsp*.war
# x = extract
# t = list table of content of the jar file

# Check the timestamp to make sure the content was indeed created/replaced
ls -al ./..

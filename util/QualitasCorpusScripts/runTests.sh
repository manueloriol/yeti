#!/bin/bash

DIRS=( ant antlr aoi argouml aspectj axion azureus c_jdbc castor cayenne checkstyle cobertura colt columba compiere derby displaytag drawswf drjava eclipse_SDK emma exoportal findbugs fitjava fitlibraryforfitnesse freecol freecs galleon ganttproject gt2 heritrix hibernate hsqldb htmlunit informa ireport itext ivatagroupware jFin_DateMath jag james jasml jasperreports javacc jboss jchempaint jedit jena jext jfreechart jgraph jgraphpad jgrapht jgroups jhotdraw jmeter jmoney joggplayer jparse jpf jrat jre jrefactory jruby jsXe jspwiki jtopen jung junit log4j lucene marauroa maven megamek mvnforum myfaces_core nakedobjects nekohtml netbeans openjms oscache picocontainer pmd poi pooka proguard quartz quickserver quilt roller rssowl sablecc sandmark springframework squirrel_sql struts sunflow tapestry tomcat trove velocity webmail weka xalan xerces xmojo )

for (( i = 0 ; i < 106 ; i++ )) do
   echo "-----------------------------------"
   echo "${DIRS[$i]}: runnning test script"
   cd ${DIRS[$i]}
   ./tests.sh
   cd ..
   echo " "
done	

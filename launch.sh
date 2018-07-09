#!/bin/sh
export CLASSPATH=".:dist/*" 
java -Dwzpath=wz/ -Xmx2000m net.server.Server
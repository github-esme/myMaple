@echo off
@title myMaple
set CLASSPATH=.;dist\*
java -Xmx10000m -Dwzpath=wz\ net.server.Server
pause
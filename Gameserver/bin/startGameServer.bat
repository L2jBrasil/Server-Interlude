@echo off
title Game Server Console
:start
echo Starting L2J Game Server.
echo.
REM -------------------------------------
REM Default parameters for a basic server.
java -Xmx1024m -cp ./lib/* com.l2jbr.gameserver.GameServer
REM
REM If you have a big server and lots of memory, you could experiment for example with
REM java -server -Xmx2048m -Xms1024m -Xmn512m  -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts
REM -------------------------------------
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restart ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly
echo.
:end
echo.
echo server terminated
echo.
pause

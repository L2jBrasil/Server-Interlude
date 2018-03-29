@echo off
REM ##############################################
REM ## L2JDP Database Installer - (by DrLecter) ##
REM ##############################################
REM ## Interactive script setup -  (by TanelTM) ##
REM ##############################################
REM Copyright (C) 2007 L2J DataPack
REM This program is free software; you can redistribute it and/or modify 
REM it under the terms of the GNU General Public License as published by 
REM the Free Software Foundation; either version 2 of the License, or (at
REM your option) any later version.
REM
REM This program is distributed in the hope that it will be useful, but 
REM WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
REM or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
REM for more details.
REM
REM You should have received a copy of the GNU General Public License along 
REM with this program; if not, write to the Free Software Foundation, Inc., 
REM 675 Mass Ave, Cambridge, MA 02139, USA. Or contact the Official L2J
REM DataPack Project at http://www.l2jdp.com, http://forum.l2jdp.com or
REM #l2j-datapack @ irc://irc.freenode.net

set config_file=vars.txt
set config_version=0

set workdir="%cd%"
set full=0
set stage=0
set logging=0

set upgrade_mode=0
set backup=.
set logdir=.
set safe_mode=1
set cmode=c
set fresh_setup=0

:loadconfig
title L2JDP installer - Reading configuration from file...
cls
if not exist %config_file% goto configure
ren %config_file% vars.bat
call vars.bat
ren vars.bat %config_file%
call :colors 17
if /i %config_version% == 1 goto ls_section
set upgrade_mode=2
echo It seems to be the first time you run this version of
echo database_installer but I found a settings file already.
echo I'll hopefully ask this questions just once.
echo.
echo Configuration upgrade options:
echo.
echo (1) Import and continue: I'll read your old settings and
echo    continue execution, but since no new settings will be
echo    saved, you'll see this menu again next time.
echo.
echo (2) Import and configure: This tool has some new options
echo    available, you choose the values that fit your needs
echo    using former settings as a base.
echo.
echo (3) Ignose stored settings: I'll let you configure me 
echo    with a fresh set of default values as a base.
echo.
echo (4) View saved settings: See the contents of the config 
echo    file.
echo.
echo (5) Quit: Did you came here by mistake?
echo.
set /P upgrade_mode="Type a number, press Enter (default is '%upgrade_mode%'): "
if %upgrade_mode%==1 goto ls_section
if %upgrade_mode%==2 goto configure
if %upgrade_mode%==3 goto configure
if %upgrade_mode%==4 (cls&type %config_file%&pause&goto loadconfig)
if %upgrade_mode%==5 goto :eof
goto loadconfig

:colors
if /i "%cmode%"=="n" (
if not "%1"=="17" (	color F	) else ( color )
) else ( color %1 )
goto :eof

:configure
call :colors 17
title L2JDP installer - Setup
cls
set config_version=1
if NOT %upgrade_mode% == 2 (
set fresh_setup=1
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.0\bin
set lsuser=root
set lspass=
set lsdb=l2jdb
set lshost=localhost
set gsuser=root
set gspass=
set gsdb=l2jdb
set gshost=localhost
set cmode=c
set backup=.
set logdir=.
)
set mysqlPath=%mysqlBinPath%\mysql.exe
echo New settings will be created for this tool to run in
echo your computer, so I need to ask you some questions.
echo.
echo 1-MySql Binaries
echo --------------------
echo In order to perform my tasks, I need the path for commands
echo such as 'mysql' and 'mysqldump'. Both executables are
echo usually stored in the same place.
echo.
if "%mysqlBinPath%" == "" (
set mysqlBinPath=use path
echo I can't determine if the binaries are available with your
echo default settings.
) else (
echo I can try to find out if the current setting actually works...
echo.
echo %mysqlPath%
)
if not "%mysqlBinPath%" == "use path" call :binaryfind
echo.
path|find "MySQL">NUL
if %errorlevel% == 0 (
echo I found MySQL is in your PATH, this will be used by default.
echo If you want to use something different, change 'use path' for 
echo something else.
set mysqlBinPath=use path
) else (
echo Look, I can't find "MYSQL" in your PATH environment variable.
echo It would be good if you go and find out where "mysql.exe" and 
echo "mysqldump.exe" are.
echo.
echo If you have no idea about the meaning of words such as MYSQL
echo or PATH, you'd better close this window, and consider googling
echo and reading about it. Setup and host an L2J server requires a
echo minimum of technical skills.
)
echo.
echo Write the path to your MySQL binaries (no trailing slash needed):
set /P mysqlBinPath="(default %mysqlBinPath%): "
cls
echo.
echo 2-LoginServer settings
echo --------------------
echo I will connect to the MySQL server you specify, and setup a
echo Loginserver database there, most people use a single MySQL
echo server and database for both Login and Gameserver tables.
echo.
set /P lsuser="MySQL Username (default is '%lsuser%'): "
set /P lspass="Password (will be shown as you type, default '%lspass%'): "
set /P lsdb="Database (default is '%lsdb%'): "
set /P lshost="Host (default is '%lshost%'): "
if NOT "%lsuser%"=="%gsuser%" set gsuser=%lsuser%
if NOT "%lspass%"=="%gspass%" set gspass=%lspass%
if NOT "%lsdb%"=="%gsdb%" set gsdb=%lsdb%
if NOT "%lshost%"=="%gshost%" set gshost=%lshost%
echo.
echo 3-GameServer settings
echo --------------------
set /P gsuser="User (default is '%gsuser%'): "
set /P gspass="Pass (default is '%gspass%'): "
set /P gsdb="Database (default is '%gsdb%'): "
set /P gshost="Host (default is '%gshost%'): "
echo.
echo 4-Misc. settings
echo --------------------
set /P cmode="Color mode (c)olor or (n)on-color, default %cmode% : "
set /P backup="Path for your backups (default '%backup%'): "
set /P logdir="Path for your logs (default '%logdir%'): "
:safe1
set safemode=y
set /P safemode="Debugging messages and increase verbosity a lil bit (y/n, default '%safemode%'): "
if /i %safemode%==y (set safe_mode=1&goto safe2)
if /i %safemode%==n (set safe_mode=0&goto safe2)
goto safe1
:safe2
echo.
if "%mysqlBinPath%" == "use path" (
set mysqlBinPath=
set mysqldumpPath=mysqldump
set mysqlPath=mysql
) else (
set mysqldumpPath=%mysqlBinPath%\mysqldump.exe
set mysqlPath=%mysqlBinPath%\mysql.exe
)
echo @echo off > %config_file%
echo set config_version=%config_version% >> %config_file%
echo set cmode=%cmode%>> %config_file%
echo set safe_mode=%safe_mode% >> %config_file%
echo set mysqlPath=%mysqlPath%>> %config_file%
echo set mysqlBinPath=%mysqlBinPath%>> %config_file%
echo set mysqldumpPath=%mysqldumpPath%>> %config_file%
echo set lsuser=%lsuser%>> %config_file%
echo set lspass=%lspass%>> %config_file%
echo set lsdb=%lsdb%>> %config_file%
echo set lshost=%lshost% >> %config_file%
echo set gsuser=%gsuser%>> %config_file%
echo set gspass=%gspass%>> %config_file%
echo set gsdb=%gsdb%>> %config_file%
echo set gshost=%gshost%>> %config_file%
echo set logdir=%logdir%>> %config_file%
echo set backup=%backup%>> %config_file%
echo.
echo Script setup complete, your settings were saved in the
echo '%config_file%' file. Remember: your passwords are stored
echo as clear text.
echo.
echo press any key to continue...
pause> nul
goto loadconfig

:ls_section
cls
call :colors 17
set cmdline=
set stage=1
title L2JDP installer - Login Server database setup
echo.
echo Trying to make a backup of your loginserver database.
set cmdline="%mysqldumpPath%" --add-drop-table -h %lshost% -u %lsuser% --password=%lspass% %lsdb% ^> "%backup%\loginserver_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto lsdbok
REM if %safe_mode% == 1 goto omfg
:ls_err1
call :colors 47
title L2JDP installer - Login Server database setup ERROR!!!
cls
echo.
echo Backup attempt failed! A possible reason for this to 
echo happen, is that your DB doesn't exist yet. I could 
echo try to create %lsdb% for you, or maybe you prefer to
echo proceed with the GameServer part of this tool.
echo.
:ls_ask1
set lsdbprompt=y
echo ATTEMPT TO CREATE LOGINSERVER DATABASE:
echo.
echo (y)es
echo.
echo (n)o
echo.
echo (r)econfigure
echo.
echo (q)uit
echo.
set /p lsdbprompt= Choose (default yes):
if /i %lsdbprompt%==y goto lsdbcreate
if /i %lsdbprompt%==n goto gs_backup
if /i %lsdbprompt%==r goto configure
if /i %lsdbprompt%==q goto end
goto ls_ask1

:omfg
cls
call :colors 57
title L2JDP installer - potential PICNIC detected at stage %stage%
echo.
echo There was some problem while executing:
echo.
echo "%cmdline%"
echo.
echo I'd suggest you to look for correct values and try this
echo script again later. But maybe you'd prefer to go on now.
echo.
if %stage% == 1 set label=ls_err1
if %stage% == 2 set label=ls_err2
if %stage% == 3 set label=gs_backup
if %stage% == 4 set label=gs_err1
if %stage% == 5 set label=gs_err2
if %stage% == 6 set label=horrible_end
if %stage% == 7 set label=horrible_end
:omfgask1
set omfgprompt=q
echo (c)ontinue running the script
echo.
echo (r)econfigure
echo.
echo (q)uit now
echo.
set /p omfgprompt= Choose (default quit):
if  /i %omfgprompt%==c goto %label%
if  /i %omfgprompt%==r goto configure
if  /i %omfgprompt%==q goto horrible_end
goto omfgask1

:lsdbcreate
call :colors 17
set cmdline=
set stage=2
title L2JDP installer - Login Server database setup - DB Creation
echo.
echo Trying to create a Login Server database...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -e "CREATE DATABASE %lsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto logininstall
if %safe_mode% == 1 goto omfg
:ls_err2
call :colors 47
title L2JDP installer - Login Server database setup - DB Creation error
cls
echo An error occured while trying to create a database for 
echo your login server.
echo.
echo Possible reasons:
echo 1-You provided innacurate info , check user, password, etc.
echo 2-User %lsuser% don't have enough privileges for 
echo database creation. Check your MySQL privileges.
echo 3-Database exists already...?
echo.
echo Unless you're sure that the pending actions of this tool 
echo could work, i'd suggest you to look for correct values
echo and try this script again later.
echo.
:ls_ask2
set omfgprompt=q
echo (c)ontinue running
echo.
echo (r)econfigure
echo.
echo (q)uit now
echo.
set /p omfgprompt= Choose (default quit):
if /i %omfgprompt%==c goto gs_backup
if /i %omfgprompt%==q goto horrible_end
if /i %omfgprompt%==r goto configure
goto ls_ask2

:lsdbok
call :colors 17
title L2JDP installer - Login Server database setup - WARNING!!!
echo.
:asklogin
if %fresh_setup%==0 (
set loginprompt=s
set msg=default skip
) else (
set loginprompt=x
set msg=no default for fresh install
)
echo LOGINSERVER DATABASE install type:
echo.
echo (f)ull: I will destroy data in your `accounts` and 
echo    and `gameserver` tables.
echo.
echo (s)kip: I'll take you to the gameserver database
echo    installation and upgrade options.
echo.
echo (r)econfigure: You'll be able to redefine MySQL path,
echo    user and database information and start over with
echo    those fresh values.
echo.
echo (q)uit
echo.
set /p loginprompt= Choose (%msg%) : 
if /i %loginprompt%==f goto logininstall
if /i %loginprompt%==s goto gs_backup
if /i %loginprompt%==r goto configure
if /i %loginprompt%==q goto end
goto asklogin

:logininstall
set stage=3
call :colors 17
set cmdline=
title L2JDP installer - Login Server database setup - Full install
echo Deleting loginserver tables for new content.
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< login_install.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
goto gs_backup

:gs_backup
call :colors 17
set cmdline=
if %full% == 1 goto fullinstall
set stage=4
title L2JDP installer - Game server database setup
cls
echo.
echo Making a backup of the original gameserver database.
set cmdline="%mysqldumpPath%" --add-drop-table -h %gshost% -u %gsuser% --password=%gspass% %gsdb% ^> "%backup%\gameserver_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gsdbok
if %safe_mode% == 1 goto omfg
:gs_err1
call :colors 47
title L2JDP installer - Game Server database setup - Backup failed!
cls
echo.
echo Backup attempt failed! A possible reason for this to happen,
echo is that your DB doesn't exist yet. I could try to create 
echo %gsdb% for you, but maybe you prefer me to continue with 
echo last part of the script.
echo.
:askgsdb
set gsdbprompt=y
echo ATTEMPT TO CREATE GAMESERVER DATABASE?
echo.
echo (y)es
echo.
echo (n)o
echo.
echo (r)econfigure
echo.
echo (q)uit
echo.
set /p gsdbprompt= Choose (default yes):
if /i %gsdbprompt%==y goto gsdbcreate
if /i %gsdbprompt%==n goto horrible_end
if /i %gsdbprompt%==r goto configure
if /i %gsdbprompt%==q goto end
goto askgsdb

:gsdbcreate
call :colors 17
set stage=5
set cmdline=
title L2JDP installer - Game Server database setup - DB Creation
cls
echo Trying to create Game Server database...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -e "CREATE DATABASE %gsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto fullinstall
if %safe_mode% == 1 goto omfg
:gs_err2
call :colors 47
title L2JDP installer - Game Server database setup - DB Creation failed!
cls
echo.
echo An error occured while trying to create a database for 
echo your game server.
echo.
echo Possible reasons:
echo 1-You provided innacurate info, check username, pass, etc.
echo 2-User %gsuser% don't have enough privileges for 
echo database creation.
echo 3-Database exists already...?
echo.
echo I'd suggest you to look for correct values and try this
echo script again later. But you can try to reconfigure it now.
echo.
:askgsdbcreate
set omfgprompt=q
echo (r)estart script with fresh configuration values
echo.
echo (q)uit now
echo.
set /p omfgprompt=  Choose (default quit):
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto horrible_end
goto askgsdbcreate

:gsdbok
call :colors 17
title L2JDP installer - Game Server database setup - WARNING!!!
cls
echo.
:asktype
set installtype=u
echo GAMESERVER DATABASE install:
echo.
echo (f)ull: WARNING! I'll destroy ALL of your existing character
echo    data (i really mean it: items, pets.. ALL)
echo.
echo (u)pgrade: I'll do my best to preserve all of your character
echo    data.
echo.
echo (s)kip: We'll get into the last set of questions (cummulative
echo    updates, experimental stuff...)
echo.
echo (q)uit
echo.
set /p installtype= Choose (default upgrade):
if /i %installtype%==f goto fullinstall
if /i %installtype%==u goto upgradeinstall
if /i %installtype%==s goto experimental
if /i %installtype%==q goto end
goto asktype

:fullinstall
call :colors 17
set stage=6
set cmdline=
title L2JDP installer - Game Server database setup - Full install
echo Deleting all gameserver tables for new content...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< full_install.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo Game Server tables were deleted.
goto upgradeinstall

:upgradeinstall
set stage=6
set cmdline=
if %full% == 1 (
title L2JDP installer - Game Server database setup - Installing...
echo Installing new gameserver content.
) else (
title L2JDP installer - Game Server database setup - Upgrading...
echo Upgrading gameserver content.
)
if %logging% == 0 set output=NUL
set dest=ls
for %%i in (
accounts.sql
gameservers.sql
) do call :dump %%i
set dest=gs
for %%i in (
account_data.sql
armor.sql
armorsets.sql
auction.sql
auction_bid.sql
auction_watch.sql
augmentations.sql
auto_chat.sql
auto_chat_text.sql
boxaccess.sql
boxes.sql
castle.sql
castle_door.sql
castle_doorupgrade.sql
castle_siege_guards.sql
char_templates.sql
character_friends.sql
character_hennas.sql
character_macroses.sql
character_quests.sql
character_recipebook.sql
character_recommends.sql
character_shortcuts.sql
character_skills.sql
character_skills_save.sql
character_subclasses.sql
characters.sql
clan_data.sql
clan_privs.sql
clan_skills.sql
clan_subpledges.sql
clan_wars.sql
clanhall.sql
clanhall_functions.sql
class_list.sql
cursed_weapons.sql
dimensional_rift.sql
droplist.sql
enchant_skill_trees.sql
etcitem.sql
fish.sql
fishing_skill_trees.sql
forums.sql
games.sql
global_tasks.sql
helper_buff_list.sql
henna.sql
henna_trees.sql
heroes.sql
items.sql
itemsonground.sql
locations.sql
lvlupgain.sql
mapregion.sql
merchant_areas_list.sql
merchant_buylists.sql
merchant_lease.sql
merchant_shopids.sql
merchants.sql
minions.sql
mods_wedding.sql
npc.sql
npcskills.sql
olympiad_nobles.sql
pets.sql
pets_stats.sql
pledge_skill_trees.sql
posts.sql
quest_global_data.sql
raidboss_spawnlist.sql
random_spawn.sql
random_spawn_loc.sql
seven_signs.sql
seven_signs_festival.sql
seven_signs_status.sql
siege_clans.sql
skill_learn.sql
skill_spellbooks.sql
skill_trees.sql
spawnlist.sql
teleport.sql
topic.sql
walker_routes.sql
weapon.sql
zone.sql
zone_vertices.sql
castle_manor_procure.sql
castle_manor_production.sql
) do call :dump %%i
echo done...
echo.
goto experimental

:dump
set cmdline=
if /i %full% == 1 (set action=Installing) else (set action=Upgrading)
echo %action% %1>>"%output%"
echo %action% %~nx1
if "%dest%"=="ls" set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< ..\sql\%1 2^>^>"%output%"
if "%dest%"=="gs" set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< ..\sql\%1 2^>^>"%output%"
%cmdline%
if %logging%==0 if NOT %ERRORLEVEL%==0 call :omfg2 %1
goto :eof

:omfg2
cls
call :colors 47
title L2JDP installer - potential database issue at stage %stage%
echo.
echo Something caused an error while executing instruction :
echo %mysqlPath% -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb%
echo.
echo with file %~nx1
echo.
echo What we should do now?
echo.
:askomfg2
set ntpebcak=c
echo (l)og it: I will create a log for this file, then continue
echo    with the rest of the list in non-logging mode.
echo.
echo (c)ontinue: Let's pretend that nothing happened and continue with
echo    the rest of the list.
echo.
echo (r)econfigure: Perhaps these errors were caused by a typo.
echo    you can restart from scratch and redefine paths, databases
echo    and user info again.
echo.
echo (q)uit now
echo.
set /p ntpebcak= Choose (default continue):
if  /i %ntpebcak%==c (call :colors 17 & goto :eof)
if  /i %ntpebcak%==l (call :logginon %1 & goto :eof)
if  /i %ntpebcak%==r (call :configure & exit)
if  /i %ntpebcak%==q (call :horrible_end & exit)
goto askomfg2

:logginon
cls
call :colors 17
title L2JDP installer - Game Server database setup - Logging options turned on
set logging=1
if %full% == 1 (
  set output=%logdir%\install-%~nx1.log
) else (
  set output=%logdir%\upgrade-%~nx1.log
)
echo.
echo Per your request, i'll create a log file for your reading pleasure.
echo.
echo I'll call it %output%
echo.
echo If you already have such a file and would like to keep a copy.
echo go now and read it or back it up, because it's not going to be rotated
echo or anything, instead i'll just overwrite it.
echo.
echo When you're done or if you don't mind, press any key to start.
pause>NUL
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^<..\sql\%1 2^>^>"%output%"
date /t >"%output%"
time /t >>"%output%"
%cmdline%
echo Log file created, resuming normal operations...
call :colors 17
set logging=0
set output=NUL
goto :eof

:experimental
REM echo.
:askexp
REM set expprompt=x
REM set /p expprompt=Install experimental gameserver DB tables: (y) yes or (n) no or (q) quit? 
REM if /i %expprompt%==y goto expinstall
REM if /i %expprompt%==n goto newbie_helper
REM if /i %expprompt%==q goto end
REM goto end
:expinstall
REM echo Installing new content.
REM %mysqlPath% -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/experimental/npc.sql
goto newbie_helper

:newbie_helper
call :colors 17
set stage=7
title L2JDP installer - Game Server database setup - update SQL files
cls
if %full% == 1 goto end
echo.
echo In the sql/updates folder, we use to store cummulative
echo changes needed by the database structures.
echo.
echo Usually these SQL files are created whenever some new
echo feature implementation requires it.
echo.
echo If you're too lame to know what these changes are about,
echo i can try to apply these patches for you.
:asknb
set nbprompt=a
echo.
echo What we do with the files in sql/updates folder?
echo.
echo (a)utomagic processing: I'll get into the folder and 
echo    try to dump _every_ '.sql' file i find there, into 
echo    your database. A fresh setup wouldn't usually need 
echo    such a thing. And, as any automagic task, this may
echo    pose a risk on your data.
echo.
echo (s)kip: I'll do nothing, it's up to you to find out
echo    which file does what, which one could be of use for
echo    you, etc.
echo.
set /p nbprompt= Choose (default auto):
if /i %nbprompt%==a goto nbinstall
if /i %nbprompt%==s goto end
goto asknb
:nbinstall
cd ..\sql\updates\
echo @echo off> temp.bat
if exist errors.txt del errors.txt
for %%i in (*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> errors.txt >> temp.bat
call temp.bat> nul
del temp.bat
move errors.txt %workdir%
cd %workdir%
title L2JDP installer - Game Server database setup - updates processing complete
cls
echo Automagic processing finished, i'm leaving an 'errors.txt'
echo file for your consideration.
echo.
echo Remember that some of these files could have tried to add stuff that were 
echo in the database already, so don't go out yelling about 
echo.
echo 'Duplicate column name'
echo.
echo messages you may find there.
echo.
echo Rather you should focus in those that say 
echo.
echo 'Table doesn't exist'
echo.
echo for example.
echo.
pause
goto end

:binaryfind
if EXIST "%mysqlBinPath%" (echo Found) else (echo Not Found)
goto :eof

:horrible_end
call :colors 47
title L2JDP installer - Oops!
cls
echo This wasn't a clean run, but don't worry.
echo You can get help and support:
echo.
echo 1-Read the L2J Datapack project wiki :
echo       (http://l2jdp.com/trac/wiki)
echo 2-Search for a similar problem in our forums
echo       (http://forum.l2jdp.com/)
echo.
echo You can ask for support in our forums or irc channel:
echo irc://irc.freenode.net channel: #l2j-datapack
echo.
echo I'll try to gather some versioning information that you
echo may find useful when asking for support :
echo.
echo Datapack revision reported by 'SVN version':
svnversion -n 2>NUL
echo.
if %ERRORLEVEL% == 9009 (
echo   SVN commandline tools not found!
echo   Please download and install a copy from :
echo   http://subversion.tigris.org/servlets/ProjectDocumentList?folderID=91
echo.
)
set dpvf="..\config\l2jdp-version.properties" 
echo Datapack revision reported by properties file :
if NOT EXIST %dpvf% (
echo   Your %dpvf% file is missing!
echo   Use eclipse/ant to build one from your DP SVN copy.
echo   With it we'll be able to help you better.
) else (
type %dpvf% | find "version" 2> NUL
if not %ERRORLEVEL% == 0 (
echo   An error occured while trying to read
echo   your %dpvf% file!
echo   Make sure you keep it up to date
echo   and in the correct place.
echo %ERRORLEVEL%
))
echo.
rem del %config_file%
pause
goto end

:end
call :colors 17
title L2JDP installer - Script execution finished
cls
echo.
echo L2JDP database_installer version 0.2.1
echo (C) 2007 L2J Datapack Team
echo database_installer comes with ABSOLUTELY NO WARRANTY;
echo This is free software, and you are welcome to redistribute it
echo under certain conditions; See the file gpl.txt for further
echo details.
echo.
echo Thanks for using our software.
echo visit http://www.l2jdp.com for more info about
echo the Official L2J Datapack project.
echo.
pause
color

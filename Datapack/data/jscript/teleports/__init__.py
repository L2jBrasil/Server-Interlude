__all__ = [
'1100_teleport_with_charm',
'1101_teleport_to_race_track',
'1102_toivortex_green',
'1102_toivortex_blue',
'1102_toivortex_red',
'1103_OracleTeleport',
'1104_NewbieTravelToken',
'1630_PaganTeleporters',
'2000_NoblesseTeleport',
'2211_HuntingGroundsTeleport',
'2400_toivortex_exit',
'6111_ElrokiTeleporters'
]
print ""
print "importing teleport data ..."
for name in __all__ :
    try :
        __import__('data.jscript.teleports.'+name,globals(), locals(), ['__init__'], -1)
    except:
        print "failed to import quest : ",name
print "... done"
print ""

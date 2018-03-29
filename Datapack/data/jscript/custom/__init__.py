__all__ = [
'3995_echo',
'4000_ShadowWeapons',
'7000_HeroItems',
'8000_RaidbossInfo',
'6050_KetraOrcSupport',
'6051_VarkaSilenosSupport'
]
print ""
print "importing custom data ..."
for name in __all__ :
    try :
        __import__('data.jscript.custom.'+name,globals(), locals(), ['__init__'], -1)
    except:
        print "failed to import quest : ",name
print "... done"
print ""

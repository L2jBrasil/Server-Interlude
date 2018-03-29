__all__ = [
'group_template',
'individual'
]
print ""
print "importing scriptable AI ..."
for name in __all__ :
    try :
        __import__('data.jscript.ai.'+name,globals(), locals(), ['__init__'], -1)
    except:
        print "failed to import quest : ",name
print"... done"
print ""

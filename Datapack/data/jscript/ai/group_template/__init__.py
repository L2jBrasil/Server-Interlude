__all__ = [
'polymorphing_angel',
'feedable_beasts',
'chests'
]

for name in __all__ :
    try :
        __import__(name,globals(), locals(), [], -1)
    except:
        print "failed to import quest : ",name

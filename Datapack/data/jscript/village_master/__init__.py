__all__ = [
'dwarven_occupation_change',
'elven_human_mystics_1',
'elven_human_mystics_2',
'elven_human_buffers_2',
'elven_human_fighters_1',
'dark_elven_change_1',
'dark_elven_change_2',
'orc_occupation_change_1',
'orc_occupation_change_2',
'30026_bitz_occupation_change',
'30031_biotin_occupation_change',
'30109_hannavalt_occupation_change',
'30154_asterios_occupation_change',
'30358_thifiell_occupation_change',
'30520_reed_occupation_change',
'30525_bronk_occupation_change',
'30565_kakai_occupation_change',
'9000_clan',
'9001_alliance'
]
print ""
print "importing village master data ..."
for name in __all__ :
    try :
        __import__('data.jscript.village_master.'+name,globals(), locals(), ['__init__'], -1)
    except:
        print "failed to import quest : ",name
print "... done"
print ""

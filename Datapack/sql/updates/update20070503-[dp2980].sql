ALTER TABLE armor ADD item_skill_id decimal(11,0) NOT NULL default '0';
ALTER TABLE armor ADD item_skill_lvl decimal(11,0) NOT NULL default '0';

ALTER TABLE weapon ADD item_skill_id decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD item_skill_lvl decimal(11,0) NOT NULL default '0';

ALTER TABLE weapon ADD enchant4_skill_id decimal(11,0) NOT NULL default '0'; -- for duals +4
ALTER TABLE weapon ADD enchant4_skill_lvl decimal(11,0) NOT NULL default '0';

ALTER TABLE weapon ADD onCast_skill_id decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD onCast_skill_lvl decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD onCast_skill_chance decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD onCrit_skill_id decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD onCrit_skill_lvl decimal(11,0) NOT NULL default '0';
ALTER TABLE weapon ADD onCrit_skill_chance decimal(11,0) NOT NULL default '0';


--         Boss jewelry        ---
UPDATE armor  SET item_skill_id = 3558, item_skill_lvl = 1 WHERE item_id = 6656; -- antharas earring 
UPDATE armor  SET item_skill_id = 3557, item_skill_lvl = 1 WHERE item_id = 6657; -- necklace of valakas 
UPDATE armor  SET item_skill_id = 3561, item_skill_lvl = 1 WHERE item_id = 6658; -- Ring of baium 
UPDATE armor  SET item_skill_id = 3559, item_skill_lvl = 1 WHERE item_id = 6659; -- Zaken earring 
UPDATE armor  SET item_skill_id = 3562, item_skill_lvl = 1 WHERE item_id = 6660; -- Ring of ant queen
UPDATE armor  SET item_skill_id = 3560, item_skill_lvl = 1 WHERE item_id = 6661; -- Earring of Orfen
UPDATE armor  SET item_skill_id = 3563, item_skill_lvl = 1 WHERE item_id = 6662; -- Ring of core
UPDATE armor  SET item_skill_id = 3604, item_skill_lvl = 1 WHERE item_id = 8191; -- Frintezza's Necklace

-- +4  passive skills ------
-- UPDATE weapon SET enchant4_skill_id = , enchant4_skill_lvl = WHERE item_id = ; --


--     passive weapon SAs     --
                                         --    swords   --
UPDATE weapon  SET item_skill_id = 3026, item_skill_lvl = 1 WHERE item_id = 4681; -- stormbringer 'critical anger' 
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 1 WHERE item_id = 4682; -- stormbringer 'focus' 
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4683; -- stormbringer 'light' 

UPDATE weapon  SET item_skill_id = 3007, item_skill_lvl = 2 WHERE item_id = 4684; -- shamshir 'guidance' 
UPDATE weapon  SET item_skill_id = 3018, item_skill_lvl = 2 WHERE item_id = 4685; -- shamshir 'back blow' 
UPDATE weapon  SET item_skill_id = 3028, item_skill_lvl = 2 WHERE item_id = 4686; -- shamshir 'rsk. evasion'

UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 2 WHERE item_id = 4687; -- katana 'focus' 
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 2 WHERE item_id = 4688; -- katana 'critical damage' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 2 WHERE item_id = 4689; -- katana 'haste'

UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 2 WHERE item_id = 4690; -- spirit sword 'critical damage' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 2 WHERE item_id = 4692; -- spirit sword 'haste'

UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 2 WHERE item_id = 4693; -- raid sword 'focus'

UPDATE weapon  SET item_skill_id = 3007, item_skill_lvl = 3 WHERE item_id = 4696; -- caliburs 'guidance' 
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 3 WHERE item_id = 4697; -- caliburs 'focus'
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 3 WHERE item_id = 4698; -- caliburs 'critical damage'

UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 3 WHERE item_id = 4699; -- sword of delusion 'focus'
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4700; -- sword of delusion 'health'
UPDATE weapon  SET item_skill_id = 3032, item_skill_lvl = 3 WHERE item_id = 4701; -- sword of delustion 'rsk. haste'
	  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 3 WHERE item_id = 4702; -- tsurugi 'focus'
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 3 WHERE item_id = 4703; -- tsurugi 'critical damage'
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 3 WHERE item_id = 4704; -- tsurugi 'haste'
  
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4705; -- sword of nightmare 'health'  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 3 WHERE item_id = 4706; -- sword of nightmare 'focus'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4707; -- sword of nightmare 'light'
  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 4 WHERE item_id = 4708; -- samurai long sword 'focus'  
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 4 WHERE item_id = 4709; -- samurai long sword 'critical damage'  
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 4 WHERE item_id = 4710; -- samurai long sword 'haste'

UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 1 WHERE item_id = 4711; -- flamberge 'critical damage'  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 1 WHERE item_id = 4712; -- flamberge 'focus'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4713; -- flamberge 'light'
  
UPDATE weapon  SET item_skill_id = 3007, item_skill_lvl = 5 WHERE item_id = 4714; -- keshanberk 'guidance'  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 5 WHERE item_id = 4715; -- keshanberk 'focus'  
UPDATE weapon  SET item_skill_id = 3018, item_skill_lvl = 5 WHERE item_id = 4716; -- keshanberk 'back blow'  

UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 6 WHERE item_id = 4717; -- sword of damascus 'focus'     
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 3 WHERE item_id = 4718; -- sword of damascus 'critical damage'
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 6 WHERE item_id = 4719; -- sword of damascus 'haste'

UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4720; -- tallum blade 'health'
UPDATE weapon  SET item_skill_id = 3028, item_skill_lvl = 1 WHERE item_id = 4721; -- tallum blade 'rsk.evasion' 
UPDATE weapon  SET item_skill_id = 3032, item_skill_lvl = 2 WHERE item_id = 4722; -- tallum blade 'rsk.haste'
  
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4723; -- great sword 'health'  
UPDATE weapon  SET item_skill_id = 3023, item_skill_lvl = 5 WHERE item_id = 4724; -- great sword 'critical damage'  
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 5 WHERE item_id = 4725; -- great sword 'focus'

UPDATE weapon  SET item_skill_id = 3073, item_skill_lvl = 1 WHERE item_id = 5638; -- elemental sword 'magic power'
UPDATE weapon  SET item_skill_id = 3072, item_skill_lvl = 1 WHERE item_id = 5604; -- elemental sword 'empower'

UPDATE weapon  SET item_skill_id = 3073, item_skill_lvl = 2 WHERE item_id = 5641; -- sword of miracles 'magic power'
UPDATE weapon  SET item_skill_id = 3047, item_skill_lvl = 2 WHERE item_id = 5643; -- sword of miracles 'acumen'

UPDATE weapon  SET item_skill_id = 3067, item_skill_lvl = 2 WHERE item_id = 5647; -- dark legions edge 'critical damage'
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 3 WHERE item_id = 5648; -- dark legions edge 'health'
UPDATE weapon  SET item_skill_id = 3071, item_skill_lvl = 2 WHERE item_id = 5649; -- dark legions edge 'rsk. focus' 


  --     passive weapon SAs     --
                                         --    blunts   --
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4726; -- big hammer 'health' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 1 WHERE item_id = 4727; -- big hammer 'rsk.focus'
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 1 WHERE item_id = 4728; -- big hammer 'haste' 

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 1 WHERE item_id = 4729; -- battle axe 'anger' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 1 WHERE item_id = 4730; -- battle axe 'rsk.focus'
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 1 WHERE item_id = 4731; -- battle axe 'haste' 

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 1 WHERE item_id = 4732; -- silver axe 'anger' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 1 WHERE item_id = 4733; -- silver axe 'rsk.focus' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 1 WHERE item_id = 4734; -- silver axe 'haste' 

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 1 WHERE item_id = 4735; -- skull graver 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4736; -- skull graver 'health' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 1 WHERE item_id = 4737; -- skull graver 'rsk.focus'

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 2 WHERE item_id = 4738; -- dwarven war hammer 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4739; -- dwarven war hammer 'health' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 2 WHERE item_id = 4740; -- dwarven war hammer 'haste' 

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 3 WHERE item_id = 4741; -- war axe 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4742; -- war axe 'health' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 3 WHERE item_id = 4743; -- war axe 'haste'

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 4 WHERE item_id = 4744; -- yaksa mace 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4745; -- yaksa mace 'health' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 4 WHERE item_id = 4746; -- yaksa mace 'rsk.focus' 

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 5 WHERE item_id = 4747; -- heav war axe 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4748; -- heavy war axe 'health' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 5 WHERE item_id = 4749; -- heavy war axe 'rsk.focus'

UPDATE weapon  SET item_skill_id = 3012, item_skill_lvl = 6 WHERE item_id = 4750; -- deadmans glory 'anger' 
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4751; -- deadmans glory 'health' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 6 WHERE item_id = 4752; -- deadmans glory 'haste' 

UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4753; -- art of battle axe 'health' 
UPDATE weapon  SET item_skill_id = 3027, item_skill_lvl = 6 WHERE item_id = 4754; -- art of battle axe 'rsk.focus' 
UPDATE weapon  SET item_skill_id = 3036, item_skill_lvl = 6 WHERE item_id = 4755; -- art of battle axe 'haste' 

UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 1 WHERE item_id = 4756; -- meteor shower 'health' 
UPDATE weapon  SET item_skill_id = 3010, item_skill_lvl = 1 WHERE item_id = 4757; -- meteor shower 'focus' 
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4758; -- meteor shower 'p.focus'  ?????
UPDATE weapon  SET item_skill_id = 3050, item_skill_lvl = 1 WHERE item_id = 5599; -- meteor shower 'focus'
UPDATE weapon  SET item_skill_id = 3056, item_skill_lvl = 2 WHERE item_id = 5601; -- meteor shower 'rsk. haste' 

UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 3 WHERE item_id = 5602; -- elysian 'health'
UPDATE weapon  SET item_skill_id = 3057, item_skill_lvl = 2 WHERE item_id = 5603; -- elysian 'anger'

-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = ;


--     passive weapon SAs     --
                                         --    daggers   --
UPDATE weapon  SET item_skill_id = 3033, item_skill_lvl = 1 WHERE item_id = 4761; -- cursed dagger 'rsk.haste'

UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 1 WHERE item_id = 4762; -- dark elven dagger 'focus'
UPDATE weapon  SET item_skill_id = 3019, item_skill_lvl = 1 WHERE item_id = 4763; -- dark elven dagger 'back blow' 
UPDATE weapon  SET item_skill_id = 3035, item_skill_lvl = 1 WHERE item_id = 4764; -- dark elven dagger 'might mortal'

UPDATE weapon  SET item_skill_id = 3035, item_skill_lvl = 2 WHERE item_id = 4767; -- stiletoo 'might mortal'
 
UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 3 WHERE item_id = 4768; -- grace dagger 'evasion' 
UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 3 WHERE item_id = 4769; -- grace dagger 'focus' 
UPDATE weapon  SET item_skill_id = 3019, item_skill_lvl = 3 WHERE item_id = 4770; -- grace dagger 'back blow' 

UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 3 WHERE item_id = 4771; -- dark screamer 'evasion' 
UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 3 WHERE item_id = 4772; -- dark screamer 'focus' 

UPDATE weapon  SET item_skill_id = 3035, item_skill_lvl = 4 WHERE item_id = 4776; -- crystal dagger 'might mortal'
 
UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 5 WHERE item_id = 4777; -- kris 'evasion' 
UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 5 WHERE item_id = 4778; -- kris 'focus' 
UPDATE weapon  SET item_skill_id = 3019, item_skill_lvl = 5 WHERE item_id = 4779; -- kris 'back blow' 

UPDATE weapon  SET item_skill_id = 3035, item_skill_lvl = 6 WHERE item_id = 4782; -- deamons sword 'might mortal'

UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 6 WHERE item_id = 4783; -- bloody orchid 'evasion' 
UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 2 WHERE item_id = 4784; -- bloody orchid 'focus' 
UPDATE weapon  SET item_skill_id = 3019, item_skill_lvl = 6 WHERE item_id = 4785; -- bloody orchid 'back blow' 
UPDATE weapon  SET item_skill_id = 3051, item_skill_lvl = 1 WHERE item_id = 5614; -- bloody orchid 'focus';

UPDATE weapon  SET item_skill_id = 3011, item_skill_lvl = 5 WHERE item_id = 4786; -- hell knife 'focus' 
UPDATE weapon  SET item_skill_id = 3019, item_skill_lvl = 5 WHERE item_id = 4787; -- hell knife 'back blow' 
UPDATE weapon  SET item_skill_id = 3035, item_skill_lvl = 5 WHERE item_id = 4788; -- hell knife 'might mortal' 

UPDATE weapon  SET item_skill_id = 3064, item_skill_lvl = 1 WHERE item_id = 5617; -- soul separator 'guidance'
UPDATE weapon  SET item_skill_id = 3066, item_skill_lvl = 2 WHERE item_id = 5618; -- soul separator 'critical damage'
UPDATE weapon  SET item_skill_id = 3056, item_skill_lvl = 2 WHERE item_id = 5619; -- sould separator 'rsk. haste';


--     passive weapon SAs     --
                                         --    fists    --
UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 1 WHERE item_id = 4791; -- chakram 'rsk. haste' 

UPDATE weapon  SET item_skill_id = 3030, item_skill_lvl = 3 WHERE item_id = 4792; -- fist blade 'rsk.evasion'
UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 3 WHERE item_id = 4793; -- fist blade 'rsk. haste' 
UPDATE weapon  SET item_skill_id = 3037, item_skill_lvl = 3 WHERE item_id = 4794; -- fist blade 'haste'  

UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 4 WHERE item_id = 4797; -- great pata 'rsk. haste' 

UPDATE weapon  SET item_skill_id = 3030, item_skill_lvl = 2 WHERE item_id = 4798; -- knuckle duster 'rsk. evasion' 
UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 2 WHERE item_id = 4799; -- knuckle duster 'rsk. haste' 
UPDATE weapon  SET item_skill_id = 3037, item_skill_lvl = 2 WHERE item_id = 4800; -- knuckle duster 'haste'  

UPDATE weapon  SET item_skill_id = 3030, item_skill_lvl = 5 WHERE item_id = 4802; -- arthro nail 'rsk. evasion'  
UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 5 WHERE item_id = 4803; -- arthro nail 'rsk. haste' 

UPDATE weapon  SET item_skill_id = 3034, item_skill_lvl = 6 WHERE item_id = 4806; -- bellion cestus 'rsk. haste'  

UPDATE weapon  SET item_skill_id = 3030, item_skill_lvl = 6 WHERE item_id = 4808; -- blood tornado 'rsk. evasion'
UPDATE weapon  SET item_skill_id = 3037, item_skill_lvl = 6 WHERE item_id = 4809; -- bloody tornado 'haste' 
UPDATE weapon  SET item_skill_id = 3068, item_skill_lvl = 2 WHERE item_id = 5620; -- blood tornado 'haste'
UPDATE weapon  SET item_skill_id = 3565, item_skill_lvl = 1 WHERE item_id = 5621; -- blood tornado 'focus'
UPDATE weapon  SET item_skill_id = 3058, item_skill_lvl = 1 WHERE item_id = 5622; -- blood tornado 'anger';

UPDATE weapon  SET item_skill_id = 3069, item_skill_lvl = 1 WHERE item_id = 5623; -- dragon grinder 'rsk. evasion' 
UPDATE weapon  SET item_skill_id = 3065, item_skill_lvl = 1 WHERE item_id = 5624; -- dragon grinder 'guidance'
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 3 WHERE item_id = 5625; -- dragon grinder 'health'


--     passive weapon SAs     --
                                            --    bows    --
UPDATE weapon  SET item_skill_id = 3008, item_skill_lvl = 1 WHERE item_id = 4810; -- crystalized ice bow 'guidance'  
UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 1 WHERE item_id = 4811; -- crystalized ice bow 'evasion' 
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4812; -- crystalized ice bow 'quite recovery'
  
UPDATE weapon  SET item_skill_id = 3008, item_skill_lvl = 2 WHERE item_id = 4813; -- elemental bow 'guidance'   
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4814; -- elemental bow 'miser'   
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4815; -- elemental bow 'quick recovery' 
 
UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 2 WHERE item_id = 4816; -- elven bow of nobility 'evasion'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4817; -- elven bow of nobility 'miser'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4817; -- elven bow of nobility 'cheap shot'
  
UPDATE weapon  SET item_skill_id = 3008, item_skill_lvl = 3 WHERE item_id = 4819; -- akat long bow 'guidance'  
UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 3 WHERE item_id = 4820; -- akat long bow 'evasion' 
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4821; --  akat long bow 'miser'

UPDATE weapon  SET item_skill_id = 3008, item_skill_lvl = 4 WHERE item_id = 4822; -- eminence bow 'guidance'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4823; --  eminence bow 'miser'
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4824; --  eminence bow 'cheap shot'

UPDATE weapon  SET item_skill_id = 3009, item_skill_lvl = 5 WHERE item_id = 4825; -- dark elven long bow 'evasion'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4827; -- dark elven long bow 'miser'  

UPDATE weapon  SET item_skill_id = 3008, item_skill_lvl = 6 WHERE item_id = 4828; -- bow of peril 'guidance'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4829; -- bow of peril 'quick recovery'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4830; -- bow of peril 'cheap shot'  

UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 1 WHERE item_id = 4832; -- carnage bow 'mana up'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4833; --  carnage 'quick recovery'
UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 2 WHERE item_id = 5610; -- carnage bow 'mana up'


--     passive weapon SAs     --
                                           --    pole    --
UPDATE weapon  SET item_skill_id = 3600, item_skill_lvl = 1 WHERE item_id = 4834; -- scythe 'anger'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4836; -- scythe 'light'  

UPDATE weapon  SET item_skill_id = 3600, item_skill_lvl = 1 WHERE item_id = 4837; -- orcish glaive 'anger'  
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4839; -- orcish glaive 'long blow'  

UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4841; -- body slasher 'long blow'  
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4842; -- body slasher 'wide blow'  

UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4844; -- bec de corbin 'long blow'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4845; -- bec de corbin 'light'
  
UPDATE weapon  SET item_skill_id = 3600, item_skill_lvl = 3 WHERE item_id = 4846; --  scorpion 'anger'
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4848; -- scorpion 'long blow'  

UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4850; -- widow maker 'long blow'  
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4851; -- widow maker 'wide blow'

UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4853; -- orcish poleaxe 'long blow'
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4854; -- orcish poleaxe 'wide blow'
 
UPDATE weapon  SET item_skill_id = 3600, item_skill_lvl = 5 WHERE item_id = 4855; -- great axe 'anger'  
-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = 4857; --  great axe 'light'

UPDATE weapon  SET item_skill_id = 3600, item_skill_lvl = 6 WHERE item_id = 4858; -- lance 'anger'
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4860; -- lance 'long blow' 
 
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4862; -- halberd 'long blow'  
UPDATE weapon  SET item_skill_id = 3599, item_skill_lvl = 1 WHERE item_id = 4863; -- halberd 'wide blow'
UPDATE weapon  SET item_skill_id = 3601, item_skill_lvl = 7 WHERE item_id = 5626; -- halberd 'haste'

UPDATE weapon  SET item_skill_id = 3602, item_skill_lvl = 8 WHERE item_id = 5632; -- tallum glaive 'guidance'
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 4 WHERE item_id = 5633; -- tallum glaive 'health'
UPDATE weapon  SET item_skill_id = 3068, item_skill_lvl = 2 WHERE item_id = 5636; -- tallum glaive 'haste'
UPDATE weapon  SET item_skill_id = 3057, item_skill_lvl = 1 WHERE item_id = 5637; -- tallum blade 'anger'


--     passive weapon SAs     --
                                           --    staff    --
UPDATE weapon  SET item_skill_id = 3031, item_skill_lvl = 1 WHERE item_id = 4867; -- crystal staff 'rsk. evasion' 
UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 1 WHERE item_id = 4868; -- crystal staff 'mana up'  

UPDATE weapon  SET item_skill_id = 3031, item_skill_lvl = 3 WHERE item_id = 4879; -- paagrio hammer 'rsk. evasion'  
 
UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 1 WHERE item_id = 4885; -- paagrio axe 'mana up' 
 
UPDATE weapon  SET item_skill_id = 3031, item_skill_lvl = 4 WHERE item_id = 4891; -- ghouls staff 'rsk. evasion'   
UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 1 WHERE item_id = 4892; -- ghouls staff 'mana up' 

UPDATE weapon  SET item_skill_id = 3014, item_skill_lvl = 1 WHERE item_id = 5596; -- dasparions staff 'mana up'
UPDATE weapon  SET item_skill_id = 3048, item_skill_lvl = 2 WHERE item_id = 5597; -- dasparions staff 'conversion'
UPDATE weapon  SET item_skill_id = 3047, item_skill_lvl = 2 WHERE item_id = 5598; -- dasparions staff 'acumen'

UPDATE weapon  SET item_skill_id = 3048, item_skill_lvl = 2 WHERE item_id = 5605; -- branch of the mother tree 'conversion'
UPDATE weapon  SET item_skill_id = 3552, item_skill_lvl = 1 WHERE item_id = 5606; -- branch of the mother tree 'magic damage'
UPDATE weapon  SET item_skill_id = 3047, item_skill_lvl = 2 WHERE item_id = 5607; -- branch of the mother tree 'acumen'

-- UPDATE weapon  SET item_skill_id = , item_skill_lvl = WHERE item_id = ; --  


--     passive weapon SAs     --
                                           --    big swords    --
UPDATE weapon  SET item_skill_id = 3013, item_skill_lvl = 3 WHERE item_id = 5644; -- dragon slayer 'health'


--   active onCrit weapons SA ----
-- UPDATE weapon SET onCrit_skill_id = , onCrit_skill_lvl = , onCrit_skill_chance = WHERE item_id = ; --


--   active onCast weapons SA  ---

-- UPDATE weapon SET onCast_skill_id = , onCast_skill_lvl = , onCast_skill_chance = WHERE item_id = ; --
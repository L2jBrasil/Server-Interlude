package com.l2jbr.gameserver.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ScriptingManager {
    private Logger logger = LoggerFactory.getLogger(ScriptingManager.class);

    private static ScriptingManager instance;
    private Map<String, ScriptEngine> scriptEngines;
    private ScriptEngineManager engineManager;

    private ScriptingManager(){
        load();
    }

    private void load() {
        engineManager = new ScriptEngineManager();
        scriptEngines = new HashMap<>(engineManager.getEngineFactories().size());
        engineManager.getEngineFactories().forEach(this::register);
    }

    private void register(ScriptEngineFactory scriptEngineFactory) {
        logger.info(getMessage("info.engine.register", scriptEngineFactory.getLanguageName(), scriptEngineFactory.getExtensions()));
        ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
        scriptEngine.setBindings(engineManager.getBindings(), ScriptContext.GLOBAL_SCOPE);
        scriptEngines.put(scriptEngineFactory.getLanguageName(), scriptEngine);
    }

   public ScriptEngine getEngine(String language) {
        ScriptEngine engine;
        if(scriptEngines.containsKey(language)) {
            engine = scriptEngines.get(language);
        } else {
            engine = engineManager.getEngineByName(language);
            if(nonNull(engine)) {
                scriptEngines.put(language, engine);
            }
        }
        return engine;
    }


    public static ScriptingManager getInstance() {
        return isNull(instance) ? instance = new ScriptingManager() : instance;
    }

}

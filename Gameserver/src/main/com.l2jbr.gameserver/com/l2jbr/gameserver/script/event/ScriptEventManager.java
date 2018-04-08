/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.script.event;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.script.Document;
import com.l2jbr.gameserver.script.Parser;
import com.l2jbr.gameserver.script.ParserFactory;
import com.l2jbr.gameserver.script.ParserNotCreatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class ScriptEventManager {

    private final Logger logger = LoggerFactory.getLogger(ScriptEventManager.class);
    private final String scriptDirectory = "data/script/";
    private static ScriptEventManager _instance;
    private LinkedList<Document> scripts;

    public static final Hashtable<String, ParserFactory> parserFactories = new Hashtable<>();

    private ScriptEventManager() {
        reloadScripts();
    }

    public static ScriptEventManager getInstance() {
        if (_instance == null) {
            _instance = new ScriptEventManager();
        }
        return _instance;
    }

    public void reloadScripts() {
        scripts = new LinkedList<>();
        loadScripts();
    }

    private void loadScripts() {
        File directory = new File(Config.DATAPACK_ROOT, scriptDirectory);
        logger.debug(getMessage("debug.script.loading", directory.getAbsolutePath()));

        File[] files = directory.listFiles(file -> file.getName().endsWith(".xml"));

        if (files == null) {
            logger.debug(getMessage("debug.script.not.found", directory.getAbsolutePath()));
            return;
        }

        for(File file : files) {
            loadScriptFile(file);
        }
        scripts.forEach(this::parseScript);
    }

    private void loadScriptFile(File file) {
        try {
            Document document = new Document(file);
            scripts.add(document);
        } catch (IOException | SAXException e) {
            logger.error(getMessage("error.script.not.loaded", file.getAbsolutePath()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void parseScript(Document script) {
        String scriptName = script.getName();
        logger.debug(getMessage("debug.parsing.script", scriptName));

        Node node = script.getFirstChild();
        String parserClass = String.format("event.Xml%sParser", node.getNodeName());

        Parser parser = createParser(scriptName, parserClass);
        if (parser == null) { return; }

        try {
            parser.parseScript(node);
            logger.debug(getMessage("debug.script.parsed", scriptName));
        } catch (Exception e) {
            logger.error(getMessage("error.script.not.parsed", scriptName));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private Parser createParser(String scriptName, String parserClass) {
        Parser parser = null;
        try {
            parser = createParser(parserClass);
        } catch (ParserNotCreatedException | ClassNotFoundException e) {
            logger.warn(getMessage("error.parser.script.not.found", parserClass));
            logger.debug(e.getLocalizedMessage(), e);
        }

        if (parser == null) {
            logger.warn(getMessage("error.unknown.script.type", scriptName));
            return null;
        }
        return parser;
    }

    private Parser createParser(String name) throws ParserNotCreatedException, ClassNotFoundException {
        ParserFactory s = parserFactories.get(name);
        if (s == null) {
            Class.forName("com.l2jbr.gameserver.script." + name);
            s = parserFactories.get(name);

            if (s == null) {
                throw new ParserNotCreatedException();
            }
        }
        return s.create();
    }
}

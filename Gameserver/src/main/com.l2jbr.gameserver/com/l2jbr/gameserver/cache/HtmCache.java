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
package com.l2jbr.gameserver.cache;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.FilterUtils;
import com.l2jbr.gameserver.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.commons.util.FilterUtils.htmlFilter;
import static java.util.Objects.isNull;

/**
 * @author Layane
 */
public class HtmCache {
	private static Logger _log = LoggerFactory.getLogger(HtmCache.class.getName());
	private static HtmCache INSTANCE;
	
	private final Map<Integer, String> _cache;
	
	private int _loadedFiles;
	private long _bytesBuffLen;
	
	public static HtmCache getInstance() {
		if (isNull(INSTANCE)) {
			INSTANCE = new HtmCache();
		}
		return INSTANCE;
	}
	
	public HtmCache() {
		_cache = new HashMap<>();
		reload();
	}
	
	public void reload() {
		reload(Config.DATAPACK_ROOT);
	}
	
	public void reload(File f) {
		if (!Config.LAZY_CACHE) {
			_log.info("Html cache start...");
			parseDir(f);
			_log.info("Cache[HTML]: {} megabytes on {} files loaded", getMemoryUsage(), getLoadedFiles());
		} else {
			_cache.clear();
			_loadedFiles = 0;
			_bytesBuffLen = 0;
			_log.info("Cache[HTML]: Running lazy cache");
		}
	}
	
	public void reloadPath(File f) {
		parseDir(f);
		_log.info("Cache[HTML]: Reloaded specified path.");
	}
	
	public double getMemoryUsage()
	{
		return ((float) _bytesBuffLen / 1048576);
	}
	
	public int getLoadedFiles()
	{
		return _loadedFiles;
	}
	
	private void parseDir(File dir) {
		var files = dir.listFiles(FilterUtils::htmlFilter);

		for (File file : files) {
			if (!file.isDirectory()) {
				loadFile(file);
			} else {
				parseDir(file);
			}
		}
	}
	
	public String loadFile(final File file) {
		if (file.exists() && htmlFilter(file) && !file.isDirectory()) {
			try {
                var content = Files.lines(file.toPath(), Charset.forName("UTF-8")).
                        reduce("", String::concat).replaceAll("\r\n", "\n");

                int hash = Util.getRelativePath(Config.DATAPACK_ROOT, file).hashCode();
                updateContent(content, hash);
                return content;
            } catch (IOException e) {
                _log.warn("problem with htm file " + e);
            }
		}
        return "";
	}

    private void updateContent(String content, int hash) {
        var old = _cache.get(hash);

        if(isNull(old)) {
            _bytesBuffLen += content.length();
            _loadedFiles++;
        } else {
            _bytesBuffLen += content.length() - old.length();
        }
        _cache.put(hash, content);
    }

    public String getHtmForce(String path)
	{
		String content = getHtm(path);
		
		if (content == null)
		{
			content = "<html><body>My text is missing:<br>" + path + "</body></html>";
			_log.warn("Cache[HTML]: Missing HTML page: " + path);
		}
		
		return content;
	}
	
	public String getHtm(String path)
	{
		String content = _cache.get(path.hashCode());
		
		if (Config.LAZY_CACHE && (content == null))
		{
			content = loadFile(new File(Config.DATAPACK_ROOT, path));
		}
		
		return content;
	}
	
	public boolean contains(String path)
	{
		return _cache.containsKey(path.hashCode());
	}
	
	/**
	 * Check if an HTM exists and can be loaded
	 * @param path The path to the HTM
	 * @return
	 */
	public boolean isLoadable(String path)
	{
		File file = new File(path);
		
		if (file.exists() && htmlFilter(file) && !file.isDirectory())
		{
			return true;
		}
		
		return false;
	}
}

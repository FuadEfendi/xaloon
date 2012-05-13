package org.xaloon.wicket.plugin.google.storage.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class GoogleImagePathUtil implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String getGoogleResizedPath(String imagePath, int size) {
		
		String[] path = imagePath.split("/");
		int lastElem = path.length-2;
		
		if (lastElem > 0) {
			if (isGoogleWidthProvided(path[lastElem])) {
				//replace existing with new one
				path[lastElem] = "s" + size;
				return StringUtils.join(path, "/");
			} else if (path.length > 1) {
				List<String> items = Arrays.asList(path);
				List<String> result = new ArrayList<String>(items);
				result.add(lastElem+1, "s" + size);
				return StringUtils.join(result, "/");
			}
		}
		return imagePath;
	}

	private static boolean isGoogleWidthProvided(String path) {
		if (!path.startsWith("s")) {
			return false;
		}
		String width = path.substring(1);
		return StringUtils.isNumeric(width);
	}

	public static boolean isPicasaImage(String path) {
		return !StringUtils.isEmpty(path) && path.contains("google");
	}
}

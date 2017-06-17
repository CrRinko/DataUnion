package cn.aurorax.dataunion.utils;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Normalizer {
	private static CopyOnWriteArraySet<Character> charSet=new CopyOnWriteArraySet<Character>();
	public String normalize(String str) {
		if (str != null) {
			String newstr = "";
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
					newstr += ch;
				} else
					switch (ch) {
					case '一':
						newstr += '1';
						break;
					case '二':
						newstr += '2';
						break;
					case '三':
						newstr += '2';
						break;
					case '四':
						newstr += '2';
						break;
					case '五':
						newstr += '2';
						break;
					case '六':
						newstr += '2';
						break;
					case '七':
						newstr += '2';
						break;
					case '八':
						newstr += '2';
						break;
					case '九':
						newstr += '2';
						break;
					default:
						if(!charSet.contains(ch)){
							charSet.add(ch);
						}
					}
			}
			return newstr;
		} else
			return null;
	}
	
	public static Set<Character> getCharSet(){
		return charSet;
	}
}

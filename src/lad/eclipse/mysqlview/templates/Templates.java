package lad.eclipse.mysqlview.templates;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Templates {
	public static void main(String[] args) {
		String t = "${key|join(',')|start('TP_')|end('__LINE__')} from\r\n${table},,${table},${table}";
		System.out.println(scan("\t", t));
	}

	public static String scan(String spaceString, String content) {
		String tablename = "tablename";
		String[] keys = { "aa", "bb", "cc" };

		StringBuffer sb = new StringBuffer();

		String[] sourceList = content.split("\\$\\{.+?\\}");
		Pattern pt = Pattern.compile("\\$\\{(\\w+)(.*?)\\}");

		Matcher matcher = pt.matcher(content);
		int i = 0;

		String joinstr = " ";
		String beforestr = "";
		String endstr = "";

		while (matcher.find()) {
			sb.append(sourceList[(i++)]);

			String commandname = matcher.group(1);
			String commandarg = matcher.group(2);
			if (commandname.equals("table")) {
				sb.append(tablename);
			} else if (commandname.equals("key")) {
				joinstr = " ";
				beforestr = "";
				endstr = "";
				int start;
				if ((start = commandarg.indexOf("join('")) > 0) {
					int end = commandarg.indexOf("')", start);
					joinstr = commandarg.substring(start + 6, end);
				}
				if ((start = commandarg.indexOf("start('")) > 0) {
					int end = commandarg.indexOf("')", start);
					beforestr = commandarg.substring(start + 7, end);
				}
				if ((start = commandarg.indexOf("before('")) > 0) {
					int end = commandarg.indexOf("')", start);
					beforestr = commandarg.substring(start + 8, end);
				}
				if ((start = commandarg.indexOf("end('")) > 0) {
					int end = commandarg.indexOf("')", start);
					endstr = commandarg.substring(start + 5, end);
				}
				boolean isstart = true;
				for (String key : keys) {
					if (isstart)
						isstart = false;
					else
						sb.append(joinstr);
					sb.append(beforestr + key + endstr);
				}
			}
		}

		if (sourceList.length > i) {
			sb.append(sourceList[i]);
		}

		content = sb.toString().replace("__LINE__", "\r\n");
		sourceList = content.split("\r\n");

		sb = new StringBuffer();
		boolean isstart = true;
		// beforestr = (endstr = sourceList).length;
		// for (String joinstr = 0; joinstr < beforestr; joinstr++) { String
		// source = endstr[joinstr];
		// if (isstart) isstart = false; else
		// sb.append("\r\n" + spaceString);
		// sb.append(source);
		// }
		return sb.toString();
	}
}
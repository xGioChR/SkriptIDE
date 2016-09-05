package com.skriptide.util;

import com.skriptide.guis.SceneManager;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Liz3ga on 04.09.2016.
 */
public class IDEPrintWriter extends PrintStream {


	public IDEPrintWriter(PrintStream out) {
		super(out);
	}

	@Override
	public void println(String msg) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d.M.Y HH:mm:ss");
		if (!msg.startsWith("["))
			msg = " " + msg;
		String f = "[" + sdf.format(cal.getTime()) + " | INFO]" + msg;
		if(SceneManager.debugArea != null) {
			SceneManager.debugArea.appendText(f + System.getProperty("line.separator"));
		}
		super.println(f);

	}
}

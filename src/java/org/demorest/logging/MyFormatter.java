package org.demorest.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(new Date())+" "+
               "["+record.getLevel()+"]"+" "+
               record.getSourceClassName()+"."+record.getSourceMethodName()+" "+
               record.getMessage()+"\n";
    }

}
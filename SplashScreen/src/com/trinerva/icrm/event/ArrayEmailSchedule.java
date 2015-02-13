package com.trinerva.icrm.event;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.trinerva.icrm.object.CalendarDetail;

public class ArrayEmailSchedule extends Vector<CalendarDetail> implements KvmSerializable {
    private static final long serialVersionUID = -1166006770093411055L;
    @Override
    public Object getProperty(int arg0) {
            return this.get(arg0);
    }
    @Override
    public int getPropertyCount() {
            return this.size();
    }
    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
            arg2.name = "EmailSchedule";
            arg2.type = CalendarDetail.class;
    }
    @Override
    public void setProperty(int arg0, Object arg1) {
            this.add((CalendarDetail)arg1);
    }
}

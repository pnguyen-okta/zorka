/**
 * Copyright 2012-2013 Rafal Lewczuk <rafal.lewczuk@jitlogic.com>
 * <p/>
 * This is free software. You can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jitlogic.zorka.common.tracedata;

import com.jitlogic.zorka.common.util.ZorkaUtil;

import java.io.Serializable;
import java.util.*;

public class MetricTemplate implements Serializable {

    public static final int RAW_DATA       = 1;
    public static final int RAW_DELTA      = 2;
    public static final int TIMED_DELTA    = 3;
    public static final int WINDOWED_RATE  = 4;
    public static final int UTILIZATION    = 5;

    private int id;
    private int type;

    private String name;
    private String units;

    private String nomField, divField;

    private double multiplier = 1.0;

    /** Names of dynamic attributes */
    private Set<String> dynamicAttrs = new HashSet<String>();

    private Map<String,Metric> metrics = new HashMap<String, Metric>();


    public MetricTemplate(int type, String name, String units) {
        this(type, name, units, null, null);
    }


    public MetricTemplate(int id, int type, String name, String units, String nomField, String divField) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.units = units;
        this.nomField = nomField;
        this.divField = divField;
    }



    public MetricTemplate(int type, String name, String units, String nomField, String divField) {
        this.type = type;
        this.name = name;
        this.units = units;
        this.nomField = nomField;
        this.divField = divField;
    }


    private MetricTemplate(MetricTemplate orig) {
        this.id = orig.id;
        this.type = orig.type;
        this.name = orig.name;
        this.nomField = orig.nomField;
        this.divField = orig.divField;
        this.multiplier = orig.getMultiplier();
        this.dynamicAttrs = new HashSet<String>();
        this.dynamicAttrs.addAll(orig.getDynamicAttrs());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MetricTemplate) {
            MetricTemplate mt = (MetricTemplate)obj;
            return type == mt.type
                && ZorkaUtil.objEquals(name, mt.name)
                && ZorkaUtil.objEquals(nomField, mt.nomField)
                && ZorkaUtil.objEquals(divField, mt.divField);
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        return (1117*type) ^ (name != null ? name.hashCode() : 0);
    }


    @Override
    public String toString() {
        return "MT(type=" + type + ", name=" + name + ")";
    }


    public int getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    public String getUnits() {
        return units;
    }


    public String getNomField() {
        return nomField;
    }


    public String getDivField() {
        return divField;
    }


    public double getMultiplier() {
        return multiplier;
    }


    public MetricTemplate multiply(double multiplier) {
        MetricTemplate mt = new MetricTemplate(this);
        mt.multiplier = multiplier;
        return mt;
    }


    public Set<String> getDynamicAttrs() {
        return dynamicAttrs;
    }


    public MetricTemplate dynamicAttrs(String...attrs) {
        MetricTemplate mt = new MetricTemplate(this);
        for (String attr : attrs) {
            mt.dynamicAttrs.add(attr);
        }
        return mt;
    }


    public MetricTemplate dynamicAttrs(Collection<String> attrs) {
        if (attrs == null) { return this; }
        MetricTemplate mt = new MetricTemplate(this);
        mt.dynamicAttrs.addAll(attrs);
        return mt;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public Metric getMetric(String key) {
        return metrics.get(key);
    }


    public void putMetric(String key, Metric metric) {
        metrics.put(key, metric);
    }

}

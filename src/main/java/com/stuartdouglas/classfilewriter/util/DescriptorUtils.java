/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.stuartdouglas.classfilewriter.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with method descriptors
 * 
 * @author Stuart Douglas
 * 
 */
public class DescriptorUtils {
    /**
     * Changes a class name to the internal form suitable for use in a descriptor string.
     * 
     * e.g. java.lang.String => Ljava/lang/String;
     */
    public static String extToInt(String className) {
        String repl = className.replace(".", "/");
        return 'L' + repl + ';';
    }

    /**
     * returns an array of String representations of the parameter types. Primitives are returned as their native
     * representations, while clases are returned in the internal descriptor form e.g. Ljava/lang/Integer;
     */
    public static String[] descriptorStringToParameterArray(String methodDescriptor) {
        int i = 1; // char 0 is a '('
        List<String> ret = new ArrayList<String>();
        int arraystart = -1;
        while (methodDescriptor.charAt(i) != ')') {
            String type = null;
            if (methodDescriptor.charAt(i) == '[') {
                if (arraystart == -1) {
                    arraystart = i;
                }
            } else {
                if (methodDescriptor.charAt(i) == 'L') {
                    int start = i;
                    i++;
                    while (methodDescriptor.charAt(i) != ';') {
                        ++i;
                    }
                    if (arraystart == -1) {
                        type = methodDescriptor.substring(start, i);
                    } else {
                        type = methodDescriptor.substring(arraystart, i);
                    }
                } else {
                    if (arraystart == -1) {
                        type = methodDescriptor.charAt(i) + "";
                    } else {
                        type = methodDescriptor.substring(arraystart, i + 1);
                    }
                }
                arraystart = -1;
                ret.add(type);
            }
            ++i;
        }
        String[] r = new String[ret.size()];
        for (int j = 0; j < ret.size(); ++j) {
            r[j] = ret.get(j);
        }
        return r;
    }

    public static String classToStringRepresentation(Class<?> c) {
        if (void.class.equals(c)) {
            return "V";
        } else if (byte.class.equals(c)) {
            return "B";
        } else if (char.class.equals(c)) {
            return "C";
        } else if (double.class.equals(c)) {
            return "D";
        } else if (float.class.equals(c)) {
            return "F";
        } else if (int.class.equals(c)) {
            return "I";
        } else if (long.class.equals(c)) {
            return "J";
        } else if (short.class.equals(c)) {
            return "S";
        } else if (boolean.class.equals(c)) {
            return "Z";
        } else if (c.isArray()) {
            return c.getName().replace(".", "/");
        } else
        // normal object
        {
            return extToInt(c.getName());
        }
    }

    /**
     * returns true if the descriptor represents a primitive type
     */
    public static boolean isPrimitive(String descriptor) {
        if (descriptor.length() == 1) {
            return true;
        }
        return false;
    }

    /**
     * returns true if the descriptor represents a long or a double
     * 
     */
    public static boolean isWide(String descriptor) {
        if (!isPrimitive(descriptor)) {
            return false;
        }
        char c = descriptor.charAt(0);
        if (c == 'D' || c == 'J') {
            return true;
        }
        return false;
    }

    /**
     * returns true if the class represents a long or a double
     * 
     */
    public static boolean isWide(Class<?> cls) {
        return cls == double.class || cls == long.class;
    }

    public static String getConstructorDescriptor(Constructor<?> c) {
        StringBuilder desc = new StringBuilder("(");
        for (Class<?> p : c.getParameterTypes()) {
            desc.append(DescriptorUtils.classToStringRepresentation(p));
        }
        desc.append(")");
        desc.append("V");
        return desc.toString();
    }

    public static String[] getParameterTypes(Method m) {
        String[] ret = new String[m.getParameterTypes().length];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = DescriptorUtils.classToStringRepresentation(m.getParameterTypes()[i]);
        }
        return ret;
    }

    public static String[] getParameterTypes(Class<?>[] parameters) {
        String[] ret = new String[parameters.length];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = DescriptorUtils.classToStringRepresentation(parameters[i]);
        }
        return ret;
    }

    public static String getMethodDescriptor(Method m) {
        StringBuilder desc = new StringBuilder("(");
        for (Class<?> p : m.getParameterTypes()) {
            desc.append(DescriptorUtils.classToStringRepresentation(p));
        }
        desc.append(")");
        desc.append(DescriptorUtils.classToStringRepresentation(m.getReturnType()));
        return desc.toString();
    }

   public static String getMethodDescriptor(String[] parameters, String returnType) {
        StringBuilder desc = new StringBuilder("(");
        for (String p : parameters) {
            desc.append(p);
        }
        desc.append(")");
        desc.append(returnType);
        return desc.toString();
    }
}
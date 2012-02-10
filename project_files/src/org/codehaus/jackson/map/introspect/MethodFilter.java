package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;

public abstract interface MethodFilter
{
  public abstract boolean includeMethod(Method paramMethod);
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.MethodFilter
 * JD-Core Version:    0.6.0
 */
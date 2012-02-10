package org.codehaus.jackson.map.util;

import java.util.Collection;

public abstract interface Provider<T>
{
  public abstract Collection<T> provide();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.Provider
 * JD-Core Version:    0.6.0
 */
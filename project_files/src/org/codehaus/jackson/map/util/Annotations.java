package org.codehaus.jackson.map.util;

import java.lang.annotation.Annotation;

public abstract interface Annotations
{
  public abstract <A extends Annotation> A get(Class<A> paramClass);

  public abstract int size();
}

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.Annotations
 * JD-Core Version:    0.6.0
 */
package com.zeroturnaround.callspy.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * All implementations can't direct match the class like {@link NameMatch} did.
 *
 * @author wusheng
 */
public interface IndirectMatch extends ClassMatch {
    ElementMatcher.Junction buildJunction();

    boolean isMatch(TypeDescription typeDescription);
}

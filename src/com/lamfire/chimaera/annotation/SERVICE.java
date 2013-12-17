package com.lamfire.chimaera.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-8
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface SERVICE {
    public abstract String command();
}

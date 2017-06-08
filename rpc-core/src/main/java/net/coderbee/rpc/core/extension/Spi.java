package net.coderbee.rpc.core.extension;

import java.lang.annotation.*;

/**
 * 标记一个可扩展的组件。
 *
 * @author coderbee on 2017/6/6.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spi {

	Scope scope() default Scope.PROTOTYPE;

}

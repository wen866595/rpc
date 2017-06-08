package net.coderbee.rpc.core.extension;

import java.lang.annotation.*;

/**
 * 描述一个可扩展组件的实现。
 *
 * @author coderbee on 2017/6/6.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpiMeta {

	String name();

}

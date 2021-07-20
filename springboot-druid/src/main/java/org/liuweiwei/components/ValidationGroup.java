package org.liuweiwei.components;

/**
 * Specify one or more validation groups to apply to the validation step kicked off by this annotation.
 * 指定一个或多个验证组以应用于此批注启动的验证步骤。
 * JSR-303 defines validation groups as custom annotations which an application declares for the sole purpose of using them as type-safe group arguments, as implemented in {SpringValidatorAdapter}.
 * JSR-303 将验证组定义为自定义注释，应用程序声明这些注释的唯一目的是将它们用作类型安全组参数，在 {SpringValidatorAdapter} 中实现。
 * Other {SmartValidator} implementations may support class arguments in other ways as well.
 * 其他 {SmartValidator} 实现也可能以其他方式支持类参数。
 *
 * @author Liuweiwei
 * @since 2021-07-20
 */
public interface ValidationGroup {

}

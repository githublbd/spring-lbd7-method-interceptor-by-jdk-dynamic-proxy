package com.allen.tinyioc.beans.factory;

import java.lang.reflect.Field;

import com.allen.tinyioc.BeanReference;
import com.allen.tinyioc.beans.BeanDefinition;
import com.allen.tinyioc.beans.PropertyValue;

/**
 * 可自动装配内容的BeanFactory
 * 
 * @author yihua.huang@dianping.com
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean = createBeanInstance(beanDefinition);
        beanDefinition.setBean(bean);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
		for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
			Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
			declaredField.setAccessible(true);
			Object value = propertyValue.getValue();
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			declaredField.set(bean, value);
		}
	}
}

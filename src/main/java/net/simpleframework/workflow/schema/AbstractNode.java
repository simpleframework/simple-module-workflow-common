package net.simpleframework.workflow.schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.common.Convert;
import net.simpleframework.common.coll.CollectionUtils;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlAttri;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AbstractNode extends AbstractElementBean {
	private final AbstractNode parent;

	public AbstractNode(final XmlElement beanElement, final AbstractNode parent) {
		super(beanElement);
		this.parent = parent;
		parseElement();
	}

	public AbstractNode getParent() {
		return parent;
	}

	protected Iterator<?> children(final String name) {
		final XmlElement parent = getElement().element(name);
		return parent != null ? parent.elementIterator() : CollectionUtils.EMPTY_ITERATOR;
	}

	protected XmlElement child(final String name, final boolean insert) {
		final XmlElement element = getElement();
		XmlElement child = element.element(name);
		if (child == null && insert) {
			child = element.addElement(name);
		}
		return child;
	}

	private final Properties properties = new Properties();

	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public void setProperty(final String key, final Object value) {
		properties.setProperty(key, String.valueOf(value));
	}

	public void removeProperty(final String key) {
		properties.remove(key);
	}

	public int getIntProperty(final String key, final int defaultValue) {
		return Convert.toInt(getProperty(key), defaultValue);
	}

	public int getIntProperty(final String key) {
		return getIntProperty(key, 0);
	}

	public double getDoubleProperty(final String key, final int defaultValue) {
		return Convert.toDouble(getProperty(key), defaultValue);
	}

	public double getDoubleProperty(final String key) {
		return getDoubleProperty(key, 0);
	}

	@Override
	public void syncElement() {
		super.syncElement();

		final XmlElement pElement = child("properties", properties.size() > 0);
		if (pElement != null) {
			pElement.clearContent();
			for (final Map.Entry<?, ?> entry : properties.entrySet()) {
				setElementAttribute(pElement, (String) entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void parseElement() {
		super.parseElement();
		final XmlElement properties = getElement().element("properties");
		if (properties == null) {
			return;
		}
		final Iterator<?> it = properties.attributeIterator();
		while (it.hasNext()) {
			final XmlAttri attribute = (XmlAttri) it.next();
			setProperty(attribute.getName(), attribute.getValue());
		}
	}

	@Override
	protected boolean isRemoveErrorAttribute() {
		return true;
	}
}

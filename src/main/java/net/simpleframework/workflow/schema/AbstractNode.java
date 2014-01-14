package net.simpleframework.workflow.schema;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.common.Convert;
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

	public AbstractNode parent() {
		return parent;
	}

	protected Iterator<?> children(final String name) {
		final XmlElement parent = child(name);
		final Iterator<?> it = parent != null ? parent.elementIterator() : null;
		return it != null ? it : Collections.emptyList().iterator();
	}

	protected XmlElement addChild(final String parentName, final String name, final boolean clear) {
		XmlElement parent = child(parentName);
		if (parent == null) {
			parent = addElement(parentName);
		} else if (clear) {
			parent.clearContent();
		}
		return parent.addElement(name);
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
		XmlElement pElement = child("properties");
		if (pElement == null) {
			pElement = addElement("properties");
		} else {
			pElement.clearContent();
		}
		for (final Map.Entry<?, ?> entry : properties.entrySet()) {
			setElementAttribute(pElement, (String) entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void parseElement() {
		super.parseElement();
		final XmlElement properties = child("properties");
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

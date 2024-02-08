package org.xomda.core.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xomda.core.XOMDA;
import org.xomda.core.extension.XOmdaExtension;
import org.xomda.core.module.XOmdaTypeRefs;
import org.xomda.core.module.template.GenerateEntityTemplate;
import org.xomda.template.Template;
import org.xomda.template.TemplateContext;

public class XOMDAUtilsTest {

	static interface XOMDAUtilsTestTemplate extends Template<XOMDAUtilsTest> {
	}

	static class TestTemplate implements Template<XOMDAUtilsTest> {
		@Override
		public void generate(final XOMDAUtilsTest o, final TemplateContext context) throws IOException {
			// noop
		}
	}

	static class TestTemplate2 extends TestTemplate {

	}

	@Test
	public void testIsExtensionClass() {
		assertTrue(XOMDAUtils.isExtensionClass((new XOmdaExtension() {
		}).getClass()));

		assertTrue(XOMDAUtils.isExtensionClass(GenerateEntityTemplate.class));
		assertTrue(XOMDAUtils.isExtensionClass(TestTemplate.class));
		assertTrue(XOMDAUtils.isExtensionClass(XOmdaTypeRefs.class));

		assertFalse(XOMDAUtils.isExtensionClass(XOMDAUtils.class));
	}

	@Test
	public void testIsTemplateClass() {
		assertTrue(XOMDAUtils.isTemplateClass(TestTemplate.class));
		assertFalse(XOMDAUtils.isTemplateClass(XOMDAUtils.class));
		assertFalse(XOMDAUtils.isTemplateClass(XOmdaTypeRefs.class));

		assertTrue(XOMDAUtils.isTemplateClass(TestTemplate.class, XOMDAUtilsTest.class));
		assertTrue(XOMDAUtils.isTemplateClass(TestTemplate2.class, XOMDAUtilsTest.class));

		assertFalse(XOMDAUtils.isTemplateClass(TestTemplate.class, XOMDA.class));
		assertFalse(XOMDAUtils.isTemplateClass(TestTemplate2.class, XOMDA.class));
	}

	@Test
	public void testIsTemplate() {
		assertTrue(XOMDAUtils.isTemplateClass(TestTemplate.class));
		assertFalse(XOMDAUtils.isTemplateClass(XOMDAUtils.class));
		assertFalse(XOMDAUtils.isTemplateClass(XOmdaTypeRefs.class));
	}

}

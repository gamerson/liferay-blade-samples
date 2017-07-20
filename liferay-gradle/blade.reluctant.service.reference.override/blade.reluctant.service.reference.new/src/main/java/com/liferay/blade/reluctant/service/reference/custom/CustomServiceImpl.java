package com.liferay.blade.reluctant.service.reference.custom;

import com.liferay.blade.reluctant.service.reference.old.service.api.SomeService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author James Hinkey
 */
@Component(immediate = true, service = SomeService.class)
public class CustomServiceImpl implements SomeService {

	@Override
	public String doSomething() {
		StringBuilder sb = new StringBuilder();

		Class<?> clazz = getClass();

		sb.append(clazz.getName());

		sb.append(", which delegates to ");
		sb.append(_defaultService.doSomething());

		return sb.toString();
	}

	@Reference (
		target = "(component.name=com.liferay.blade.reluctant.service.reference.old.service.impl.SomeServiceImpl)",
		unbind = "-"
	)
	private SomeService _defaultService;

}

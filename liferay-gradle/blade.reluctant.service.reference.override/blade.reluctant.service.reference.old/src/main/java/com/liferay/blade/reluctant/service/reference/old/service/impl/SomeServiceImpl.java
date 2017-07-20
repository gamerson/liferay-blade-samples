package com.liferay.blade.reluctant.service.reference.old.service.impl;

import com.liferay.blade.reluctant.service.reference.old.service.api.SomeService;

import org.osgi.service.component.annotations.Component;

/**
 * @author James Hinkey
 */
@Component(immediate = true, service = SomeService.class)
public class SomeServiceImpl implements SomeService {

	@Override
	public String doSomething() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

}
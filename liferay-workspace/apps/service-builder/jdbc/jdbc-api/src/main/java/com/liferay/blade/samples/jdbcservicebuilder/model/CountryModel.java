/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.blade.samples.jdbcservicebuilder.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

/**
 * The base model interface for the Country service. Represents a row in the &quot;country&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.blade.samples.jdbcservicebuilder.model.impl.CountryModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.blade.samples.jdbcservicebuilder.model.impl.CountryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Country
 * @see com.liferay.blade.samples.jdbcservicebuilder.model.impl.CountryImpl
 * @see com.liferay.blade.samples.jdbcservicebuilder.model.impl.CountryModelImpl
 * @generated
 */
@ProviderType
public interface CountryModel extends BaseModel<Country> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a country model instance should use the {@link Country} interface instead.
	 */

	/**
	 * Returns the primary key of this country.
	 *
	 * @return the primary key of this country
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this country.
	 *
	 * @param primaryKey the primary key of this country
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the country ID of this country.
	 *
	 * @return the country ID of this country
	 */
	public long getCountryId();

	/**
	 * Sets the country ID of this country.
	 *
	 * @param countryId the country ID of this country
	 */
	public void setCountryId(long countryId);

	/**
	 * Returns the country name of this country.
	 *
	 * @return the country name of this country
	 */
	@AutoEscape
	public String getCountryName();

	/**
	 * Sets the country name of this country.
	 *
	 * @param countryName the country name of this country
	 */
	public void setCountryName(String countryName);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(Country country);

	@Override
	public int hashCode();

	@Override
	public CacheModel<Country> toCacheModel();

	@Override
	public Country toEscapedModel();

	@Override
	public Country toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}
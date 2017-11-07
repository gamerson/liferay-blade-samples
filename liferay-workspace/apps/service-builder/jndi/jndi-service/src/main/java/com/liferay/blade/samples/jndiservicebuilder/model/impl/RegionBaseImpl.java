/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.samples.jndiservicebuilder.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.blade.samples.jndiservicebuilder.model.Region;
import com.liferay.blade.samples.jndiservicebuilder.service.RegionLocalServiceUtil;

/**
 * The extended model base implementation for the Region service. Represents a row in the &quot;region&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link RegionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RegionImpl
 * @see Region
 * @generated
 */
@ProviderType
public abstract class RegionBaseImpl extends RegionModelImpl implements Region {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a region model instance should use the {@link Region} interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			RegionLocalServiceUtil.addRegion(this);
		}
		else {
			RegionLocalServiceUtil.updateRegion(this);
		}
	}
}

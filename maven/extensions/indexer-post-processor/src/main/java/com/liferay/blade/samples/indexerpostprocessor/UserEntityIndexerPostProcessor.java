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

package com.liferay.blade.samples.indexerpostprocessor;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Liferay
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.portal.kernel.model.User",
		"indexer.class.name=com.liferay.portal.kernel.model.UserGroup"
	},
	service = IndexerPostProcessor.class
)
public class UserEntityIndexerPostProcessor implements IndexerPostProcessor {

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter booleanFilter, SearchContext searchContext)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessContextBooleanFilter");
		}
	}

	@Override
	public void postProcessContextQuery(
			BooleanQuery contextQuery, SearchContext searchContext)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessContextQuery");
		}
	}

	@Override
	public void postProcessDocument(Document document, Object obj)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessDocument");
		}
	}

	@Override
	public void postProcessFullQuery(
			BooleanQuery fullQuery, SearchContext searchContext)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessFullQuery");
		}
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter booleanFilter,
			SearchContext searchContext)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessSearchQuery");
		}
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessSearchQuery");
		}
	}

	@Override
	public void postProcessSummary(
		Summary summary, Document document, Locale locale, String snippet) {

		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessSummary");
		}
	}

	private Logger _logger = LoggerFactory.getLogger(getClass().getName());

}
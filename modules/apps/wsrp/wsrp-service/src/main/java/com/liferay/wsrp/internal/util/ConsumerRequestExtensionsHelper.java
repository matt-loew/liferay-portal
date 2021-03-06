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

package com.liferay.wsrp.internal.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.wsrp.configuration.WSRPGroupServiceConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oasis.names.tc.wsrp.v2.types.UserProfile;

import org.apache.axis.message.MessageElement;

/**
 * @author Michael C. Han
 */
public class ConsumerRequestExtensionsHelper {

	public static void addClientAttributes(
		List<MessageElement> clientAttributes) {

		_instance._addClientAttributes(clientAttributes);
	}

	public static void addUserProfileAttributes(
		UserProfile userProfile, User user) {

		_instance._addUserProfileAttributes(userProfile, user);
	}

	private ConsumerRequestExtensionsHelper() {
		try {
			_initConsumerRequestExtensions();
		}
		catch (Exception e) {
			_log.error("Unable to instantiate consumer request extension", e);

			throw new RuntimeException(e);
		}
	}

	private void _addClientAttributes(List<MessageElement> clientAttributes) {
		for (ConsumerRequestExtension consumerRequestExtension :
				_consumerRequestExtensions) {

			consumerRequestExtension.addClientAttributes(clientAttributes);
		}
	}

	private void _addUserProfileAttributes(UserProfile userProfile, User user) {
		for (ConsumerRequestExtension consumerRequestExtension :
				_consumerRequestExtensions) {

			consumerRequestExtension.addUserProfileAttributes(
				userProfile, user);
		}
	}

	private void _initConsumerRequestExtensions() throws Exception {
		WSRPGroupServiceConfiguration wsrpGroupServiceConfiguration =
			WSRPConfigurationUtil.getWSRPConfiguration();

		String[] consumerRequestExtensionsClassNames =
			wsrpGroupServiceConfiguration.consumerRequestExtensions();

		if (consumerRequestExtensionsClassNames.length == 0) {
			return;
		}

		_consumerRequestExtensions = new ArrayList<>(
			consumerRequestExtensionsClassNames.length);

		for (String consumerRequestExtensionClassName :
				consumerRequestExtensionsClassNames) {

			if (Validator.isNotNull(consumerRequestExtensionClassName)) {
				ConsumerRequestExtension consumerRequestExtension =
					(ConsumerRequestExtension)InstanceFactory.newInstance(
						consumerRequestExtensionClassName);

				_consumerRequestExtensions.add(consumerRequestExtension);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConsumerRequestExtensionsHelper.class);

	private static final ConsumerRequestExtensionsHelper _instance =
		new ConsumerRequestExtensionsHelper();

	private List<ConsumerRequestExtension> _consumerRequestExtensions =
		Collections.emptyList();

}
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

package com.liferay.message.boards.uad.display;

import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.model.MBCategory;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.Portal;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author William Newbury
 */
@Component(immediate = true, service = MBCategoryUADEntityDisplayHelper.class)
public class MBCategoryUADEntityDisplayHelper {

	/**
	 * Returns an ordered string array of the fields' names to be displayed.
	 * Each field name corresponds to a table column based on the order they are
	 * specified.
	 *
	 * @return the array of field names to display
	 */
	public String[] getDisplayFieldNames() {
		return new String[] {"name", "description"};
	}

	public String getMBCategoryEditURL(
			MBCategory mbCategory, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			portal.getControlPanelPlid(liferayPortletRequest),
			MBPortletKeys.MESSAGE_BOARDS, PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcRenderCommandName", "/wiki/edit_category");
		portletURL.setParameter(
			"redirect", portal.getCurrentURL(liferayPortletRequest));
		portletURL.setParameter(
			"mbCategoryId", String.valueOf(mbCategory.getCategoryId()));

		return portletURL.toString();
	}

	public Map<String, Object> getUADEntityNonanonymizableFieldValues(
		MBCategory mbCategory) {

		Map<String, Object> uadEntityNonanonymizableFieldValues =
			new HashMap<>();

		uadEntityNonanonymizableFieldValues.put(
			"description", mbCategory.getDescription());
		uadEntityNonanonymizableFieldValues.put("name", mbCategory.getName());

		return uadEntityNonanonymizableFieldValues;
	}

	@Reference
	protected Portal portal;

}
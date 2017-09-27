package com.beenet.java;

import java.util.Collections;
import java.util.List;

import blackboard.data.user.User;
import blackboard.data.user.User.SystemRole;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

public final class RoleValidator {

	private final List<SystemRole> systemRoles;

	public RoleValidator(final List<SystemRole> systemRoles) {

		this.systemRoles = systemRoles;
	}

	public RoleValidator() {

		this(Collections.singletonList(SystemRole.SYSTEM_ADMIN));
	}

	public final String getValidatedUserName() {

		final Context context = ContextManagerFactory.getInstance().getContext();
		final User user = context.getUser();
		return user == null || !systemRoles.contains(user.getSystemRole()) ? null : user.getUserName();
	}
}
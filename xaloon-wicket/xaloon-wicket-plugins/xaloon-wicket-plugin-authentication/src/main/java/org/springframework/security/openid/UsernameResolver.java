package org.springframework.security.openid;

import java.io.Serializable;
import java.util.List;

import org.openid4java.discovery.Identifier;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.core.api.util.UrlUtil;

public class UsernameResolver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AuthenticationAttribute> attributes;
	
	private Identifier verified;
	
	public UsernameResolver(List<AuthenticationAttribute> attributes, Identifier verified) {
		this.attributes = attributes;
		this.verified = verified;
	}

	public String resolveUsername() {
		if (attributes != null && !attributes.isEmpty()) {
			StringBuilder usernameBuilder = new StringBuilder();
			for (AuthenticationAttribute authenticationAttribute : attributes) {
				if (AuthenticationConsumer.PARAM_FIRST_NAME.equals(authenticationAttribute.getName())) {
					if (usernameBuilder.length() > 0) {
						usernameBuilder.append(DelimiterEnum.DOT.value());
					}
					usernameBuilder.append(authenticationAttribute.getValue());
				}
				if (AuthenticationConsumer.PARAM_LAST_NAME.equals(authenticationAttribute.getName())) {
					if (usernameBuilder.length() > 0) {
						usernameBuilder.append(DelimiterEnum.DOT.value());
					}
					usernameBuilder.append(authenticationAttribute.getValue());
				}
			}
			return UrlUtil.encode(usernameBuilder.toString().toLowerCase());
		}
		if (verified != null) {
			return verified.getIdentifier();
		}
		return null;
	}
}

package io.github.clovisgargione.msautenticacao.config.security.customGrantType;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

public class CustomPasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
	String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
	if (!"password".equals(grantType)) {
	    return null;
	}
	Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
	MultiValueMap<String, String> parameters = getParameters(request);
	Map<String, Object> additionalParameters = new HashMap<>();
	parameters.forEach((key, value) -> {
	    if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID)) {
		additionalParameters.put(key, value.get(0));
	    }
	});
	return new CustomPasswordGrantAuthenticationToken(grantType, clientPrincipal, additionalParameters);
    }

    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
	Map<String, String[]> parameterMap = request.getParameterMap();
	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
	parameterMap.forEach((key, values) -> {
	    if (values.length > 0) {
		for (String value : values) {
		    parameters.add(key, value);
		}
	    }
	});
	return parameters;
    }

}

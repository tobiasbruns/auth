/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tobiasbruns.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tokens")
public class TokenController {

	@Autowired
	private AuthenticationService service;

	@RequestMapping(method = RequestMethod.POST)
	public TbAuthToken authenticate(@RequestBody UserCredentials userCredentials) {
		return service.authenticate(userCredentials);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "X-Auth-Token")
	public TbAuthToken refreshToken(@RequestHeader(name = "X-Auth-Token") String token) {
		return service.refreshToken(token);
	}
}
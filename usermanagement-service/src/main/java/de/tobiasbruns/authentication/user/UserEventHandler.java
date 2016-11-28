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
package de.tobiasbruns.authentication.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

/**
 * Created by on 26.09.2016.
 *
 * @author Tobias Bruns
 */
//@Component
//@RepositoryEventHandler(User.class)
public class UserEventHandler {

    @Autowired
    private UserRepository userRepository;

    @HandleBeforeSave
    public void saveUser(User user) {
        if (user.getPassword() == null)
            user.setPassword(userRepository.findOne(user.getUserId()).getPassword());
    }
}

package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.demo.UserService;

/**
 * @author coderbee on 2017/10/21.
 */
public class UserServiceImpl implements UserService {

	@Override
	public String getNameById(String userId) {
		return userId;
	}

}

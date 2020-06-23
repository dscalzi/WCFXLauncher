package com.westeroscraft.auth;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class YggdrasilAuthManager {

	private final YggdrasilAuthenticationService authService;

	public YggdrasilAuthManager() {
		this.authService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "null");
	}
	
	public static void main(String[] args){
		YggdrasilAuthManager auth = new YggdrasilAuthManager();
		auth.authenticate("user", "pass", "null");
	}

	public Object authenticate(String name, String pass, String clientToken) {
		UserAuthentication auth = new YggdrasilUserAuthentication(authService, Agent.MINECRAFT);
		auth.setUsername(name);
		auth.setPassword(pass);
		try {
			auth.logIn();
			System.out.println(auth.getSelectedProfile().toString());
			return auth;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return e;
		}
	}
}

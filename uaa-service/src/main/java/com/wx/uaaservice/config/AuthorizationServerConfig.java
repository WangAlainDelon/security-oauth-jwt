package com.wx.uaaservice.config;


import com.wx.uaaservice.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Created by IntelliJ IDEA.
 * User: wangxiang
 * Date: 2019/9/23
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    /**
     * 配置授权的Token节点和Token服务，用来配置令牌端点(Token Endpoint)的安全约束.
     *
     * @param security
     * @throws Exception
     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
////                .realm("user-service")  //安全域的配置
//                .tokenKeyAccess("permitAll()")
////                .checkTokenAccess("isAuthenticated()") //检查token接口没有允许，默认是不允许访问,默认会返回{"timestamp":"2019-09-22T07:55:52.530+0000","status":401,"error":"Unauthorized","message":"Unauthorized","path":"/oauth/check_token"}
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients();
//    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
     * 能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     * clientId：（必须的）用来标识客户的Id。
     * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
     * redirectUris 返回地址,可以理解成登录后的返回地址，可以多个。
     * 应用场景有:客户端swagger调用服务端的登录页面,登录成功，返回客户端swagger页面
     * authorizedGrantTypes：此客户端可以使用的权限（基于Spring Security authorities）
     * authorization_code：授权码类型、implicit：隐式授权类型、password：资源所有者（即用户）密码类型、
     * client_credentials：客户端凭据（客户端ID以及Key）类型、refresh_token：通过以上授权获得的刷新令牌来获取新的令牌。
     * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
     * accessTokenValiditySeconds token有效时长
     * refreshTokenValiditySeconds refresh_token有效时长
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //将客户端的信息配置在内存当中
        clients.inMemory()
                .withClient("user-service")
                .secret(new BCryptPasswordEncoder().encode("123456"))
                .redirectUris("")
                .autoApprove(true)
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
//                .authorizedGrantTypes("authorization_code", "client_credentials", "password", "refresh_token")
//                .authorizedGrantTypes("password")
                .scopes("server")
//                .resourceIds("user-service")
                .accessTokenValiditySeconds(60 * 60)
                .refreshTokenValiditySeconds(60 * 60);
    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     * 访问地址：/oauth/token
     * 属性列表:
     * authenticationManager：认证管理器，当你选择了资源所有者密码（password）授权类型的时候，
     * 需要设置为这个属性注入一个 AuthenticationManager 对象。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore()) //Token的存储方式，采用的方式是将Token储存在内存当中
                .authenticationManager(authenticationManager) //设置认证管理器，密码认证配置，只有配置了该选项密码才会开启认证
//                .userDetailsService(userDetailsService) //配置获取用户认证信息接口,  //refresh_token 需要 UserDetailsService is required
                .tokenEnhancer(jwtTokenEnhancer())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); //设置访问/oauth/token接口，获取token的方式
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("wx-jwt.jks"), "wx1234".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("wx-jwt"));
        return converter;
    }
}

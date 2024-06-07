package com.learn.microservcies.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

//GlobalFilter
@Component
public class LoggingFilterThroughApiGateway implements GlobalFilter {

	private Logger logger = LoggerFactory.getLogger(LoggingFilterThroughApiGateway.class);
	
	/**
	 * Process the Web request and (optionally) delegate to the next {@code GatewayFilter}
	 * through the given {@link GatewayFilterChain}.
	 * @param exchange the current server exchange
	 * @param GatewayFilterChain chain provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 * 
	 * IN Global filter also, one can add Authentication for every Microservice call or for logging for any microservice call.
	 * 
	 */
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("Log every request which goes through Gateway: -> {}",  exchange.getRequest().getURI());	
		return chain.filter(exchange);
	}

}

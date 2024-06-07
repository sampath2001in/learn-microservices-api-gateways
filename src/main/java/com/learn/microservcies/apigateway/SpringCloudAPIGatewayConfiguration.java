package com.learn.microservcies.apigateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/* * This class is a configuration to route the request to desired Uri using gateway */
@Configuration
public class SpringCloudAPIGatewayConfiguration {
	//Need to return back a routeLocator, for that we need a RouteLocatorBuilder
	// using this builder we can customize the route what we want to use
	@Bean
	public RouteLocator gatewayUrlRouter(RouteLocatorBuilder routeLocatorBuilder) {
		/*//Need to return back a routeLocator, for that we need a RouteLocatorBuilder
		 * 		route function Creates a new Route.

				Parameters:fn a function which takes in a PredicateSpec and returns a Route.AsyncBuilderReturns:a Builder
				Function<Input, Output>
				Function<PredicateSpec, buildable Route>
				eg: Input: Takes the path (if it contains this currency-exchange etc.) 
					output: then it returns the uri
					
				As we use RouteLocator to route for spring api gateway, we should not use
				client discovery locator in application.properties, we need to comment that.	
		 */		
		/* If the url path contains /currency-exchange/** it will route to the service currency-exchange
		 * which is registered in the Eureka with auto load balancing (lb)

		 * /currency-exchange/** ==> "lb://currency-exchange"
		 * 							"load balancing://<< NAME PRESENT IN EUREKA SERVER >>"
		 * /currency-conversion/** ==> "lb://currency-conversion"
		 * 							"load balancing://<< NAME PRESENT IN EUREKA SERVER >>"  */
		RouteLocator routeLocator = routeLocatorBuilder.routes()
									.route(x -> x.path("/get")
												.filters(f -> f.addRequestHeader("MyName", "Sampath")//Auth request header - can achieve any auth using this.
												.addRequestParameter("AnyAuthParam", "Auth")) //Auth parameters can be given - can achieve any auth using this. 
												.uri("https://httpbin.org")) //You can give any Microservice
									.route(x -> x.path("/currency-exchange/**") //Incoming url has /currency-exchange/** which will be mapped to lb (load balancing)
												.uri("lb://currency-exchange"))	//currency-exchange in eureka-naming server and re-routed to currency exchange app
									.route(x -> x.path("/currency-conversion/**")//Incoming url has /currency-conversion/** which will be mapped to lb (load balancing)
												.uri("lb://currency-conversion"))//currency-conversion in eureka-naming server and re-routed to currency conversion
									.route(p -> p.path("/currency-conversion-new/**")
											.filters(f -> f.rewritePath(
													"/currency-conversion-new/(?<segment>.*)", 
													"/currency-conversion-feign/${segment}"))
											.uri("lb://currency-conversion"))
									.build();
		//Using separate function variable just to acclimatize with FUnctional programming
		Function<PredicateSpec, Buildable<Route>> routerPathUri =
					p -> p.path("/get")
						.uri("https://httpbin.org:80");
		RouteLocator route1 = routeLocatorBuilder.routes().route(routerPathUri).build();
		//Using separate function variable just to acclimatize with FUnctional programming
		return routeLocator;
		//return routeLocatorBuilder.routes().build();
	}
}

package com.example;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.BodyFilterFunctions.adaptCachedBody;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Service
class AiSpy {}

@Configuration(proxyBeanMethods = false)
public class RouteConfig {

  /**
   * uri: https://api.openai.com/v1/chat/completions predicates: - Path=/openai/** filters: -
   * StripPrefix=1
   *
   * @return
   */
  // http POST :8080/post hello=world
  @Bean
  public RouterFunction<ServerResponse> readBodyRoute(HttpServletRequest httpServletRequest) {
    return route("openai")
        .route(path("/openai/**"), http("https://api.openai.com/v1/chat/completions"))
        .before(stripPrefix(1))
        .before(
            request -> {
              var body = MvcUtils.cacheAndReadBody(request, String.class);
              var headers = request.headers();
              System.out.println("body = " + body);
              System.out.println("request = " + request);
              return request;
            })
        .before(adaptCachedBody()) // make the body readable for next step
        .build();
    //				.after( (request,response) -> {
    //					ServerResponse.from(response).build( (servletRequest, servletResponse) ->  {
    //
    //			//			System.out.println("body = " + body);
    //						return null;
    //					});
    ////					var httpServletRequest = response.writeTo(new HttpServletResponseWrapper());
    ////					var wrapper = new HttpServletResponseWrapper(servletRequset);
    ////					response
    //
    //					System.out.println("response = " + response);
    //					return response;
    //				}).build();

  }
}
